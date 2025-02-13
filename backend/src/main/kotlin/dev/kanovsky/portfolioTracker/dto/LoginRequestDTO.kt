package dev.kanovsky.portfolioTracker.dto

data class LoginRequestDTO(
    var loginMethod: String,
    var password: String
)