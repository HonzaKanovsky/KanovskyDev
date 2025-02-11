package dev.kanovsky.portfolioTracker.security

import dev.kanovsky.portfolioTracker.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Custom implementation of Spring Security's UserDetailsService.
 * This service is used for loading user details during authentication.
 **/
@Service
class CustomUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    /**
     * Loads a user by their username.
     * @param username The username of the user to be loaded.
     * @return A UserDetails object containing the user's authentication details.
     * @throws UsernameNotFoundException if the user is not found in the database.
     **/
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")

        return User(user.username, user.password, emptyList())
    }
}
