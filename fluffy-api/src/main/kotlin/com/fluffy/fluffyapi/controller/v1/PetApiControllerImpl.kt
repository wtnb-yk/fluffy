package com.fluffy.fluffyapi.controller.v1

import com.fluffy.fluffyapi.controller.gen.apis.PetsApiController
import com.fluffy.fluffyapi.controller.gen.models.Pet
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class PetApiControllerImpl: PetsApiController {
    override suspend fun getPets(request: ServerRequest): ServerResponse {
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(
                listOf(
                    Pet(
                        id = "6ef0a13b-6b86-48a2-8a4c-2fce55d89844",
                        name = "daifuku"
                    )
                )
            )
    }
}