package dev.kanovsky.portfolioTracker.Dto

data class ApiResponse<T> (
    val success: Boolean,
    val message: String,
    val data: T? = null
)
