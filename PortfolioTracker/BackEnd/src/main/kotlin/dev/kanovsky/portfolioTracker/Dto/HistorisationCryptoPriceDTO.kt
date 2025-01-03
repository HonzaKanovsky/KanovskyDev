package dev.kanovsky.portfolioTracker.Dto

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class HistorisationCryptoPriceDTO(
    val timestamp: LocalDate,
    val price: BigDecimal
)