package dev.kanovsky.portfolioTracker.repository

import dev.kanovsky.portfolioTracker.model.Crypto
import dev.kanovsky.portfolioTracker.model.HistorisationCryptoPrice
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface HistorisationCryptoPriceRepository : JpaRepository<HistorisationCryptoPrice, Long> {
    fun findByCrypto(crypto: Crypto): List<HistorisationCryptoPrice>
    fun findByCryptoAndTimestamp(crypto: Crypto, timestamp: LocalDate): HistorisationCryptoPrice?

    fun findFirstByCryptoOrderByTimestampDesc(crypto: Crypto): HistorisationCryptoPrice?
    fun findByCryptoAndTimestampBetweenOrderByTimestampDesc(
        crypto: Crypto,
        start: LocalDate,
        end: LocalDate
    ): List<HistorisationCryptoPrice>

    fun findHistorisationCryptoPricesByTimestamp(
        pageable: Pageable,
        timestamp: LocalDate
    ): Page<HistorisationCryptoPrice>

    fun findAllByTimestamp(date: LocalDate): List<HistorisationCryptoPrice>

    @Query("SELECT MAX(h.timestamp) FROM HistorisationCryptoPrice h WHERE h.timestamp < :currentDate")
    fun findLatestTimestampBefore(currentDate: LocalDate): LocalDate?
}