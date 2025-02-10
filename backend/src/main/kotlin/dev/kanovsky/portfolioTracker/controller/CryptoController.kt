package dev.kanovsky.portfolioTracker.controller

import dev.kanovsky.portfolioTracker.dto.CryptoDTO
import dev.kanovsky.portfolioTracker.dto.HistorisationCryptoPriceDTO
import dev.kanovsky.portfolioTracker.service.CryptoService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cryptos")
class CryptoController(private val cryptoService: CryptoService) {

    @GetMapping("/search")
    fun searchCryptos(@RequestParam query: String): ResponseEntity<List<CryptoDTO>> {
        val results = cryptoService.searchCryptoByNameOrSymbol(query)
        return ResponseEntity.ok(results)
    }

    @GetMapping("/list")
    fun getAllCryptosWithPrices(
        @PageableDefault(size = 30, sort = ["id"]) pageable: Pageable
    ): Page<HistorisationCryptoPriceDTO> = cryptoService.getAllCryptosWithPricesToday(pageable)

    @GetMapping("/{id}")
    fun getCryptoById(@PathVariable id: Long): ResponseEntity<Any> {
        val foundCryptoEntry = cryptoService.getCryptoDataById(id)

        return foundCryptoEntry.fold(
            onSuccess = { crypto -> ResponseEntity.ok(crypto) },
            onFailure = { exception ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to exception.message))
            }
        )
    }

    @GetMapping("/{id}/history")
    fun getHistoricalData(
        @PathVariable id: Long = 0,
        @RequestParam(required = false) startDate: String?,
        @RequestParam(required = false) endDate: String?
    ): ResponseEntity<Any> {
        val cryptoData = cryptoService.getHistoricalData(id, startDate, endDate)

        return cryptoData.fold(
            onSuccess = { crypto -> ResponseEntity.ok(crypto) },
            onFailure = { exception ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to exception.message))
            }
        )
    }

    @PostMapping("/update")
    fun updateCryptoPricesManually(@RequestParam amount: Long): ResponseEntity<Any> {
        val updateResult = cryptoService.updateDBCryptoEntries(amount)

        return updateResult.fold(
            onSuccess = { crypto -> ResponseEntity.ok(crypto) },
            onFailure = { exception ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to exception.message))
            }
        )
    }
}