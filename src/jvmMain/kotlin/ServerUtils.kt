import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

fun PipelineContext<*, ApplicationCall>.callId(): Int {
    return call.parameters["id"]?.toInt() ?: error("Invalid delete request")
}

suspend fun PipelineContext<*, ApplicationCall>.respondOk() {
    return call.respond(HttpStatusCode.OK)
}
