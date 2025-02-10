package dev.kanovsky.portfolioTracker.controller

import dev.kanovsky.portfolioTracker.dto.CryptoDTO
import dev.kanovsky.portfolioTracker.dto.CryptoDetailDTO
import dev.kanovsky.portfolioTracker.dto.HistorisationCryptoPriceDTO
import dev.kanovsky.portfolioTracker.model.Crypto
import dev.kanovsky.portfolioTracker.service.CryptoService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.math.BigDecimal
import java.time.LocalDate

class CryptoControllerTest {

    private lateinit var mockMvc: MockMvc
    private val cryptoService = mockk<CryptoService>() // ✅ Manually create a mock instance

    @BeforeEach
    fun setup() {
        val cryptoController = CryptoController(cryptoService) // ✅ Inject mock manually
        mockMvc = MockMvcBuilders.standaloneSetup(cryptoController).build()
    }

    @Test
    fun `should return paginated list of cryptos`() {
        val cryptoList = listOf(Crypto(id = 1L, name = "Bitcoin", symbol = "BTC"))
        val page: Page<Crypto> = PageImpl(cryptoList, PageRequest.of(0, 30), cryptoList.size.toLong())

        val pageable = PageRequest.of(0, 30) // ✅ Manually create a pageable instance

        every { cryptoService.getAllCryptos(pageable) } returns page

        mockMvc.perform(
            get("/api/cryptos")
                .param("page", "0")
                .param("size", "30")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].name").value("Bitcoin"))
            .andExpect(jsonPath("$.content[0].symbol").value("BTC"))

        verify(exactly = 1) { cryptoService.getAllCryptos(any()) }
    }


    @Test
    fun `should return paginated list of cryptos with prices`() {
        val dtoList = listOf(
            HistorisationCryptoPriceDTO(
                price = BigDecimal.valueOf(50000.0),
                marketCap = 900_000_000_000,
                timestamp = LocalDate.parse("2023-01-01"),
                cryptoDTO = CryptoDTO(id = 1L, name = "Bitcoin", symbol = "BTC")
            )
        )
        val page: Page<HistorisationCryptoPriceDTO> = PageImpl(dtoList, PageRequest.of(0, 30), dtoList.size.toLong())

        every { cryptoService.getAllCryptosWithPricesToday(any()) } returns page

        mockMvc.perform(get("/api/cryptos/list"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].price").value(50000.0))

        verify(exactly = 1) { cryptoService.getAllCryptosWithPricesToday(any()) }
    }

    @Test
    fun `should return crypto by ID`() {
        val crypto = CryptoDTO(id = 1L, name = "Bitcoin", symbol = "BTC")

        every { cryptoService.getCryptoDataById(1L) } returns Result.success(crypto)

        mockMvc.perform(get("/api/cryptos/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Bitcoin"))
            .andExpect(jsonPath("$.symbol").value("BTC"))

        verify(exactly = 1) { cryptoService.getCryptoDataById(1L) }
    }

    @Test
    fun `should return 404 when crypto not found`() {
        every { cryptoService.getCryptoDataById(1L) } returns Result.failure(NoSuchElementException("Crypto not found"))

        mockMvc.perform(get("/api/cryptos/1"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.error").value("Crypto not found"))

        verify(exactly = 1) { cryptoService.getCryptoDataById(1L) }
    }

    @Test
    fun `should return historical data`() {
        val historicalData = CryptoDetailDTO(
            crypto = CryptoDTO(id = 1L, name = "Bitcoin", symbol = "BTC"),
            historisationCryptoPrices = listOf(
                HistorisationCryptoPriceDTO(
                    price = BigDecimal.valueOf(48000.0),
                    marketCap = 900_000_000_000,
                    timestamp = LocalDate.parse("2023-01-01"),
                    cryptoDTO = CryptoDTO(id = 1L, name = "Bitcoin", symbol = "BTC")
                )
            )
        )

        every { cryptoService.getHistoricalData(1L, "2023-01-01", "2023-12-31") } returns Result.success(historicalData)

        mockMvc.perform(
            get("/api/cryptos/1/history")
                .param("startDate", "2023-01-01")
                .param("endDate", "2023-12-31")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.crypto.name").value("Bitcoin"))
            .andExpect(jsonPath("$.historisationCryptoPrices[0].price").value(48000.0))
            .andExpect(jsonPath("$.historisationCryptoPrices[0].marketCap").value(900_000_000_000))

        verify(exactly = 1) { cryptoService.getHistoricalData(1L, "2023-01-01", "2023-12-31") }
    }

    @Test
    fun `should return 404 when historical data not found`() {
        every { cryptoService.getHistoricalData(1L, "2023-01-01", "2023-12-31") } returns Result.failure(
            NoSuchElementException("No data found")
        )

        mockMvc.perform(
            get("/api/cryptos/1/history")
                .param("startDate", "2023-01-01")
                .param("endDate", "2023-12-31")
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.error").value("No data found"))

        verify(exactly = 1) { cryptoService.getHistoricalData(1L, "2023-01-01", "2023-12-31") }
    }

    @Test
    fun `should update crypto prices manually`() {
        every { cryptoService.updateDBCryptoEntries(10L) } returns Result.success("Update successful")

        mockMvc.perform(
            post("/api/cryptos/update")
                .param("amount", "10")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().string("Update successful"))

        verify(exactly = 1) { cryptoService.updateDBCryptoEntries(10L) }
    }

    @Test
    fun `should return 404 when update fails`() {
        every { cryptoService.updateDBCryptoEntries(10L) } returns Result.failure(Exception("Update failed"))

        mockMvc.perform(
            post("/api/cryptos/update")
                .param("amount", "10")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.error").value("Update failed"))

        verify(exactly = 1) { cryptoService.updateDBCryptoEntries(10L) }
    }
}
