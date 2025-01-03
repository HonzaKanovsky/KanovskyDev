package dev.kanovsky.portfolioTracker.Dto

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.Date

data class ApiResponse<T> (
    val success: Boolean,
    val message: String,
    val timestamp: LocalDateTime? = LocalDateTime.now(),
    val data: T? = null
)
