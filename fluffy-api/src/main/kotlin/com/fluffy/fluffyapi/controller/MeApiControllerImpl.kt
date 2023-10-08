package com.fluffy.fluffyapi.controller

import com.fluffy.fluffyapi.controller.gen.apis.MeApiController
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class MeApiControllerImpl: MeApiController {
    data class MeResponse (
        val userName: String = "owner"
    )

    override suspend fun getMe(request: ServerRequest): ServerResponse {
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(MeResponse())
    }
}