package dev.kanovsky.portfolioTracker.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class HistorisationService(
    private val cryptoService: CryptoService
) {
    @Scheduled(cron = "0 0 1 * * ?", zone = "UTC")
    fun saveDailyHistorisation() {
        try {
            cryptoService.updateDBCryptoEntries(amount = 5000)
            println("Daily historisation job completed successfully.")
        } catch (e: Exception) {
            println("Error occurred in daily historisation job: ${e.message}")
            e.printStackTrace()
        }
    }
}