package dev.kanovsky.portfolioTracker.Dto

class CryptoDetailDto(
    val cryptoDTO: CryptoDTO,
    val historisationCryptoPriceDTOs: List<HistorisationCryptoPriceDTO>
)