package dev.kanovsky.portfolioTracker.repository

import dev.kanovsky.portfolioTracker.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository for managing user-related database operations.
 **/
@Repository
interface UserRepository : JpaRepository<User, Long> {
    /**
     * Finds a user by their ID.
     * @param id The unique identifier of the user.
     * @return The found User entity, or `null` if not found.
     **/
    fun findUserById(id: Long): User?

    /**
     * Finds a user by their username.
     * @param username The username to search for.
     * @return The found User entity, or `null` if not found.
     **/
    fun findByUsername(username: String): User?

    /**
     * Finds a user by their email address.
     * @param email The email to search for.
     * @return The found User entity, or `null` if not found.
     **/
    fun findByEmail(email: String): User?

    /**
     * Retrieves a user by ID. Unlike `findUserById`, this method assumes the user exists.
     * If the user is not found, an exception will be thrown.
     * @param id The unique identifier of the user.
     * @return The found User entity.
     **/
    fun getUserById(id: Long): User

}