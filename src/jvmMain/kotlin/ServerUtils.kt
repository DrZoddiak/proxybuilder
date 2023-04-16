import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

internal fun PipelineContext<*, ApplicationCall>.callId(): Int {
    return call.parameters["id"]?.toInt() ?: error("Invalid delete request")
}

internal suspend fun PipelineContext<*, ApplicationCall>.respondOk() {
    return call.respond(HttpStatusCode.OK)
}
