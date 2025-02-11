package dev.kanovsky.portfolioTracker.controller

import dev.kanovsky.portfolioTracker.dto.LoginRequestDTO
import dev.kanovsky.portfolioTracker.enums.TokenValidityCode
import dev.kanovsky.portfolioTracker.model.User
import dev.kanovsky.portfolioTracker.service.AuthorisationService
import dev.kanovsky.portfolioTracker.service.UserService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller responsible for handling authentication-related requests.
 **/
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val authorisationService: AuthorisationService
) {
    /**
     * Registers a new user.
     * @param user The user details provided in the request body.
     * @return ResponseEntity containing the created user or an error message.
     **/
    @PostMapping("/register")
    fun registerUser(@RequestBody user: User): ResponseEntity<Any> {
        val result = userService.registerUser(user)
        return result.fold(
            onSuccess = { newUser -> ResponseEntity.status(HttpStatus.CREATED).body(newUser) },
            onFailure = { exception ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to exception.message))
            }
        )
    }

    /**
     * Authenticates a user and issues an authentication token.
     * @param loginRequestDTO The login credentials provided in the request body.
     * @param httpResponse The HTTP response to set cookies if necessary.
     * @return ResponseEntity containing the authenticated user or an error message.
     **/
    @PostMapping("/login")
    fun loginUser(
        @RequestBody loginRequestDTO: LoginRequestDTO,
        httpResponse: HttpServletResponse
    ): ResponseEntity<Any> {
        val result = userService.loginUser(loginRequestDTO, httpResponse)

        return result.fold(
            onSuccess = { user -> ResponseEntity.ok(user) },
            onFailure = { exception ->
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to exception.message))
            }
        )
    }

    /**
     * Refreshes the access token using the refresh token stored in cookies.
     * @param request The HTTP request containing the refresh token.
     * @return ResponseEntity containing the new access token or an error message.
     **/
    @PostMapping("/refresh")
    fun refreshAccessToken(request: HttpServletRequest): ResponseEntity<Any> {

        val result = authorisationService.refreshAccessToken(request)

        return result.fold(
            onSuccess = { accessToken -> ResponseEntity.ok(accessToken) },
            onFailure = { exception ->
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to exception.message))
            }
        )
    }

    /**
     * Logs out the user by clearing the refresh token cookie.
     * @param response The HTTP response used to set the cookie.
     * @return ResponseEntity with an empty response indicating successful logout.
     **/
    @PostMapping("/logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Unit> {
        // Create a cookie with an empty value and set expiration to zero to remove it
        val cookie = Cookie("refreshToken", "").apply {
            isHttpOnly = true
            path = "/"
            maxAge = TokenValidityCode.ZERO.seconds
        }
        response.addCookie(cookie)

        return ResponseEntity.ok().build()
    }

}