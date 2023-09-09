package com.fluffy.fluffyapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
class FluffyApiApplication {
    @Bean
    fun corsConfigurer(): WebMvcConfigurer? {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
//                registry.addMapping("/**").allowedOrigins("http://localhost:10280")
//                    .allowedMethods("GET", "POST", "DELETE")
                registry.addMapping("/**")
                    .allowedOrigins("*")  // すべてのオリジンを許可
                    .allowedMethods("*")  // すべてのHTTPメソッドを許可
                    .allowedHeaders("*")  // すべてのヘッダーを許可
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<FluffyApiApplication>(*args)
}
