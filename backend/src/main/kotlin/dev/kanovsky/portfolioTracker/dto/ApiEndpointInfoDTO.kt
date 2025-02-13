package dev.kanovsky.portfolioTracker.dto

data class ApiEndpointInfoDTO(
    val path: String,
    val method: String,
    val description: String,
    val parameters: List<ApiParameterInfoDTO>
)