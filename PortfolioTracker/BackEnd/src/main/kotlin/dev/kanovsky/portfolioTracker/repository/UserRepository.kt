package dev.kanovsky.portfolioTracker.repository

import dev.kanovsky.portfolioTracker.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findUserById(id: Long): User?
    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?
}