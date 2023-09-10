/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package com.fluffy.fluffyapi.controller.gen.apis

import com.fluffy.fluffyapi.controller.system.SystemApiController
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class SystemApiRouter {
    @Bean
    fun SystemApiRoutes(handler: SystemApiController) = coRouter {
        GET("/ping", handler::healthCheck)
    }
}