
import DatabaseConnection.collection
import ServerSettings.port
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.litote.kmongo.eq

fun main() {
    embeddedServer(Netty, port) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Delete)
            val targetSrc = "scryfall.com"
            val embedSrc = "embed.$targetSrc"
            headersOf(
                "connect-src" to listOf("api.$targetSrc", embedSrc),
                "img-src" to listOf("*.scryfall.io"),
                "style-src" to listOf(embedSrc),
                "script-src" to listOf(embedSrc),
                "font-src" to listOf(embedSrc)
            )
            anyHost()
        }
        install(Compression) {
            gzip()
        }

        routing {
            get("/") {
                call.respondText(
                    this::class.java.classLoader.getResource("index.html")?.readText()
                        ?: error("Failed to load resource"),
                    ContentType.Text.Html
                )
            }
            static("/") {
                resources("")
            }
            route(FinalizedCard.path) {
                get {
                    call.respond(collection.find().toList())
                }
                post {
                    collection.insertOne(call.receive())
                    respondOk()
                }
                delete("/{id}") {
                    val id = callId()
                    collection.deleteOne(FinalizedCard::id eq id)
                    respondOk()
                }
                post("/{id}/replace") {
                    val id = callId()
                    val card = call.receive<FinalizedCard>()
                    collection.replaceOne(FinalizedCard::id eq id, card)
                    respondOk()
                }
                delete {
                    collection.drop()
                    respondOk()
                }
            }
        }
    }.start(wait = true)
}