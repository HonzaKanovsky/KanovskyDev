package dev.kanovsky.portfolioTracker.Model

import jakarta.persistence.*
import lombok.Data
import java.time.LocalDateTime
import java.math.BigDecimal


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
    val amount: BigDecimal,

    @Column(nullable = false)
    val timestamp: LocalDateTime = LocalDateTime.now()
)
