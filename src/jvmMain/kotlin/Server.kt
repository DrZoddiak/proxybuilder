
import com.mongodb.ConnectionString
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
import io.ktor.util.pipeline.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

val connectionString: ConnectionString? = System.getenv("MONGODB_URI")?.let {
    ConnectionString("$it?retryWrites=false")
}

val klient =
    if (connectionString != null) KMongo.createClient(connectionString).coroutine else KMongo.createClient().coroutine
val database by lazy {
    klient.getDatabase(connectionString?.database ?: "test")
}

val cardlection = database.getCollection<FinalizedCard>()

fun PipelineContext<*, ApplicationCall>.callId(): Int {
    return call.parameters["id"]?.toInt() ?: error("Invalid delete request")
}

suspend fun PipelineContext<*, ApplicationCall>.respondOk() {
    return call.respond(HttpStatusCode.OK)
}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 9090
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
                    call.respond(cardlection.find().toList())
                }
                post {
                    cardlection.insertOne(call.receive())
                    respondOk()
                }
                delete("/{id}") {
                    val id = callId()
                    cardlection.deleteOne(FinalizedCard::id eq id)
                    respondOk()
                }
                //replace
                post("/{id}/replace") {
                    val id = callId()
                    val card = call.receive<FinalizedCard>()
                    cardlection.replaceOne(FinalizedCard::id eq id, card)
                    respondOk()
                }
                delete {
                    cardlection.drop()
                    respondOk()
                }
            }
        }
    }.start(wait = true)
}