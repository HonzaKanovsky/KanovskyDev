package dev.kanovsky.portfolioTracker.dto

data class UserRegistrationDTO(
    val id: Long,
    val username: String,
    val email: String,
    val password: String,
)
