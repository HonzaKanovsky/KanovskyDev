package dev.kanovsky.portfolioTracker.Repository

import dev.kanovsky.portfolioTracker.Model.Crypto
import dev.kanovsky.portfolioTracker.Model.HistorisationCryptoPrice
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface HistorisationCryptoPriceRepository : JpaRepository<HistorisationCryptoPrice, Long> {
    fun findByCrypto(crypto: Crypto): List<HistorisationCryptoPrice>
    fun findByCryptoAndTimestamp(crypto: Crypto, timestamp: LocalDate): HistorisationCryptoPrice?
    fun findByCryptoAndTimestampBetween(
        crypto: Crypto,
        start: LocalDate,
        end: LocalDate
    ): List<HistorisationCryptoPrice>

    fun findHistorisationCryptoPricesByTimestamp(
        pageable: Pageable,
        timestamp: LocalDate
    ): Page<HistorisationCryptoPrice>
}