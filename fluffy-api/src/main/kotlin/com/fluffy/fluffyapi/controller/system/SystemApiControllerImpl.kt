package com.fluffy.fluffyapi.controller.system

import com.fluffy.fluffyapi.controller.gen.apis.SystemApiController
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class SystemApiControllerImpl : SystemApiController {
    override suspend fun healthCheck(request: ServerRequest): ServerResponse {
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_PLAIN)
            .bodyValueAndAwait("ping")
    }
}