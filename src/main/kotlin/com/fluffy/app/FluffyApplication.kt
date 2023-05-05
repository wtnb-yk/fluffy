package com.fluffy.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FluffyApplication

fun main(args: Array<String>) {
    runApplication<FluffyApplication>(*args)
}
