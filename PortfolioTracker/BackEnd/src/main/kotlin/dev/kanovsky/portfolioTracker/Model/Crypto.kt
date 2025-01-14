package dev.kanovsky.portfolioTracker.Model

import dev.kanovsky.portfolioTracker.Dto.CryptoDTO
import jakarta.persistence.*
import lombok.Data

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
    val name: String
)
 {
    fun toDto() = CryptoDTO(id, symbol, name)

    companion object {
        fun fromDto(dto: CryptoDTO?) = dto?.let {
            Crypto(
                id = it.id,
                symbol = dto.symbol,
                name = dto.name
            )
        }
    }
}
