package com.fluffy.fluffyapi.controller.system

import com.fluffy.fluffyapi.controller.gen.apis.SystemApiControllerInterface
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class SystemApiController : SystemApiControllerInterface {
    data class HealthCheckResponse(val message: String = "facility-store-api ping", val status: String = "success")

    override suspend fun healthCheck(request: ServerRequest): ServerResponse {
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(HealthCheckResponse())
    }
}