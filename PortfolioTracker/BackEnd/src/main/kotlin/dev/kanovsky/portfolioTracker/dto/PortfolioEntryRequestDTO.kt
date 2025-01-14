package dev.kanovsky.portfolioTracker.dto

data class PortfolioEntryRequestDTO(
    val userId: Long,
    val cryptoId: Long,
    val amount: Double
)
