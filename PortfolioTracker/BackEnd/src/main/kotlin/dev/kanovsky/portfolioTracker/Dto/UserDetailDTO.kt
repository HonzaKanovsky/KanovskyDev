package dev.kanovsky.portfolioTracker.Dto

import org.springframework.data.domain.Page

data class UserDetailDTO(
    val user: UserDTO,
    val portfolio: Page<PortfolioEntryDTO> = Page.empty()
) {

}