package dev.kanovsky.portfolioTracker.service

import dev.kanovsky.portfolioTracker.dto.CryptoDTO
import dev.kanovsky.portfolioTracker.dto.CryptoDetailDTO
import dev.kanovsky.portfolioTracker.dto.HistorisationCryptoPriceDTO
import dev.kanovsky.portfolioTracker.enums.CurrencyCode
import dev.kanovsky.portfolioTracker.model.Crypto
import dev.kanovsky.portfolioTracker.model.HistorisationCryptoPrice
import dev.kanovsky.portfolioTracker.repository.CryptoRepository
import dev.kanovsky.portfolioTracker.repository.HistorisationCryptoPriceRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate

/**
 * Service responsible for handling cryptocurrency data operations.
 **/
@Service
class CryptoService(
    private val cryptoRepository: CryptoRepository,
    private val historisationCryptoPriceRepository: HistorisationCryptoPriceRepository,
) {

    /**
     * Retrieves all cryptocurrencies with pagination.
     **/
    fun getAllCryptos(pageable: Pageable): Page<Crypto> = cryptoRepository.findAll(pageable)

    /**
     * Retrieves all cryptocurrencies with their latest prices.
     **/
    fun getAllCryptosWithPricesToday(pageable: Pageable): Page<HistorisationCryptoPriceDTO> {
        return try {
            val pricesPage =
                historisationCryptoPriceRepository.findHistorisationCryptoPricesByTimestamp(pageable, LocalDate.now());
            return pricesPage.map { it.toDto(containCryptoDto = true) }
        } catch (e: Exception) {
            Page.empty(pageable)
        }
    }

    /**
     * Retrieves cryptocurrency data by ID.
     **/
    fun getCryptoDataById(id: Long): Result<CryptoDTO> {
        return try {
            Result.success(getCryptoById(id).toDto())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Retrieves historical price data for a given cryptocurrency.
     **/
    fun getHistoricalData(cryptoId: Long, startDate: String?, endDate: String?): Result<CryptoDetailDTO> {

        return try {
            val crypto = getCryptoById(cryptoId)
            val start = LocalDate.parse(startDate ?: LocalDate.now().minusMonths(12).toString())
            val end = LocalDate.parse(endDate ?: LocalDate.now().toString())
            val cryptosInInterval =
                historisationCryptoPriceRepository.findByCryptoAndTimestampBetweenOrderByTimestampDesc(
                    crypto,
                    start,
                    end
                )

            val cryptosInIntervalDto = mutableListOf<HistorisationCryptoPriceDTO>()
            for (cryptoEntry in cryptosInInterval) {
                cryptosInIntervalDto.add(cryptoEntry.toDto())
            }

            val cryptoDetailDto =
                CryptoDetailDTO(crypto = crypto.toDto(), historisationCryptoPrices = cryptosInIntervalDto)
            return Result.success(cryptoDetailDto)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    /**
     * Fetches the latest cryptocurrency prices from CoinMarketCap and updates the database.
     **/
    fun updateDBCryptoEntries(amount: Long, currency: CurrencyCode = CurrencyCode.USD): Result<String> {
        return (
                try {
                    val apiKey = System.getenv("CMC_API_KEY") ?: throw IllegalStateException("API key not found")
                    val baseUrl = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"
                    val url = "$baseUrl?start=1&limit=$amount&convert=${currency.code}"

                    val response = fetchDataFromCMCApi(apiKey, url)
                    val responseData = response.body?.string()

                    if (response.isSuccessful) {
                        val jsonResponse = JSONObject(responseData)
                        val data = jsonResponse.getJSONArray("data")

                        saveNewHistorisationCryptoPrices(data)

                        Result.success("Updating crypto prices was successful")
                    } else {
                        Result.failure(Exception("Fetching data from CMC was unsuccessful: ${response.message}"))
                    }
                } catch (e: Exception) {
                    Result.failure(e)
                })
    }

    /**
     * Searches for cryptocurrencies by name or symbol. (used for search in UI)
     **/
    fun searchCryptoByNameOrSymbol(query: String): List<CryptoDTO> {
        return cryptoRepository.findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(query, query)
            .map { it.toDto() }
    }

    /**
     * Retrieves a cryptocurrency entity by its ID.
     **/
    private fun getCryptoById(id: Long): Crypto =
        cryptoRepository.findById(id).orElseThrow { IllegalArgumentException("Crypto with id $id not found") }

    /**
     * Saves or updates historical cryptocurrency prices in the database.
     **/
    private fun saveNewHistorisationCryptoPrices(data: JSONArray) {
        //Get map of cryptos saved in database group them by name and symbol
        val existingCryptos = cryptoRepository.findAll()
        val existingCryptoMap = existingCryptos.associateBy { it.name to it.symbol }

        //Get map of cryptos saved in database group them by crypto
        val existingRecordsToday = historisationCryptoPriceRepository.findAllByTimestamp(LocalDate.now())
        val existingRecordMap = existingRecordsToday.associateBy { it.crypto }

        val currentDate = LocalDate.now()
        val latestTimestamp = historisationCryptoPriceRepository.findLatestTimestampBefore(currentDate)

        // Get all records from that timestamp
        val previousRecords = latestTimestamp?.let {
            historisationCryptoPriceRepository.findAllByTimestamp(it)
        } ?: emptyList()

        val previousRecordMap = previousRecords.associateBy { it.crypto }

        val newHistorisationCryptoPrices = mutableListOf<HistorisationCryptoPrice>()


        for (i in 0 until data.length()) {
            val cryptoEntryJSON = data.getJSONObject(i)
            val name = cryptoEntryJSON.getString("name")
            val symbol = cryptoEntryJSON.getString("symbol")

            val quote = cryptoEntryJSON.getJSONObject("quote").getJSONObject("USD")
            val price = quote.getBigDecimal("price")
            val marketCap = quote.getLong("market_cap")

            val existingCrypto = existingCryptoMap[name to symbol]
            val cryptoToBeUpdated = existingCrypto ?: cryptoRepository.save(Crypto(name = name, symbol = symbol))


            val previousPrice = previousRecordMap[existingCrypto]?.price

            val existingRecord = if (existingRecordMap[cryptoToBeUpdated] != null) {
                existingRecordMap[cryptoToBeUpdated]?.price = price
                existingRecordMap[cryptoToBeUpdated]!!
            } else {
                HistorisationCryptoPrice(
                    crypto = cryptoToBeUpdated,
                    timestamp = currentDate,
                    price = price,
                    marketCap = marketCap
                )
            }

            existingRecord.calculatePriceChange(previousPrice)
            newHistorisationCryptoPrices.add(
                existingRecord
            )
        }
        historisationCryptoPriceRepository.saveAll(newHistorisationCryptoPrices)
    }

    /**
     * Fetches cryptocurrency data from CoinMarketCap API.
     **/
    private fun fetchDataFromCMCApi(apiKey: String, url: String): Response {
        // Set up OkHttp client
        val client = OkHttpClient.Builder()
            .build()

        // Create the request
        val request = Request.Builder()
            .url(url)
            .addHeader("Accepts", "application/json")
            .addHeader("X-CMC_PRO_API_KEY", apiKey)
            .build()

        // Make call
        return client.newCall(request).execute()
    }


}