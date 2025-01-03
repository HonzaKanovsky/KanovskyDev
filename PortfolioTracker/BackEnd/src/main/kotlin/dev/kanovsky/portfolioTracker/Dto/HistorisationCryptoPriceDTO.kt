package dev.kanovsky.portfolioTracker.Dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class HistorisationCryptoPriceDTO(
    val timestamp: LocalDateTime,
    val price: BigDecimal
)