package dev.kanovsky.portfolioTracker.Model

import dev.kanovsky.portfolioTracker.Dto.CryptoDTO
import dev.kanovsky.portfolioTracker.Dto.PortfolioEntryDTO
import dev.kanovsky.portfolioTracker.Dto.UserDTO
import jakarta.persistence.*
import lombok.Data
import org.springframework.data.domain.Page

@Entity
@Data
@Table(name = "user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false, unique = true)
    val email: String
) {
    fun toDto(portfolioEntryDTO: Page<PortfolioEntryDTO> = Page.empty()) =
        UserDTO(id, username, email, portfolio = portfolioEntryDTO)

    companion object {
        fun fromDto(dto: CryptoDTO) {
            TODO("Not yet implemented")
        }
    }
}
