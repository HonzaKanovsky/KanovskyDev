package dev.kanovsky.portfolioTracker.Service

import dev.kanovsky.portfolioTracker.Dto.ApiResponse
import dev.kanovsky.portfolioTracker.Dto.CryptoDTO
import dev.kanovsky.portfolioTracker.Dto.CryptoDetailDto
import dev.kanovsky.portfolioTracker.Dto.HistorisationCryptoPriceDTO
import dev.kanovsky.portfolioTracker.Model.Crypto
import dev.kanovsky.portfolioTracker.Model.HistorisationCryptoPrice
import dev.kanovsky.portfolioTracker.Repository.CryptoRepository
import dev.kanovsky.portfolioTracker.Repository.HistorisationCryptoPriceRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
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
    companion object {
        private val logger: Logger = LogManager.getLogger(CryptoService::class.java)
    }
    fun getAllCryptos(pageable: Pageable): Page<Crypto> = cryptoRepository.findAll(pageable)

    fun getCryptoById(id: Long): Crypto =
        cryptoRepository.findById(id).orElseThrow { IllegalArgumentException("Crypto with id $id not found") }

    fun getHistoricalData(cryptoId: Long, startDate: String?, endDate: String?): ApiResponse<CryptoDetailDto> {
        val crypto = getCryptoById(cryptoId)
        val start = LocalDate.parse(startDate ?: LocalDate.now().minusMonths(12).toString())
        val end = LocalDate.parse(endDate ?: LocalDate.now().toString())
        val cryptosInInterval = historisationCryptoPriceRepository.findByCryptoAndTimestampBetween(crypto, start, end)

        val cryptosInIntervalDto = mutableListOf<HistorisationCryptoPriceDTO>()
        for (cryptoEntry in cryptosInInterval){
            cryptosInIntervalDto.add(cryptoEntry.toDto())
        }

        val cryptoDetailDto = CryptoDetailDto(cryptoDTO = crypto.toDto(), historisationCryptoPriceDTOs = cryptosInIntervalDto)
        return ApiResponse(success = true, message = "Prices in interval are found", data = cryptoDetailDto)
    }

    fun updatedCryptoPrices(amount: Long): ApiResponse<CryptoDTO> {
        val apiKey = System.getenv("CMC_API_KEY") ?: throw IllegalStateException("API key not found")
        val baseUrl = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"
        val url = "$baseUrl?start=1&limit=$amount&convert=USD"


        val response = requestCMCApi(apiKey, url)
        val responseData = response.body?.string()

        return if (response.isSuccessful) {
            val jsonResponse = JSONObject(responseData)
            val data = jsonResponse.getJSONArray("data")

            updateCryptos(data, cryptoRepository)
            ApiResponse(true,"Updating crypto prices was successful")
        }else{
            ApiResponse(false,"Updating crypto prices failed: ${response.message}")
        }
    }

    private fun updateCryptos(data: JSONArray, cryptoRepository: CryptoRepository){
        val existingCryptos = cryptoRepository.findAll()
        val existingCryptoMap = existingCryptos.associateBy { it.name to it.symbol }
        val currentDate = LocalDate.now()

        val newOrUpdatedCryptos = mutableListOf<Crypto>()
        val newOrUpdatedHistorisationCryptoPrices = mutableListOf<HistorisationCryptoPrice>()

        for (i in 0 until data.length()) {
            val cryptoEntry = data.getJSONObject(i)
            val name = cryptoEntry.getString("name")
            val symbol = cryptoEntry.getString("symbol")

            val quote = cryptoEntry.getJSONObject("quote").getJSONObject("USD")
            val price = quote.getBigDecimal("price")
            val marketCap = quote.getLong("market_cap")

            val existingCrypto = existingCryptoMap[name to symbol]
            val updatedCrypto = existingCrypto ?: Crypto(name = name, symbol = symbol)

            val existingEntry = historisationCryptoPriceRepository.findByCryptoAndTimestamp(updatedCrypto, currentDate)
            val updatedEntry = existingEntry?.copy(price = price, marketCap = marketCap)
                ?: HistorisationCryptoPrice(crypto = updatedCrypto, timestamp = currentDate, price = price, marketCap = marketCap)

            newOrUpdatedCryptos.add(updatedCrypto)
            newOrUpdatedHistorisationCryptoPrices.add(updatedEntry)
        }
        cryptoRepository.saveAll(newOrUpdatedCryptos)
        historisationCryptoPriceRepository.saveAll(newOrUpdatedHistorisationCryptoPrices)
    }

/*    private fun saveOrUpdateDailyHistory(cryptoList: MutableList<Crypto>) : List<HistorisationCryptoPrice> {
        val newOrUpdatedHistorisationCryptoPrices = mutableListOf<HistorisationCryptoPrice>()

        for(crypto in cryptoList){
            val existingRecord = historisationCryptoPriceRepository.findByCryptoAndTimestamp(crypto, currentDate)
            val updatedRecord = if (existingRecord != null) {
            } else {
                HistorisationCryptoPrice(crypto = crypto, timestamp = currentDate, price = crypto.price)
            }
            newOrUpdatedHistorisationCryptoPrices.add(updatedRecord)
        }
        return newOrUpdatedHistorisationCryptoPrices
    }
*/
    private fun requestCMCApi(apiKey: String, url: String): Response {

        // Set up OkHttp client
        val client = OkHttpClient.Builder()
            .build()

        // Create the request
        val request = Request.Builder()
            .url(url)
            .addHeader("Accepts", "application/json")
            .addHeader("X-CMC_PRO_API_KEY", apiKey)
            .build()

        // Execute the request and get the response
        return client.newCall(request).execute()
    }
}