package dev.kanovsky.portfolioTracker.service

import dev.kanovsky.portfolioTracker.Model.Crypto
import dev.kanovsky.portfolioTracker.Model.PortfolioEntry
import dev.kanovsky.portfolioTracker.Model.User
import dev.kanovsky.portfolioTracker.repository.CryptoRepository
import dev.kanovsky.portfolioTracker.repository.PortfolioEntryRepository
import dev.kanovsky.portfolioTracker.repository.UserRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class PortfolioEntryService(
    private val portfolioEntryRepository: PortfolioEntryRepository,
    private val userRepository: UserRepository,
    private val cryptoRepository: CryptoRepository
) {

    fun getPortfolioEntriesByUserId(userId: Long): List<PortfolioEntry> {
        val user = getUserById(userId)
        return portfolioEntryRepository.findByUser(user)
    }

    fun addPortfolioEntry(userId: Long, cryptoId: Long, amount: Double): PortfolioEntry {
        val user = getUserById(userId)
        val crypto = getCryptoById(cryptoId)
        val portfolioEntry = PortfolioEntry(
            user = user,
            crypto = crypto,
            amount = BigDecimal.valueOf(amount)
        )
        return portfolioEntryRepository.save(portfolioEntry)
    }

    fun deletePortfolioEntry(id: Long) {
        if (!portfolioEntryRepository.existsById(id)) {
            throw IllegalArgumentException("Portfolio entry with id $id not found")
        }
        portfolioEntryRepository.deleteById(id)
    }

    private fun getUserById(userId: Long): User =
        userRepository.findById(userId).orElseThrow { IllegalArgumentException("User with id $userId not found") }

    private fun getCryptoById(cryptoId: Long): Crypto =
        cryptoRepository.findById(cryptoId)
            .orElseThrow { IllegalArgumentException("Crypto with id $cryptoId not found") }
}