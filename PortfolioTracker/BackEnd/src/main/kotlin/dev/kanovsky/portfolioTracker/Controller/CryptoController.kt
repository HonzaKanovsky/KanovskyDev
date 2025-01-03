package dev.kanovsky.portfolioTracker.Controller

import dev.kanovsky.portfolioTracker.Dto.ApiResponse
import dev.kanovsky.portfolioTracker.Dto.CryptoDTO
import dev.kanovsky.portfolioTracker.Model.Crypto
import dev.kanovsky.portfolioTracker.Model.HistorisationCryptoPrice
import dev.kanovsky.portfolioTracker.Service.CryptoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/api/cryptos")
class CryptoController(private val cryptoService: CryptoService) {
    @GetMapping
    fun getAllCryptos(): List<Crypto> = cryptoService.getAllCryptos()

    @GetMapping("/{id}")
    fun getCryptoById(@PathVariable id: Long): Crypto = cryptoService.getCryptoById(id)

    @GetMapping("/{id}/history")
    fun getHistoricalData(
        @PathVariable id: Long,
        @RequestParam startDate: String,
        @RequestParam endDate: String
    ): List<HistorisationCryptoPrice> = cryptoService.getHistoricalData(id, startDate, endDate)

    @PostMapping("/update")
    fun updateCryptoPricesManually(@RequestParam amount: Long): ResponseEntity<ApiResponse<CryptoDTO>> {
        return try {
            val response = cryptoService.updatedCryptoPrices(amount)
            if (response.success) {
                ResponseEntity.ok(response)
            } else {
                ResponseEntity.badRequest().body(response)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(500).body(
                ApiResponse(false, "An unexpected error occurred: ${e.message}")
            )
        }
    }
}