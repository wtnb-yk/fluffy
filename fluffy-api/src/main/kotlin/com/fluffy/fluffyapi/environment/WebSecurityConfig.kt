package com.fluffy.fluffyapi.environment

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurityConfig(
    val config: CustomConfig,
//    @Qualifier("manager") val SessionFilterHelper: SessionCookieFilterHelper,
) {

//    val API_PATH_MATCHER = "/fluffy-api/**"

    @Autowired
//    private lateinit var serverAuthenticationEntryPoint: FluffyAuthenticationEntryPoint

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {

        http
            .withAllowedOrigins()
            .withDisabledHttpBasicAuth()
            .withPublicEndPoint("/ping")
            .csrf{
                it.disable()
            }

        return http.build()
    }

    private fun ServerHttpSecurity.withAllowedOrigins(): ServerHttpSecurity =
        this.cors {
            it.configurationSource(corsConfigurationSource())
        }

    private fun ServerHttpSecurity.withDisabledHttpBasicAuth(): ServerHttpSecurity =
        this.formLogin { formLogin ->
            formLogin
                .disable().httpBasic {
                    it.authenticationEntryPoint(HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
                }
        }

//    private fun ServerHttpSecurity.withCookieSessionAuthentication(
//        path: String,
//        filter: WebFilter
//    ): ServerHttpSecurity =
//        this.authorizeExchange()
//            .pathMatchers(path)
//            .authenticated()
//            .and()
//            .addFilterAt(filter, SecurityWebFiltersOrder.AUTHENTICATION)

    private fun ServerHttpSecurity.withPublicEndPoint(path: String): ServerHttpSecurity =
        this.authorizeExchange {
            it.pathMatchers(path)
                .permitAll()
        }


//    private fun ServerHttpSecurity.withAuthenticationEntryPoint(): ServerHttpSecurity =
//        this.exceptionHandling()
////            .authenticationEntryPoint(serverAuthenticationEntryPoint)
//            .and()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.addAllowedMethod(CorsConfiguration.ALL)
        configuration.addAllowedHeader(CorsConfiguration.ALL)
//        configuration.allowCredentials = true
//        configuration.allowedOrigins = config.allowedOrigins.split(",")
        configuration.applyPermitDefaultValues()
        configuration.addExposedHeader("location")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)

        return source
    }
}

//@Configuration
//class SessionFilter {
//    @Bean("manager")
//    fun SessionCookieFilterHelper(
//        @Qualifier("redisTemplate") redisTemplate: StringRedisTemplate
//    ): SessionCookieFilterHelper =
//        SessionCookieFilterHelper(redisTemplate, ManagerSessionDeserializer())
//}
