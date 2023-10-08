package com.codmon.facilitystore.environment

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.WebFilter

@Configuration
class ApiPathFilterConfig {

    // pingを除くすべてのリクエストが/facility-store-apiから始まるので、
    // ルータが処理する前に/facility-store-apiを削除する
    @Bean
    fun pathStripFilter(): WebFilter {
        return WebFilter { exchange, chain ->
            val path = exchange.request.path.toString()
            if (path.startsWith("/facility-store-api")) {
                val newPath = path.removePrefix("/facility-store-api")
                val newRequest = exchange.request.mutate().path(newPath).build()
                chain.filter(exchange.mutate().request(newRequest).build())
            } else {
                chain.filter(exchange)
            }
        }
    }
}
