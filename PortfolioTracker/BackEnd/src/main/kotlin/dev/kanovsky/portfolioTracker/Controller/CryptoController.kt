package dev.kanovsky.portfolioTracker.Controller

import dev.kanovsky.portfolioTracker.Dto.ApiResponse
import dev.kanovsky.portfolioTracker.Dto.CryptoDTO
import dev.kanovsky.portfolioTracker.Dto.CryptoDetailDto
import dev.kanovsky.portfolioTracker.Dto.HistorisationCryptoPriceDTO
import dev.kanovsky.portfolioTracker.Model.Crypto
import dev.kanovsky.portfolioTracker.Service.CryptoService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cryptos")
class CryptoController(private val cryptoService: CryptoService) {
    @GetMapping
    fun getAllCryptos(
        @PageableDefault(size = 30, sort = ["id"]) pageable: Pageable
    ): Page<Crypto> = cryptoService.getAllCryptos(pageable)

    @GetMapping("/all/list")
    fun getAllCryptosWithPrices(
        @PageableDefault(size = 30, sort = ["id"]) pageable: Pageable
    ): Page<HistorisationCryptoPriceDTO> = cryptoService.getAllCryptosWithPricesToday(pageable)

    @GetMapping("/{id}")
    fun getCryptoById(@PathVariable id: Long): ResponseEntity<ApiResponse<CryptoDTO>> {
        return try {
            val foundCryptoEntry = cryptoService.getCryptoDataById(id)
            ResponseEntity.ok(foundCryptoEntry)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse(
                    success = false,
                    message = "An unexpected error occured while fetching crypto data : ${e.message}"
                )
            )
        }
    }

    @GetMapping("/{id}/history")
    fun getHistoricalData(
        @PathVariable id: Long = 0,
        @RequestParam(required = false) startDate: String?,
        @RequestParam(required = false) endDate: String?
    ): ApiResponse<CryptoDetailDto> = cryptoService.getHistoricalData(id, startDate, endDate)


    @PostMapping("/update")
    fun updateCryptoPricesManually(@RequestParam amount: Long): ResponseEntity<ApiResponse<CryptoDTO>> {
        val response = cryptoService.updateDBCryptoEntries(amount)
        return if (response.success) {
            ResponseEntity.ok(response)
        } else {
            ResponseEntity.badRequest().body(response)
        }
    }
}