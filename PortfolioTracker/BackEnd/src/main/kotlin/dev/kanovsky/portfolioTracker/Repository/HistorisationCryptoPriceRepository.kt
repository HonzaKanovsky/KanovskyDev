package dev.kanovsky.portfolioTracker.Repository

import dev.kanovsky.portfolioTracker.Model.Crypto
import dev.kanovsky.portfolioTracker.Model.HistorisationCryptoPrice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface HistorisationCryptoPriceRepository : JpaRepository<HistorisationCryptoPrice, Long> {
    fun findByCrypto(crypto: Crypto): List<HistorisationCryptoPrice>
    fun findByCryptoAndTimestampBetween(crypto: Crypto, start: LocalDateTime, end: LocalDateTime): List<HistorisationCryptoPrice>
}