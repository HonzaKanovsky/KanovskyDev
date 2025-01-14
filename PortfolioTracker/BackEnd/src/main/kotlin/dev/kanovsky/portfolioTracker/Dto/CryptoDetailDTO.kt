package dev.kanovsky.portfolioTracker.Dto

data class CryptoDetailDTO(
    val crypto: CryptoDTO,
    val historisationCryptoPrices: List<HistorisationCryptoPriceDTO>
)