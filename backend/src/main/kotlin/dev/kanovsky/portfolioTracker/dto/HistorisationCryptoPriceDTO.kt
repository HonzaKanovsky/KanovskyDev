package dev.kanovsky.portfolioTracker.dto

import java.math.BigDecimal
import java.time.LocalDate

data class HistorisationCryptoPriceDTO(
    val cryptoDTO: CryptoDTO?,
    val timestamp: LocalDate,
    val price: BigDecimal,
    val marketCap: Long
)