package dev.kanovsky.portfolioTracker.dto

import java.math.BigDecimal

data class PortfolioEntryDTO(
    val id: Long,
    val crypto: CryptoDTO,
    val amount: BigDecimal
)
