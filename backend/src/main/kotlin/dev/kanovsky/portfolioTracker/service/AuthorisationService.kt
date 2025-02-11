package dev.kanovsky.portfolioTracker.service

import dev.kanovsky.portfolioTracker.exceptions.InvalidRefreshTokenException
import dev.kanovsky.portfolioTracker.exceptions.NoCookiesFoundException
import dev.kanovsky.portfolioTracker.exceptions.RefreshTokenNotFoundException
import dev.kanovsky.portfolioTracker.repository.UserRepository
import dev.kanovsky.portfolioTracker.security.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service

/**
 * Service responsible for handling authorization-related operations.
 **/
@Service
class AuthorisationService(private val userRepository: UserRepository) {
    val jwtUtil = JwtUtil()

    /**
     * Checks if the user making the request has the required access.
     * @param requestedID The ID of the user whose data is being accessed.
     * @param token The JWT token extracted from the request.
     * @return `true` if the token matches the requested user's ID, otherwise `false`.
     **/
    fun checkAllowedAccess(requestedID: Long, token: String): Boolean {
        val usernameFromToken = jwtUtil.extractUsername(token)
        val databaseUserName = userRepository.getUserById(requestedID).username

        return databaseUserName == usernameFromToken
    }

    /**
     * Refreshes the access token using a valid refresh token stored in cookies.
     * @param request The HTTP request containing cookies.
     * @return A new access token if the refresh token is valid, or an error result otherwise.
     **/
    fun refreshAccessToken(request: HttpServletRequest): Result<String> {
        val cookies = request.cookies ?: return Result.failure(NoCookiesFoundException("No cookies found"))

        // Retrieve cookies from the request
        val refreshToken = cookies.find { it.name == "refreshToken" }?.value
            ?: return Result.failure(RefreshTokenNotFoundException("Refresh token not found"))

        // Extract the username from the refresh token
        val username = jwtUtil.extractUsername(refreshToken)
            ?: return Result.failure(InvalidRefreshTokenException("Invalid refresh token"))

        // Generate a new access token and return it
        return Result.success(jwtUtil.generateAccessToken(username))
    }
}