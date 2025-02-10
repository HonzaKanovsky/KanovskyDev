package dev.kanovsky.portfolioTracker.dto

import java.math.BigDecimal

data class PortfolioEntryDTO(
    val crypto: CryptoDTO,
    var currentPrice: BigDecimal = BigDecimal.ZERO,
    val amount: BigDecimal
)
