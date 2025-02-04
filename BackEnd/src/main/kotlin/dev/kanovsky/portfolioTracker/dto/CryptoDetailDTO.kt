package dev.kanovsky.portfolioTracker.dto

import java.math.BigDecimal

data class CryptoDetailDTO(
    val crypto: CryptoDTO,
    val historisationCryptoPrices: List<HistorisationCryptoPriceDTO>?
)