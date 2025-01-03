package dev.kanovsky.portfolioTracker.Model

import jakarta.persistence.*
import lombok.Data
import java.math.BigDecimal

@Entity
@Data
@Table(name = "crypto")
data class Crypto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true, length = 10)
    val symbol: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, precision = 18, scale = 8)
    val price: BigDecimal,

    @Column
    val marketCap: Long?
)
