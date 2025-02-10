package dev.kanovsky.portfolioTracker.controller

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
    ): ResponseEntity<UserDetailDTO> {
        val token = getToken(request) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        return portfolioService.getPortfolioEntriesByUserId(userId, pageable, token)
            .fold(
                onSuccess = { ResponseEntity.ok(it) },
                onFailure = { ResponseEntity.status(HttpStatus.NOT_FOUND).build() }
            )
    }

    @PostMapping
    fun addPortfolioEntry(
        @RequestParam userId: Long,
        @RequestParam cryptoId: Long,
        @RequestParam @Min(0) amount: Double,
        @PageableDefault(size = 30, sort = ["id"]) pageable: Pageable,
        request: HttpServletRequest
    ): ResponseEntity<UserDetailDTO> {
        val token = getToken(request) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        return portfolioService.addPortfolioEntry(userId, cryptoId, amount, pageable, token)
            .fold(
                onSuccess = { ResponseEntity.ok(it) },
                onFailure = { ResponseEntity.status(HttpStatus.BAD_REQUEST).build() }
            )
    }

    @PatchMapping
    fun updatePortfolioEntry(
        @RequestParam userId: Long,
        @RequestParam cryptoId: Long,
        @RequestParam @Min(0) amount: BigDecimal,
        @PageableDefault(size = 30, sort = ["id"]) pageable: Pageable,
        request: HttpServletRequest
    ): ResponseEntity<UserDetailDTO> {
        val token = getToken(request) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        return portfolioService.updatePortfolioEntry(userId, cryptoId, amount, pageable, token)
            .fold(
                onSuccess = { ResponseEntity.ok(it) },
                onFailure = { ResponseEntity.status(HttpStatus.NOT_FOUND).build() }
            )
    }

    @DeleteMapping
    fun deletePortfolioEntry(
        @RequestParam userId: Long,
        @RequestParam cryptoId: Long,
        @PageableDefault(size = 30, sort = ["id"]) pageable: Pageable,
        request: HttpServletRequest
    ): ResponseEntity<UserDetailDTO> {
        val token = getToken(request) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        return portfolioService.removePortfolioEntry(userId, cryptoId, pageable, token)
            .fold(
                onSuccess = { ResponseEntity.ok(it) },
                onFailure = { ResponseEntity.status(HttpStatus.NOT_FOUND).build() }
            )
    }

    private fun getToken(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")?.substring(7)
    }
}
