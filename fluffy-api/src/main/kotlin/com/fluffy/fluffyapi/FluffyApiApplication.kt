package com.fluffy.fluffyapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FluffyApiApplication

fun main(args: Array<String>) {
    runApplication<FluffyApiApplication>(*args)
}
