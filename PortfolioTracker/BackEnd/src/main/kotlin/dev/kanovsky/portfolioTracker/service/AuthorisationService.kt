package dev.kanovsky.portfolioTracker.service

import dev.kanovsky.portfolioTracker.repository.UserRepository
import dev.kanovsky.portfolioTracker.security.JwtUtil
import org.springframework.stereotype.Service

@Service
class AuthorisationService(private val userRepository: UserRepository) {
    val jwtUtil = JwtUtil()

    fun checkAllowedAccess(requestedID: Long, token: String): Boolean {
        val usernameFromToken = jwtUtil.extractUsername(token)
        val databaseUserName = userRepository.getUserById(requestedID).username

        return databaseUserName == usernameFromToken
    }
}