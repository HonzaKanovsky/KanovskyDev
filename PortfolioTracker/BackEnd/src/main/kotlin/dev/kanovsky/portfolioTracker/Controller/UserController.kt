package dev.kanovsky.portfolioTracker.Controller

import dev.kanovsky.portfolioTracker.Dto.ApiResponse
import dev.kanovsky.portfolioTracker.Dto.UserDTO
import dev.kanovsky.portfolioTracker.Model.User
import dev.kanovsky.portfolioTracker.Service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {
    @PostMapping("/register")
    fun registerUser(@RequestBody user: User): ResponseEntity<ApiResponse<UserDTO>> {
        return try {
            val response = userService.registerUser(user)
            ResponseEntity.status(
                if (response.success) HttpStatus.CREATED else HttpStatus.BAD_REQUEST
            ).body(response)
        } catch (e: Exception) {
            val response =
                ApiResponse<UserDTO>(success = false, message = "${e.message}")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
        }
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): User = userService.getUserById(id)
}