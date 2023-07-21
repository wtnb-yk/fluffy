package com.fluffy.fluffyapi.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/user")
class UserController {
    @GetMapping
    fun index() = listOf(
        User(UUID.randomUUID(), "Mike"),
        User(UUID.randomUUID(), "Ann")
    )
    data class User(val id: UUID, val name: String)
}
