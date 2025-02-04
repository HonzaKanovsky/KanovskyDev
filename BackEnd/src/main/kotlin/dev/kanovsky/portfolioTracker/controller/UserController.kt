package dev.kanovsky.portfolioTracker.controller

import dev.kanovsky.portfolioTracker.dto.ApiResponse
import dev.kanovsky.portfolioTracker.dto.UserDetailDTO
import dev.kanovsky.portfolioTracker.model.User
import dev.kanovsky.portfolioTracker.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {
    @PostMapping("/register")
    fun registerUser(@RequestBody user: User): ResponseEntity<ApiResponse<UserDetailDTO>> {
        val response = userService.registerUser(user)
        return ResponseEntity.status(
            if (response.success) HttpStatus.CREATED else HttpStatus.BAD_REQUEST
        ).body(response)
    }

    @DeleteMapping
    fun DeleteUser() {
        TODO("Not implemented yet")
    }

    @PostMapping
    fun ModifyUser() {
        TODO("Not implemented yet")
    }
}