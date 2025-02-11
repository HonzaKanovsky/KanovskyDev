package dev.kanovsky.portfolioTracker.model

import dev.kanovsky.portfolioTracker.dto.CryptoDTO
import jakarta.persistence.*

/**
 * Entity representing a cryptocurrency.
 **/
@Entity
@Table(name = "crypto")
data class Crypto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true, length = 50)
    val symbol: String,

    @Column(nullable = false)
    val name: String
) {
    /**
     * Converts this Crypto entity to a CryptoDTO.
     **/
    fun toDto() = CryptoDTO(id, symbol, name)
}
