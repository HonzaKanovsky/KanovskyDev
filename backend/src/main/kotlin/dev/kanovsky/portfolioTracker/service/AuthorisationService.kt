package dev.kanovsky.portfolioTracker.service

import dev.kanovsky.portfolioTracker.exceptions.InvalidRefreshTokenException
import dev.kanovsky.portfolioTracker.exceptions.NoCookiesFoundException
import dev.kanovsky.portfolioTracker.exceptions.RefreshTokenNotFoundException
import dev.kanovsky.portfolioTracker.repository.UserRepository
import dev.kanovsky.portfolioTracker.security.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service

@Service
class AuthorisationService(private val userRepository: UserRepository) {
    val jwtUtil = JwtUtil()

    fun checkAllowedAccess(requestedID: Long, token: String): Boolean {
        val usernameFromToken = jwtUtil.extractUsername(token)
        val databaseUserName = userRepository.getUserById(requestedID).username

        return databaseUserName == usernameFromToken
    }

    fun refreshAccessToken(request: HttpServletRequest): Result<String> {
        val cookies = request.cookies ?: return Result.failure(NoCookiesFoundException("No cookies found"))

        val refreshToken = cookies.find { it.name == "refreshToken" }?.value
            ?: return Result.failure(RefreshTokenNotFoundException("Refresh token not found"))

        val username = jwtUtil.extractUsername(refreshToken)
            ?: return Result.failure(InvalidRefreshTokenException("Invalid refresh token"))

        return Result.success(jwtUtil.generateAccessToken(username))
    }
}