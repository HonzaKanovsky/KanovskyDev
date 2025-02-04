package dev.kanovsky.portfolioTracker.dto

import org.springframework.data.domain.Page
import java.math.BigDecimal

data class UserDetailDTO(
    val user: UserDTO,
    val portfolioValue: BigDecimal = BigDecimal.ZERO,
    val portfolio: Page<PortfolioEntryDTO> = Page.empty()
) {

}