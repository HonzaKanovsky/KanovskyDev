package dev.kanovsky.portfolioTracker.model

import dev.kanovsky.portfolioTracker.dto.PortfolioEntryDTO
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Entity representing a user's portfolio entry.
 */
@Entity
@Table(name = "portfolio_entry")
data class PortfolioEntry(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", nullable = false)
    val user: User, // Removed `private` modifier

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cryptoID", nullable = false)
    val crypto: Crypto,

    @Column(nullable = false, precision = 18, scale = 4)
    var amount: BigDecimal,

    @Column(nullable = false)
    var timestamp: LocalDateTime = LocalDateTime.now()
) {
    /**
     * Converts this PortfolioEntry entity to a DTO.
     * @return A DTO representation of this PortfolioEntry.
     */
    fun toDto() = PortfolioEntryDTO(
        crypto = crypto.toDto(),
        amount = amount
    )
}
