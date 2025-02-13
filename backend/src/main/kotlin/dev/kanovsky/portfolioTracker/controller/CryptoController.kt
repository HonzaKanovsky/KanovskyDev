package dev.kanovsky.portfolioTracker.controller

import dev.kanovsky.portfolioTracker.annotations.ApiDescription
import dev.kanovsky.portfolioTracker.dto.CryptoDTO
import dev.kanovsky.portfolioTracker.dto.HistorisationCryptoPriceDTO
import dev.kanovsky.portfolioTracker.service.CryptoService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Controller responsible for handling cryptocurrency-related operations.
 **/
@RestController
@RequestMapping("/api/cryptos")
class CryptoController(private val cryptoService: CryptoService) {

    /**
     * Searches for cryptocurrencies by name or symbol.
     * @param query The search term provided as a request parameter.
     * @return A list of matching cryptocurrencies.
     **/
    @GetMapping("/search")
    @ApiDescription("Searches for cryptocurrencies by name or symbol.")
    fun searchCryptos(@RequestParam query: String): ResponseEntity<List<CryptoDTO>> {
        val results = cryptoService.searchCryptoByNameOrSymbol(query)
        return ResponseEntity.ok(results)
    }

    /**
     * Retrieves a paginated list of all cryptocurrencies along with their latest prices.
     * @param pageable Pagination details, defaults to 30 results sorted by ID.
     * @return A paginated list of cryptocurrency prices.
     **/
    @GetMapping("/list")
    @ApiDescription("Retrieves a paginated list of all cryptocurrencies along with their latest prices.")
    fun getAllCryptosWithPrices(
        @PageableDefault(size = 30, sort = ["id"]) pageable: Pageable
    ): Page<HistorisationCryptoPriceDTO> = cryptoService.getAllCryptosWithPricesToday(pageable)

    /**
     * Retrieves cryptocurrency data by its ID.
     * @param id The cryptocurrency's unique identifier.
     * @return The cryptocurrency details if found, or an error message otherwise.
     **/
    @GetMapping("/{id}")
    @ApiDescription("Retrieves cryptocurrency data by its ID.")
    fun getCryptoById(@PathVariable id: Long): ResponseEntity<Any> {
        val foundCryptoEntry = cryptoService.getCryptoDataById(id)

        return foundCryptoEntry.fold(
            onSuccess = { crypto -> ResponseEntity.ok(crypto) },
            onFailure = { exception ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to exception.message))
            }
        )
    }

    /**
     * Retrieves historical price data for a cryptocurrency.
     * @param id The cryptocurrency's unique identifier.
     * @param startDate Optional start date for filtering historical data.
     * @param endDate Optional end date for filtering historical data.
     * @return The historical price data if found, or an error message otherwise.
     **/
    @GetMapping("/{id}/history")
    @ApiDescription("Retrieves historical price data for a cryptocurrency.")
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

    /**
     * Manually updates cryptocurrency prices in the database.
     * @param amount The number of cryptocurrency entries to update.
     * @return A success message or an error if the update fails.
     **/
    @PostMapping("/update")
    @ApiDescription("Manually updates cryptocurrency prices in the database.")
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