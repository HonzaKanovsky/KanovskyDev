package dev.kanovsky.portfolioTracker.model

import dev.kanovsky.portfolioTracker.dto.UserDTO
import jakarta.persistence.*
import lombok.Data

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
    fun toDto() = UserDTO(id, username, email)

    companion object {
        fun fromDto() {
            TODO("Not yet implemented")
        }
    }
}
