package dev.kanovsky.portfolioTracker.controller

import dev.kanovsky.portfolioTracker.dto.*
import dev.kanovsky.portfolioTracker.model.User
import dev.kanovsky.portfolioTracker.security.JwtUtil
import dev.kanovsky.portfolioTracker.service.UserService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(private val userService: UserService) {

    val jwtUtil = JwtUtil()

    @PostMapping("/register")
    fun registerUser(@RequestBody user: User): ResponseEntity<ApiResponse<UserDetailDTO>> {
        val response = userService.registerUser(user)
        return ResponseEntity.status(
            if (response.success) HttpStatus.CREATED else HttpStatus.BAD_REQUEST
        ).body(response)
    }

    @GetMapping("/login")
    fun loginUser(
        @RequestBody loginRequestDTO: LoginRequestDTO,
        httpResponse: HttpServletResponse
    ): ResponseEntity<ApiResponse<LoginResponseDTO>> {
        val response = userService.loginUser(loginRequestDTO, httpResponse)
        return ResponseEntity.status(
            if (response.success) HttpStatus.CREATED else HttpStatus.UNAUTHORIZED
        ).body(response)
    }

    @PostMapping("/refresh")
    fun refreshAccessToken(request: HttpServletRequest): ResponseEntity<ApiResponse<AccessTokenDTO>> {
        val cookies = request.cookies ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse(success = false, message = "No cookies found"))

        val refreshToken = cookies.find { it.name == "refreshToken" }?.value
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse(success = false, message = "Refresh token not found"))

        val username = jwtUtil.extractUsername(refreshToken)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse(success = false, message = "Invalid refresh token"))

        val newAccessToken = jwtUtil.generateAccessToken(username)

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Access token refreshed",
                data = AccessTokenDTO(newAccessToken)
            )
        )
    }

    @PostMapping("/logout")
    fun logout(response: HttpServletResponse): ResponseEntity<ApiResponse<Unit>> {
        val cookie = Cookie("refreshToken", "")
        cookie.isHttpOnly = true
        cookie.path = "/api/auth/refresh"
        cookie.maxAge = 0 // 🔹 Remove cookie
        response.addCookie(cookie)

        return ResponseEntity.ok(ApiResponse(success = true, message = "Logged out successfully"))
    }

}