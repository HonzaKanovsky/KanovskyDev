package dev.kanovsky.portfolioTracker.dto

data class LoginResponseDTO(
    val userId: Long,
    val accessToken: String,
    val refreshToken: String
)