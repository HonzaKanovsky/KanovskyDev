package dev.kanovsky.portfolioTracker.controller

import dev.kanovsky.portfolioTracker.dto.ApiResponse
import dev.kanovsky.portfolioTracker.dto.UserDetailDTO
import dev.kanovsky.portfolioTracker.service.PortfolioService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.constraints.Min
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/api/portfolios")
class PortfolioController(private val portfolioService: PortfolioService) {

    @GetMapping("/{userId}")
    fun getPortfolioByUserId(
        @PathVariable userId: Long,
        @PageableDefault(size = 30, sort = ["id"]) pageable: Pageable,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<UserDetailDTO>> {
        val token = getToken(request) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        val result = portfolioService.getPortfolioEntriesByUserId(userId, pageable, token)
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
        @PageableDefault(size = 30, sort = ["id"]) pageable: Pageable,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<UserDetailDTO>> {
        val token = getToken(request) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        val result = portfolioService.addPortfolioEntry(userId, cryptoId, amount, pageable, token)
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
        @PageableDefault(size = 30, sort = ["id"]) pageable: Pageable,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<UserDetailDTO>> {
        val token = getToken(request) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        val result = portfolioService.updatePortfolioEntry(userId, cryptoId, amount, pageable, token)

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
        @PageableDefault(size = 30, sort = ["id"]) pageable: Pageable,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<UserDetailDTO>> {
        val token = getToken(request) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        val result = portfolioService.removePortfolioEntry(userId, cryptoId, pageable, token)
        return if (result.success) {
            ResponseEntity.status(HttpStatus.OK).body(result)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(result)
        }
    }


    private fun getToken(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")?.substring(7)
    }

}