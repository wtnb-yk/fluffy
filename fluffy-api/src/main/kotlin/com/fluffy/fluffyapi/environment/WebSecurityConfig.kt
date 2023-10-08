package com.codmon.facilitystore.environment

import com.codmon.common.security.*
import com.codmon.facilitystore.controller.error.FacilityStoreAuthenticationEntryPoint
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.server.WebFilter

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurityConfig(
    val config: CustomConfig,
    @Qualifier("manager") val codmonMangerSessionFilterHelper: SessionCookieFilterHelper,
    @Qualifier("parents") val codmonParentsSessionFilterHelper: SessionCookieFilterHelper
) {

    val API_PATH_MATCHER = "/facility-store-api/admin/**"
    val API_PATH_MATCHER_PARENT = "/facility-store-api/parents/**"

    @Autowired
    private lateinit var serverAuthenticationEntryPoint: FacilityStoreAuthenticationEntryPoint

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http
            .withAllowedOrigins()
            .withDisabledHttpBasicAuth()
            .withPublicEndPoint("/ping")
            .withCookieSessionAuthentication(
                API_PATH_MATCHER,
                ManagerSessionCookieFilter(codmonMangerSessionFilterHelper, API_PATH_MATCHER)
            )
            .withCookieSessionAuthentication(
                API_PATH_MATCHER_PARENT,
                ParentSessionCookieFilter(codmonParentsSessionFilterHelper, API_PATH_MATCHER_PARENT)
            )
            .withAuthenticationEntryPoint()
            .csrf().disable()

        return http.build()
    }

    private fun ServerHttpSecurity.withAllowedOrigins(): ServerHttpSecurity =
        this.cors()
            .configurationSource(corsConfigurationSource())
            .and()

    private fun ServerHttpSecurity.withDisabledHttpBasicAuth(): ServerHttpSecurity =
        this.formLogin().disable().httpBasic()
            .authenticationEntryPoint(HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
            .and()

    private fun ServerHttpSecurity.withCookieSessionAuthentication(
        path: String,
        filter: WebFilter
    ): ServerHttpSecurity =
        this.authorizeExchange()
            .pathMatchers(path)
            .authenticated()
            .and()
            .addFilterAt(filter, SecurityWebFiltersOrder.AUTHENTICATION)

    private fun ServerHttpSecurity.withPublicEndPoint(path: String): ServerHttpSecurity =
        this.authorizeExchange()
            .pathMatchers(path)
            .permitAll()
            .and()

    private fun ServerHttpSecurity.withAuthenticationEntryPoint(): ServerHttpSecurity =
        this.exceptionHandling()
            .authenticationEntryPoint(serverAuthenticationEntryPoint)
            .and()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.addAllowedMethod(CorsConfiguration.ALL)
        configuration.addAllowedHeader(CorsConfiguration.ALL)
        configuration.allowCredentials = true
        configuration.allowedOrigins = config.allowedOrigins.split(",")
        configuration.applyPermitDefaultValues()
        configuration.addExposedHeader("location")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)

        return source
    }
}

@Configuration
class SessionFilter {
    @Bean("manager")
    fun managerSessionCookieFilterHelper(
        @Qualifier("redisTemplate") redisTemplate: StringRedisTemplate
    ): SessionCookieFilterHelper =
        SessionCookieFilterHelper(redisTemplate, ManagerSessionDeserializer())

    @Bean("parents")
    fun parentsSessionCookieFilterHelper(
        @Qualifier("parentsRedisTemplate") redisTemplate: StringRedisTemplate
    ): SessionCookieFilterHelper =
        SessionCookieFilterHelper(redisTemplate, ParentSessionDeserializer())
}
