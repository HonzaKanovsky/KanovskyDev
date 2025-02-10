package dev.kanovsky.portfolioTracker.controller

import dev.kanovsky.portfolioTracker.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {
    
    @DeleteMapping
    fun DeleteUser() {
        TODO("Not implemented yet")
    }

    @PostMapping
    fun ModifyUser() {
        TODO("Not implemented yet")
    }
}