package dev.kanovsky.portfolioTracker.repository

import dev.kanovsky.portfolioTracker.Model.Crypto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CryptoRepository : JpaRepository<Crypto, Long> {
    fun findBySymbol(symbol: String): Crypto?
    fun findByName(name: String): List<Crypto>
}