package dev.kanovsky.portfolioTracker.Dto

import java.math.BigDecimal

data class CryptoDTO(
    val id: Long,
    val symbol: String,
    val name: String,
    val price: BigDecimal,
    val marketCap: Long?
)