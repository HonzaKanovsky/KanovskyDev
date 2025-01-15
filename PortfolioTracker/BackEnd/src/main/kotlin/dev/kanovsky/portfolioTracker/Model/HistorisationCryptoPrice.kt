package dev.kanovsky.portfolioTracker.model

import dev.kanovsky.portfolioTracker.dto.HistorisationCryptoPriceDTO
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
    val price: BigDecimal,
    @Column(nullable = false)
    val marketCap: Long
) {
    fun toDto(containCryptoDto: Boolean = false): HistorisationCryptoPriceDTO {
        return (if (containCryptoDto) {
            HistorisationCryptoPriceDTO(crypto.toDto(), timestamp, price, marketCap)
        } else {
            HistorisationCryptoPriceDTO(null, timestamp, price, marketCap)
        })
    }

    companion object {
        fun fromDto() {
            TODO("Not yet implemented")
        }
    }
}
