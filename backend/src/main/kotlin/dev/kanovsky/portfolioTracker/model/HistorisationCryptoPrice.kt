package dev.kanovsky.portfolioTracker.model

import dev.kanovsky.portfolioTracker.dto.HistorisationCryptoPriceDTO
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Entity representing historical cryptocurrency prices.
 */
@Entity
@Table(name = "historisation_crypto_price")
data class HistorisationCryptoPrice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "crypto_id", nullable = false)
    val crypto: Crypto,

    @Column(nullable = false)
    val timestamp: LocalDate,

    @Column(nullable = false, precision = 18, scale = 8)
    var price: BigDecimal,

    @Column(nullable = false)
    val marketCap: Long,

    @Column(nullable = false, precision = 10, scale = 2)
    var priceChangePercentage: BigDecimal = BigDecimal.ZERO

) {

    /**
     * Calculates the price change percentage based on the previous price.
     * @param previousPrice The previous recorded price for comparison.
     **/
    fun calculatePriceChange(previousPrice: BigDecimal?) {
        priceChangePercentage = if (previousPrice == null || previousPrice.compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal.ZERO
        } else {
            ((price - previousPrice) / previousPrice * BigDecimal(100))
        }
    }

    /**
     * Converts this entity to a DTO.
     * @param containCryptoDto If `true`, includes crypto details in the DTO.
     * @return A DTO representation of this entity.
     **/
    fun toDto(containCryptoDto: Boolean = false): HistorisationCryptoPriceDTO {
        return (if (containCryptoDto) {
            HistorisationCryptoPriceDTO(crypto.toDto(), timestamp, price, marketCap, priceChangePercentage)
        } else {
            HistorisationCryptoPriceDTO(null, timestamp, price, marketCap, priceChangePercentage)
        })
    }
}
