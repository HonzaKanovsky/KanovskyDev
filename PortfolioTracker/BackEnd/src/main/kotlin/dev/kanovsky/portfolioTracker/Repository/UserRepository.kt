package dev.kanovsky.portfolioTracker.Repository

import dev.kanovsky.portfolioTracker.Model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User,Long> {
    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?
}