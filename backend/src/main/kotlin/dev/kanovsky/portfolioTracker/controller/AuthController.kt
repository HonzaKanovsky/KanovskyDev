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

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val authorisationService: AuthorisationService
) {

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

    @PostMapping("/logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Unit> {

        val cookie = Cookie("refreshToken", "").apply {
            isHttpOnly = true
            path = "/"
            maxAge = TokenValidityCode.ZERO.seconds
        }
        response.addCookie(cookie)

        return ResponseEntity.ok().build()

    }

}