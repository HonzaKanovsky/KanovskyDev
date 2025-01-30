package dev.kanovsky.portfolioTracker.controller

import dev.kanovsky.portfolioTracker.dto.ApiResponse
import dev.kanovsky.portfolioTracker.dto.LoginRequestDTO
import dev.kanovsky.portfolioTracker.dto.LoginResponseDTO
import dev.kanovsky.portfolioTracker.dto.UserDetailDTO
import dev.kanovsky.portfolioTracker.model.User
import dev.kanovsky.portfolioTracker.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(private val userService: UserService) {

    @PostMapping("/register")
    fun registerUser(@RequestBody user: User): ResponseEntity<ApiResponse<UserDetailDTO>> {
        val response = userService.registerUser(user)
        return ResponseEntity.status(
            if (response.success) HttpStatus.CREATED else HttpStatus.BAD_REQUEST
        ).body(response)
    }

    @GetMapping("/login")
    fun loginUser(@RequestBody loginRequestDTO: LoginRequestDTO): ResponseEntity<ApiResponse<LoginResponseDTO>> {
        val response = userService.loginUser(loginRequestDTO)
        return ResponseEntity.status(
            if (response.success) HttpStatus.CREATED else HttpStatus.UNAUTHORIZED
        ).body(response)
    }
}