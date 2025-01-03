package dev.kanovsky.portfolioTracker.Repository

import dev.kanovsky.portfolioTracker.Model.Crypto
import dev.kanovsky.portfolioTracker.Model.PortfolioEntry
import dev.kanovsky.portfolioTracker.Model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PortfolioEntryRepository : JpaRepository<PortfolioEntry, Long> {
    fun findByUser(user: User): List<PortfolioEntry>
    fun findByUserAndCrypto(user: User, crypto: Crypto): PortfolioEntry?
}