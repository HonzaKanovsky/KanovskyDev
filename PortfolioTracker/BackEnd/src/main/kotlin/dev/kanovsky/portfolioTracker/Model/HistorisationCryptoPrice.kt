package dev.kanovsky.portfolioTracker.Model

import dev.kanovsky.portfolioTracker.Dto.CryptoDTO
import dev.kanovsky.portfolioTracker.Dto.HistorisationCryptoPriceDTO
import jakarta.persistence.*
import lombok.Data
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Data
@Table(name = "historisation_crypto_price")
data class HistorisationCryptoPrice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crypto_id", nullable = false)
    val crypto: Crypto,
    @Column(nullable = false)
    val timestamp: LocalDate,
    @Column(nullable = false, precision = 18, scale = 8)
    val price: BigDecimal,
    @Column(nullable = false)
    val marketCap: Long
) {
    fun toDto() = HistorisationCryptoPriceDTO(null, timestamp, price, marketCap)

    companion object {
        fun fromDto(dto: CryptoDTO) {
            TODO("Not yet implemented")
        }
    }
}
