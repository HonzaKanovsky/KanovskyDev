package dev.kanovsky.portfolioTracker.controller

import dev.kanovsky.portfolioTracker.dto.ApiResponse
import dev.kanovsky.portfolioTracker.dto.UserDetailDTO
import dev.kanovsky.portfolioTracker.service.PortfolioEntryService
import jakarta.validation.constraints.Min
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/api/portfolios")
class PortfolioEntryController(private val portfolioEntryService: PortfolioEntryService) {

    @GetMapping("/{userId}")
    fun getPortfolioByUserId(
        @PathVariable userId: Long,
        @PageableDefault(size = 30, sort = ["id"]) pageable: Pageable
    ): ResponseEntity<ApiResponse<UserDetailDTO>> {
        val result = portfolioEntryService.getPortfolioEntriesByUserId(userId, pageable)
        return if (result.success) {
            ResponseEntity.status(HttpStatus.OK).body(result)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(result)
        }
    }

    @PostMapping
    fun addPortfolioEntry(
        @RequestParam userId: Long,
        @RequestParam cryptoId: Long,
        @RequestParam @Min(0) amount: Double,
        @PageableDefault(size = 30, sort = ["id"]) pageable: Pageable
    ): ResponseEntity<ApiResponse<UserDetailDTO>> {
        val result = portfolioEntryService.addPortfolioEntry(userId, cryptoId, amount, pageable)
        return if (result.success) {
            ResponseEntity.status(HttpStatus.OK).body(result)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(result)
        }
    }

    @PatchMapping
    fun updatePortfolioEntry(
        @RequestParam userId: Long,
        @RequestParam cryptoId: Long,
        @RequestParam @Min(0) amount: BigDecimal,
        @PageableDefault(size = 30, sort = ["id"]) pageable: Pageable
    ): ResponseEntity<ApiResponse<UserDetailDTO>> {
        val result = portfolioEntryService.updatePortfolioEntry(userId, cryptoId, amount, pageable)
        return if (result.success) {
            ResponseEntity.status(HttpStatus.OK).body(result)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(result)
        }
    }


    @DeleteMapping
    fun deletePortfolioEntry(
        @RequestParam userId: Long,
        @RequestParam cryptoId: Long,
        @PageableDefault(size = 30, sort = ["id"]) pageable: Pageable
    ): ResponseEntity<ApiResponse<UserDetailDTO>> {
        val result = portfolioEntryService.removePortfolioEntry(userId, cryptoId, pageable)
        return if (result.success) {
            ResponseEntity.status(HttpStatus.OK).body(result)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(result)
        }
    }

}