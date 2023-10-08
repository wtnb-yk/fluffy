package com.fluffy.fluffyapi.environment

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "custom")
class CustomConfig {
    lateinit var env: String
    lateinit var allowedOrigins: String
}
