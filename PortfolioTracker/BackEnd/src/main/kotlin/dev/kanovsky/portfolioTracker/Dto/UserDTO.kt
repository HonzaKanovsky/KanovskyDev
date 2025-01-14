package dev.kanovsky.portfolioTracker.Dto

import org.springframework.data.domain.Page

data class UserDTO(
    val id: Long,
    val username: String,
    val email: String,
    val portfolio: Page<PortfolioEntryDTO>?
)