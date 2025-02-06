package dev.kanovsky.portfolioTracker.dto

import dev.kanovsky.portfolioTracker.enums.PriceChangeStatus
import java.math.BigDecimal
import java.time.LocalDate

data class HistorisationCryptoPriceDTO(
    val cryptoDTO: CryptoDTO?,
    val timestamp: LocalDate,
    val price: BigDecimal,
    val marketCap: Long,
    val priceChangeStatus: PriceChangeStatus = PriceChangeStatus.STABLE
)