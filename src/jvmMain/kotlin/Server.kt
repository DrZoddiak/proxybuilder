
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
val artlection = database.getCollection<ArtCard>()


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
            anyHost()
        }
        install(Compression) {
            gzip()
        }

        routing {
            get("/") {
                call.respondText(
                    this::class.java.classLoader.getResource("index.html")!!.readText(),
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
                    call.respond(HttpStatusCode.OK)
                }
                delete("/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                    cardlection.deleteOne(FinalizedCard::id eq id)
                    call.respond(HttpStatusCode.OK)
                }
                delete {
                    cardlection.drop()
                    call.respond(HttpStatusCode.OK)
                }
            }
            route(ArtCard.path) {
                get {
                    call.respond(artlection.find().toList())
                }
                post {
                    artlection.insertOne(call.receive())
                    call.respond(HttpStatusCode.OK)
                }
                delete {
                    artlection.drop()
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }.start(wait = true)
}