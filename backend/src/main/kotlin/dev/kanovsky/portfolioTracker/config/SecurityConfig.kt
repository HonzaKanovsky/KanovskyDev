package dev.kanovsky.portfolioTracker.config

import dev.kanovsky.portfolioTracker.enums.TokenValidityCode
import dev.kanovsky.portfolioTracker.security.JwtAuthFilter
import dev.kanovsky.portfolioTracker.security.JwtUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

/**
 * Security configuration class for handling authentication, authorization, and CORS settings.
 **/
@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtUtil: JwtUtil,
    private val jwtAuthFilter: JwtAuthFilter,
    private val userDetailsService: UserDetailsService
) {

    /**
     * Defines security filter chain configuration.
     **/
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() } // Disable CSRF for stateless authentication
            .cors { it.configurationSource(corsConfigurationSource()) } // Enable CORS
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/auth/refresh",
                        "/api/cryptos/**",
                        "/api/cryptos/search",
                        "/api/resume",
                        "/api/endpoints",
                        "/api/contact"
                    )
                    .permitAll() // Publicly accessible endpoints
                    .anyRequest().authenticated() // All other requests require authentication
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // Stateless session
            .authenticationProvider(authenticationProvider()) // Configure authentication provider
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java) // Apply JWT filter

        return http.build()
    }

    /**
     * Configures the authentication provider to use DaoAuthenticationProvider.
     **/
    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        return DaoAuthenticationProvider().apply {
            setUserDetailsService(userDetailsService)
            setPasswordEncoder(passwordEncoder())
        }
    }

    /**
     * Provides the authentication manager for handling authentication requests.
     **/
    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }

    /**
     * Defines password encoder using BCrypt hashing algorithm.
     **/
    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    /**
     * Configures CORS settings to allow frontend communication.
     **/
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:5173") // Allow frontend origin
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf(CorsConfiguration.ALL) // Allow all headers
        configuration.allowCredentials = true
        configuration.maxAge = TokenValidityCode.ONE_HOUR.seconds.toLong() // Cache preflight response for 1 hour

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
