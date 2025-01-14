package dev.kanovsky.portfolioTracker.Dto

class CryptoDetailDTO(
    val cryptoDTO: CryptoDTO,
    val historisationCryptoPriceDTOs: List<HistorisationCryptoPriceDTO>
)