package dev.kanovsky.portfolioTracker.service

import dev.kanovsky.portfolioTracker.model.Crypto
import dev.kanovsky.portfolioTracker.repository.CryptoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class CryptoServiceTest {

    private val cryptoRepository: CryptoRepository = mockk()
    private val cryptoService = CryptoService(cryptoRepository, mockk())

    @Test
    fun `should return crypto by ID`() {
        val crypto = Crypto(id = 1L, name = "Bitcoin", symbol = "BTC")

        every { cryptoRepository.findById(1L) } returns Optional.of(crypto)

        val result = cryptoService.getCryptoDataById(1L)

        assertTrue(result.isSuccess)
        assertEquals("Bitcoin", result.getOrNull()?.name)
        verify(exactly = 1) { cryptoRepository.findById(1L) }
    }

    @Test
    fun `should return failure when crypto not found`() {
        every { cryptoRepository.findById(1L) } returns Optional.empty()

        val result = cryptoService.getCryptoDataById(1L)

        assertTrue(result.isFailure)
        verify(exactly = 1) { cryptoRepository.findById(1L) }
    }
}
