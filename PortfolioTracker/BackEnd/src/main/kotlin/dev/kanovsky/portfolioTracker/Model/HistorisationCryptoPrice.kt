package dev.kanovsky.portfolioTracker.Model

import dev.kanovsky.portfolioTracker.Dto.CryptoDTO
import dev.kanovsky.portfolioTracker.Dto.HistorisationCryptoPriceDTO
import jakarta.persistence.*
import lombok.Data
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Data
@Table(name = "historisation_crypto_price")
data class HistorisationCryptoPrice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cryptoID", nullable = false)
    val crypto: Crypto,

    @Column(nullable = false)
    val timestamp: LocalDateTime,

    @Column(nullable = false, precision = 18, scale = 4)
    val price: BigDecimal
) {
    fun toDto() = HistorisationCryptoPriceDTO(timestamp, price)

    companion object {
        fun fromDto(dto: CryptoDTO) {
            TODO("NOT IMPLEMENTED")
        }
    }
}
