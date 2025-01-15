package dev.kanovsky.portfolioTracker.service

import dev.kanovsky.portfolioTracker.dto.ApiResponse
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

@Service
class CryptoService(
    private val cryptoRepository: CryptoRepository,
    private val historisationCryptoPriceRepository: HistorisationCryptoPriceRepository,
) {
    fun getAllCryptos(pageable: Pageable): Page<Crypto> = cryptoRepository.findAll(pageable)
    fun getAllCryptosWithPricesToday(pageable: Pageable): Page<HistorisationCryptoPriceDTO> {
        return try {
            val pricesPage =
                historisationCryptoPriceRepository.findHistorisationCryptoPricesByTimestamp(pageable, LocalDate.now());
            return pricesPage.map { it.toDto(containCryptoDto = true) }
        } catch (e: Exception) {
            Page.empty(pageable)
        }
    }

    fun getCryptoById(id: Long): Crypto =
        cryptoRepository.findById(id).orElseThrow { IllegalArgumentException("Crypto with id $id not found") }

    fun getCryptoDataById(id: Long): ApiResponse<CryptoDTO> {
        return ApiResponse(
            success = true,
            message = "Requested crypto currency found",
            data = getCryptoById(id).toDto()
        )
    }

    fun getHistoricalData(cryptoId: Long, startDate: String?, endDate: String?): ApiResponse<CryptoDetailDTO> {
        val crypto = getCryptoById(cryptoId)
        val start = LocalDate.parse(startDate ?: LocalDate.now().minusMonths(12).toString())
        val end = LocalDate.parse(endDate ?: LocalDate.now().toString())
        val cryptosInInterval = historisationCryptoPriceRepository.findByCryptoAndTimestampBetween(crypto, start, end)

        val cryptosInIntervalDto = mutableListOf<HistorisationCryptoPriceDTO>()
        for (cryptoEntry in cryptosInInterval) {
            cryptosInIntervalDto.add(cryptoEntry.toDto())
        }

        val cryptoDetailDto =
            CryptoDetailDTO(crypto = crypto.toDto(), historisationCryptoPrices = cryptosInIntervalDto)
        return ApiResponse(success = true, message = "Prices in interval are found", data = cryptoDetailDto)
    }

    /*
    * Function called to initialize fetching and saving new crypto data
    *
    */
    fun updateDBCryptoEntries(amount: Long, currency: CurrencyCode = CurrencyCode.USD): ApiResponse<CryptoDTO> {
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

                        ApiResponse(true, "Updating crypto prices was successful")
                    } else {
                        ApiResponse(false, "Fetching data from CMC was unsuccessful: ${response.message}")
                    }
                } catch (e: Exception) {
                    ApiResponse(false, "Fetching data from CMC failed due to technical error: ${e.message}")
                })
    }

    private fun saveNewHistorisationCryptoPrices(data: JSONArray) {
        //Get map of cryptos saved in database
        val existingCryptos = cryptoRepository.findAll()
        val existingCryptoMap = existingCryptos.associateBy { it.name to it.symbol }

        val newHistorisationCryptoPrices = mutableListOf<HistorisationCryptoPrice>()

        val currentDate = LocalDate.now()

        for (i in 0 until data.length()) {
            val cryptoEntryJSON = data.getJSONObject(i)
            val name = cryptoEntryJSON.getString("name")
            val symbol = cryptoEntryJSON.getString("symbol")

            val quote = cryptoEntryJSON.getJSONObject("quote").getJSONObject("USD")
            val price = quote.getBigDecimal("price")
            val marketCap = quote.getLong("market_cap")

            val existingCrypto = existingCryptoMap[name to symbol]
            val cryptoToBeUpdated = existingCrypto ?: cryptoRepository.save(Crypto(name = name, symbol = symbol))

            newHistorisationCryptoPrices.add(
                HistorisationCryptoPrice(
                    crypto = cryptoToBeUpdated,
                    timestamp = currentDate,
                    price = price,
                    marketCap = marketCap
                )
            )
        }
        historisationCryptoPriceRepository.saveAll(newHistorisationCryptoPrices)
    }

    /*
    * Function responsible for retrieving data from CoinMarketCap API
    */
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