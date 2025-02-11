package dev.kanovsky.portfolioTracker.repository

import dev.kanovsky.portfolioTracker.model.Crypto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository interface for accessing cryptocurrency data.
 **/
@Repository
interface CryptoRepository : JpaRepository<Crypto, Long> {
    /**
     * Finds a cryptocurrency by its ID.
     * @param id The unique identifier of the cryptocurrency.
     * @return The found Crypto entity or `null` if not found.
     **/
    fun findCryptoById(id: Long): Crypto?

    /**
     * Searches for cryptocurrencies by name or symbol, ignoring case.
     * @param name The partial/full name of the cryptocurrency.
     * @param symbol The partial/full symbol of the cryptocurrency.
     * @return A list of matching Crypto entities.
     **/
    fun findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(name: String, symbol: String): List<Crypto>
}