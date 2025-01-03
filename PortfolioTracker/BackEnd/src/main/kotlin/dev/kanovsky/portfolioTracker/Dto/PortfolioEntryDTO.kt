package dev.kanovsky.portfolioTracker.Dto

import java.math.BigDecimal

data class PortfolioEntryDTO(
    val id: Long,
    val crypto: CryptoDTO,
    val amount: BigDecimal
)
