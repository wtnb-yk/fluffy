package com.fluffy.fluffyapi.controller

import com.fluffy.fluffyapi.gateway.OwnersRepository
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/owners")
class OwnersController(private val ownersRepository: OwnersRepository) {
    @GetMapping
    fun index(): MutableList<Owner> {
        return ownersRepository.fetchAll().map { ownerRecord ->
            Owner(ownerRecord.id!!, ownerRecord.name!!)
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) {
        ownersRepository.delete(id)
    }

    @PostMapping
    fun create(@RequestBody request: Owner) {
        ownersRepository.create(request.name)
    }

    data class Owner(val id: UUID?, val name: String)
}
