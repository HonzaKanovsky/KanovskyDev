package dev.kanovsky.portfolioTracker.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * JWT authentication filter that validates JWT tokens for each request.
 **/
@Component
class JwtAuthFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    /**
     * Extracts the JWT token from the Authorization header and validates it.
     * If valid, sets the authenticated user in the SecurityContext.
     **/
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        // If there's no Authorization header or it doesn't start with "Bearer ", continue the filter chain
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.substring(7) // Remove "Bearer " prefix
        val username = jwtUtil.extractUsername(token)

        // Ensure username is extracted and no authentication is already present
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)

            // Validate the token against the extracted username
            if (jwtUtil.isTokenValid(token, username)) {
                SecurityContextHolder.getContext().authentication =
                    org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities
                    )
            }
        }

        // Continue the request processing
        filterChain.doFilter(request, response)
    }
}
