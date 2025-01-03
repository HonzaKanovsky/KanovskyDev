package dev.kanovsky.portfolioTracker.Controller

import dev.kanovsky.portfolioTracker.Model.User
import dev.kanovsky.portfolioTracker.Service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {
    @PostMapping("/register")
    fun registerUser(@RequestBody user: User): User = userService.registerUser(user)

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): User = userService.getUserById(id)
}