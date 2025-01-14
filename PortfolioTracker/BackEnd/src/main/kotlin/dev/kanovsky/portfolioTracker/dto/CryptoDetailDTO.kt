package dev.kanovsky.portfolioTracker.dto

data class CryptoDetailDTO(
    val crypto: CryptoDTO,
    val historisationCryptoPrices: List<HistorisationCryptoPriceDTO>
)