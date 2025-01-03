package dev.kanovsky.portfolioTracker.Service

import dev.kanovsky.portfolioTracker.Model.User
import dev.kanovsky.portfolioTracker.Repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun registerUser(user: User): User {
        if (userRepository.findByUsername(user.username) != null) {
            throw IllegalArgumentException("Username ${user.username} is already taken")
        }
        if (userRepository.findByEmail(user.email) != null) {
            throw IllegalArgumentException("Email ${user.email} is already registered")
        }
        return userRepository.save(user)
    }

    fun getUserById(id: Long): User =
        userRepository.findById(id).orElseThrow { IllegalArgumentException("User with id $id not found") }
}