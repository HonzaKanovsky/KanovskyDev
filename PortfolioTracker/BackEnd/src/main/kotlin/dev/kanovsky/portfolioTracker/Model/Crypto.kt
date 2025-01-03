package dev.kanovsky.portfolioTracker.Model

import dev.kanovsky.portfolioTracker.Dto.CryptoDTO
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

    @Column(nullable = false, unique = true, length = 50)
    val symbol: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, precision = 18, scale = 8)
    var price: BigDecimal,

    @Column(name = "market_cap")
    val marketCap: Long?
) {
    fun toDto() = CryptoDTO(id, symbol, name, price, marketCap)

    companion object {
        fun fromDto(dto: CryptoDTO) = Crypto(
            id = dto.id,
            symbol = dto.symbol,
            name = dto.name,
            price = dto.price,
            marketCap = dto.marketCap
        )
    }
}
