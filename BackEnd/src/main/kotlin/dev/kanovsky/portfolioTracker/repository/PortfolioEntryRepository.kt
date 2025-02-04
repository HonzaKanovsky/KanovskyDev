package dev.kanovsky.portfolioTracker.repository

import dev.kanovsky.portfolioTracker.model.Crypto
import dev.kanovsky.portfolioTracker.model.PortfolioEntry
import dev.kanovsky.portfolioTracker.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PortfolioEntryRepository : JpaRepository<PortfolioEntry, Long> {
    fun findByUser(user: User, pageable: Pageable): Page<PortfolioEntry>
    fun findByUserAndCrypto(user: User, crypto: Crypto): PortfolioEntry?
}