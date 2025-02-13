package dev.kanovsky.portfolioTracker.dto

data class ApiParameterInfoDTO(
    val name: String,
    val type: String,
    val fields: List<ApiFieldInfoDTO>? = null
)