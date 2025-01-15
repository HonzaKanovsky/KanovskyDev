package dev.kanovsky.portfolioTracker.repository

import dev.kanovsky.portfolioTracker.model.Crypto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CryptoRepository : JpaRepository<Crypto, Long> {
    fun findCryptoById(id: Long): Crypto?
    fun findBySymbol(symbol: String): Crypto?
    fun findByName(name: String): List<Crypto>
}