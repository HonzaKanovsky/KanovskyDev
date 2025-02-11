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
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

/**
 * Service responsible for handling user-related operations such as registration and login.
 **/
@Service
class UserService(private val userRepository: UserRepository) {

    private val passwordEncoder = BCryptPasswordEncoder()
    private val jwtUtil = JwtUtil()
    private val logger = LoggerFactory.getLogger(UserService::class.java)


    /**
     * Registers a new user.
     * @param user The user object containing registration details.
     * @return A Result containing the created user's details or an error if registration fails.
     **/
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

    /**
     * Authenticates a user and issues JWT tokens.
     * @param loginRequestDTO Contains the login credentials (username or email, and password).
     * @param httpResponse The HTTP response to store the refresh token in a cookie.
     * @return A Result containing the access token and success message, or an error if login fails.
     **/
    fun loginUser(loginRequestDTO: LoginRequestDTO, httpResponse: HttpServletResponse): Result<LoginResponseDTO> {
        // Find user by username or email
        val user = userRepository.findByUsername(loginRequestDTO.loginMethod)
            ?: userRepository.findByEmail(loginRequestDTO.loginMethod)
            ?: run {
                logger.warn("Login failed: User not found for ${loginRequestDTO.loginMethod}")
                return Result.failure(UserNotFoundException("User with name ${loginRequestDTO.loginMethod} not found"))
            }

        // Validate password
        if (!passwordEncoder.matches(loginRequestDTO.password, user.password)) {
            logger.warn("Login failed: Incorrect password for user ${loginRequestDTO.loginMethod}")
            return Result.failure(InvalidPasswordException("User with name ${loginRequestDTO.loginMethod} not found"))
        }

        // Generate JWT tokens
        val accessToken = jwtUtil.generateAccessToken(user.username)
        val refreshToken = jwtUtil.generateRefreshToken(user.username)

        logger.info("User ${user.username} logged in successfully")

        // Store refresh token in an HTTP-only cookie
        val cookie = Cookie("refreshToken", refreshToken)
        cookie.isHttpOnly = true
        cookie.path = "/api/auth/refresh"
        cookie.maxAge = TokenValidityCode.ONE_DAY.seconds
        httpResponse.addCookie(cookie)

        return Result.success(LoginResponseDTO(user.id, accessToken, "Stored in HTTP-only cookie"))
    }

    /**
     * Validates username and email format.
     * @param username The username to validate.
     * @param email The email to validate.
     * @return `true` if both username and email are valid, `false` otherwise.
     **/
    private fun isUsernameAndEmailValid(username: String, email: String): Boolean {
        if (!StringUtils.hasText(username) || username.length < 3 || username.length > 30) return false
        if (!StringUtils.hasText(email) || email.length < 6) return false

        return areUserCredentialsUnique(username, email) &&
                RegexPatterns.USERNAME.matches(username) &&
                RegexPatterns.EMAIL.matches(email)
    }

    /**
     * Checks if the username and email are unique in the database.
     * @param userName The username to check.
     * @param email The email to check.
     * @return `true` if both username and email are unique, `false` otherwise.
     **/
    private fun areUserCredentialsUnique(userName: String, email: String): Boolean {
        return !(userRepository.findByUsername(userName) != null || userRepository.findByEmail(email) != null)
    }

    /**
     * Validates password format.
     * @param password The password to validate.
     * @return `true` if the password meets security requirements, `false` otherwise.
     **/
    private fun isPasswordValid(password: String): Boolean {
        if (!StringUtils.hasText(password) || password.length < 5 || password.length > 30) return false
        return RegexPatterns.PASSWORD.matches(password)
    }
}