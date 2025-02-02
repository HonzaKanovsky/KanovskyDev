package dev.kanovsky.portfolioTracker.model

import dev.kanovsky.portfolioTracker.dto.PortfolioEntryDTO
import jakarta.persistence.*
import lombok.Data
import java.math.BigDecimal
import java.time.LocalDateTime


@Entity
@Data
@Table(name = "portfolio_entry")
data class PortfolioEntry(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", nullable = false)
    private val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cryptoID", nullable = false)
    val crypto: Crypto,

    @Column(nullable = false, precision = 18, scale = 4)
    var amount: BigDecimal,

    @Column(nullable = false)
    var timestamp: LocalDateTime = LocalDateTime.now()
) {
    fun toDto() = PortfolioEntryDTO(
        id = id,
        crypto = crypto.toDto(),
        amount = amount
    )

    companion object {
        fun fromDto() {
            TODO("Not yet implemented")
        }
    }
}
