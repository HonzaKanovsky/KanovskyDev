package dev.kanovsky.portfolioTracker.dto

class LoginResponseDTO(
    val userId: Long,
    val accessToken: String,
    val refreshToken: String
)