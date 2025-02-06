package dev.kanovsky.portfolioTracker.model

import dev.kanovsky.portfolioTracker.dto.HistorisationCryptoPriceDTO
import dev.kanovsky.portfolioTracker.enums.PriceChangeStatus
import jakarta.persistence.*
import lombok.Data
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Data
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
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var priceChangeStatus: PriceChangeStatus = PriceChangeStatus.STABLE

) {

    fun determinePriceChange(previousPrice: BigDecimal?) {
        priceChangeStatus = when {
            previousPrice == null || previousPrice == price -> PriceChangeStatus.STABLE
            price > previousPrice -> PriceChangeStatus.INCREASE
            else -> PriceChangeStatus.DECREASE
        }
    }

    fun toDto(containCryptoDto: Boolean = false): HistorisationCryptoPriceDTO {
        return (if (containCryptoDto) {
            HistorisationCryptoPriceDTO(crypto.toDto(), timestamp, price, marketCap, priceChangeStatus)
        } else {
            HistorisationCryptoPriceDTO(null, timestamp, price, marketCap, priceChangeStatus)
        })
    }

    companion object {
        fun fromDto() {
            TODO("Not yet implemented")
        }
    }
}
