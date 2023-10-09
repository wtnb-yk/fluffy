package com.fluffy.fluffyapi.controller

import com.fluffy.fluffyapi.controller.gen.apis.MeApiController
import com.fluffy.fluffyapi.controller.gen.models.Me
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import java.util.UUID

@Component
class MeApiControllerImpl: MeApiController {
    override suspend fun getMe(request: ServerRequest): ServerResponse {
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(
                Me(
                    userId = "d2cb7679-44ce-4148-a4e9-4905f40a5314",
                    userName = "owner"
                )
            )
    }
}