package dev.kanovsky.portfolioTracker.repository

import dev.kanovsky.portfolioTracker.model.Crypto
import dev.kanovsky.portfolioTracker.model.PortfolioEntry
import dev.kanovsky.portfolioTracker.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository for managing user portfolio entries.
 **/
@Repository
interface PortfolioEntryRepository : JpaRepository<PortfolioEntry, Long> {
    /**
     * Retrieves a paginated list of portfolio entries for a given user.
     * @param user The user whose portfolio entries are being retrieved.
     * @param pageable Pagination details.
     * @return A paginated list of PortfolioEntry entities.
     **/
    fun findByUser(user: User, pageable: Pageable): Page<PortfolioEntry>

    /**
     * Finds a specific cryptocurrency entry within a user's portfolio.
     * @param user The user who owns the portfolio.
     * @param crypto The cryptocurrency being searched for.
     * @return The PortfolioEntry if found, or `null` if the user does not hold this cryptocurrency.
     **/
    fun findByUserAndCrypto(user: User, crypto: Crypto): PortfolioEntry?
}