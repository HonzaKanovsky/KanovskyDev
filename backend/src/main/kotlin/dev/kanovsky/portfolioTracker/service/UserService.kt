package dev.kanovsky.portfolioTracker.service

import dev.kanovsky.portfolioTracker.dto.LoginRequestDTO
import dev.kanovsky.portfolioTracker.dto.LoginResponseDTO
import dev.kanovsky.portfolioTracker.dto.UserDetailDTO
import dev.kanovsky.portfolioTracker.enums.RegexPatterns
import dev.kanovsky.portfolioTracker.enums.TokenValidityCode
import dev.kanovsky.portfolioTracker.exceptions.InvalidPasswordException
import dev.kanovsky.portfolioTracker.exceptions.UserNotFoundException
import dev.kanovsky.portfolioTracker.model.User
import dev.kanovsky.portfolioTracker.repository.UserRepository
import dev.kanovsky.portfolioTracker.security.JwtUtil
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

@Service
class UserService(private val userRepository: UserRepository) {
    private val passwordEncoder = BCryptPasswordEncoder()
    private val jwtUtil = JwtUtil()

    fun registerUser(user: User): Result<UserDetailDTO> {
        if (!isUsernameAndEmailValid(user.username, user.email)) {
            return Result.failure(IllegalArgumentException("submitted username or e-mail does not satisfy requirements."))
        }
        if (!isPasswordValid(user.password)) {
            return Result.failure(IllegalArgumentException("Password does not satisfy requirements"))
        }
        val hashedPassword = passwordEncoder.encode(user.password)
        val newUser = user.copy(password = hashedPassword)

        return Result.success(UserDetailDTO(userRepository.save(newUser).toDto()))
    }

    fun loginUser(loginRequestDTO: LoginRequestDTO, httpResponse: HttpServletResponse): Result<LoginResponseDTO> {
        val user = if (userRepository.findByUsername(loginRequestDTO.loginMethod) != null) {
            userRepository.findByUsername(loginRequestDTO.loginMethod)
        } else if (userRepository.findByEmail(loginRequestDTO.loginMethod) != null) {
            userRepository.findByEmail(loginRequestDTO.loginMethod)
        } else {
            null
        }

        if (user == null) {
            return Result.failure(UserNotFoundException("User with name ${loginRequestDTO.loginMethod} not found"))
        }


        if (!passwordEncoder.matches(loginRequestDTO.password, user.password)) {
            return Result.failure(InvalidPasswordException("User with name ${loginRequestDTO.loginMethod} not found"))
        }

        val accessToken = jwtUtil.generateAccessToken(user.username)
        val refreshToken = jwtUtil.generateRefreshToken(user.username)

        //Store refreshToken to cookie
        val cookie = Cookie("refreshToken", refreshToken)
        cookie.isHttpOnly = true
        cookie.path = "/api/auth/refresh"
        cookie.maxAge = TokenValidityCode.ONE_DAY.seconds
        httpResponse.addCookie(cookie)

        return Result.success(LoginResponseDTO(accessToken, "Stored in HTTP-only cookie"))
    }

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