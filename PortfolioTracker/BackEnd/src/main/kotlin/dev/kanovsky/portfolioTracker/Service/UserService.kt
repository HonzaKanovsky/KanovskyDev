package dev.kanovsky.portfolioTracker.Service

import dev.kanovsky.portfolioTracker.Dto.ApiResponse
import dev.kanovsky.portfolioTracker.Dto.UserDTO
import dev.kanovsky.portfolioTracker.Enums.RegexPatterns
import dev.kanovsky.portfolioTracker.Model.User
import dev.kanovsky.portfolioTracker.Repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

@Service
class UserService(private val userRepository: UserRepository) {

    fun registerUser(user: User): ApiResponse<UserDTO> {
        if (!isUsernameAndEmailValid(user.username, user.email)) {
            return ApiResponse(
                success = false,
                message = "submitted username or e-mail is already  or does not satisfy requirements."
            )
        }
        if (!isPasswordValid(user.password)) {
            return ApiResponse(
                success = false,
                message = "Password does not satisfy requirements"
            )
        }
        return ApiResponse(
            success = true,
            message = "User was succesfuly registered",
            data = userRepository.save(user).toDto()
        )
    }

    fun getUserById(id: Long): User =
        userRepository.findById(id).orElseThrow { IllegalArgumentException("User with id $id not found") }

    private fun isUsernameAndEmailValid(username: String, email: String): Boolean {
        if (!StringUtils.hasText(username) || username.length < 3 || username.length > 30) {
            return false
        }

        if (!StringUtils.hasText(email) || email.length < 6) {
            return false
        }

        return areUserCredentialsUnique(username, email) &&
                RegexPatterns.USERNAME.matches(username) &&
                RegexPatterns.EMAIL.matches(email)
    }

    private fun areUserCredentialsUnique(userName: String, email: String): Boolean {
        return !(userRepository.findByUsername(userName) != null || userRepository.findByEmail(email) != null)
    }

    private fun isPasswordValid(password: String): Boolean {
        if (!StringUtils.hasText(password) || password.length < 5 || password.length > 30) {
            return false
        }
        return RegexPatterns.PASSWORD.matches(password)
    }

}