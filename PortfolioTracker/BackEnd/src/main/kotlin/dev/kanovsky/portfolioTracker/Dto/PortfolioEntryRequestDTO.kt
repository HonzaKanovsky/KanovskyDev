package dev.kanovsky.portfolioTracker.Dto

data class PortfolioEntryRequestDTO(
    val userId: Long,
    val cryptoId: Long,
    val amount: Double
)
