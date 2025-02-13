package dev.kanovsky.portfolioTracker.controller

import dev.kanovsky.portfolioTracker.annotations.ApiDescription
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

/**
 * Controller responsible for handling user portfolio operations.
 **/
@RestController
@RequestMapping("/api/portfolios")
class PortfolioController(private val portfolioService: PortfolioService) {

    /**
     * Retrieves a user's portfolio by their user ID.
     * @param userId The user's unique identifier.
     * @param pageable Pagination details, defaults to 30 results sorted by ID.
     * @param request The HTTP request to extract the authorization token.
     * @return A ResponseEntity containing the user's portfolio or an error response.
     **/
    @GetMapping("/{userId}")
    @ApiDescription("Retrieves a user's portfolio by their user ID.")
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

    /**
     * Adds a new cryptocurrency entry to the user's portfolio.
     * @param userId The user's unique identifier.
     * @param cryptoId The cryptocurrency's unique identifier.
     * @param amount The amount of cryptocurrency to add (must be 0 or greater).
     * @param pageable Pagination details.
     * @param request The HTTP request to extract the authorization token.
     * @return A ResponseEntity containing the updated portfolio or an error response.
     **/
    @PostMapping
    @ApiDescription("Adds a new cryptocurrency entry to the user's portfolio.")
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

    /**
     * Updates an existing cryptocurrency entry in the user's portfolio.
     * @param userId The user's unique identifier.
     * @param cryptoId The cryptocurrency's unique identifier.
     * @param amount The new amount of cryptocurrency (must be 0 or greater).
     * @param pageable Pagination details.
     * @param request The HTTP request to extract the authorization token.
     * @return A ResponseEntity containing the updated portfolio or an error response.
     **/
    @PatchMapping
    @ApiDescription("Updates an existing cryptocurrency entry in the user's portfolio.")
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

    /**
     * Deletes a cryptocurrency entry from the user's portfolio.
     * @param userId The user's unique identifier.
     * @param cryptoId The cryptocurrency's unique identifier.
     * @param pageable Pagination details.
     * @param request The HTTP request to extract the authorization token.
     * @return A ResponseEntity containing the updated portfolio or an error response.
     **/
    @DeleteMapping
    @ApiDescription("Deletes a cryptocurrency entry from the user's portfolio.")
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

    /**
     * Extracts the JWT token from the Authorization header.
     * @param request The HTTP request containing the Authorization header.
     * @return The extracted token, or null if missing.
     **/
    private fun getToken(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")?.substring(7)
    }
}
