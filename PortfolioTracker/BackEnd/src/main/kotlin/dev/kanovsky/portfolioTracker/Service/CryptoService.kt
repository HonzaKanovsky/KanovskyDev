package dev.kanovsky.portfolioTracker.Service

import dev.kanovsky.portfolioTracker.Model.Crypto
import dev.kanovsky.portfolioTracker.Model.HistorisationCryptoPrice
import dev.kanovsky.portfolioTracker.Repository.CryptoRepository
import dev.kanovsky.portfolioTracker.Repository.HistorisationCryptoPriceRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CryptoService(
    private val cryptoRepository: CryptoRepository,
    private val historisationCryptoPriceRepository: HistorisationCryptoPriceRepository
) {

    fun getAllCryptos(): List<Crypto> = cryptoRepository.findAll()

    fun getCryptoById(id: Long): Crypto =
        cryptoRepository.findById(id).orElseThrow { IllegalArgumentException("Crypto with id $id not found") }

    fun getHistoricalData(cryptoId: Long, startDate: String, endDate: String): List<HistorisationCryptoPrice> {
        val crypto = getCryptoById(cryptoId)
        val start = LocalDate.parse(startDate)
        val end = LocalDate.parse(endDate)
        return historisationCryptoPriceRepository.findByCryptoAndTimestampBetween(crypto, start.atStartOfDay(), end.atStartOfDay())
    }
}