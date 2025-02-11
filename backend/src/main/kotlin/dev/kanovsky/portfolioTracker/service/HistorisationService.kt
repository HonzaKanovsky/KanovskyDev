package dev.kanovsky.portfolioTracker.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

/**
 * Service responsible for scheduling daily cryptocurrency price historisation.
 */
@Service
class HistorisationService(
    private val cryptoService: CryptoService
) {

    /**
     * Scheduled task to update cryptocurrency historical prices daily at 01:00 UTC.
     * Runs every day at 1 AM UTC.
     **/
    @Scheduled(cron = "0 0 1 * * ?", zone = "UTC")
    fun saveDailyHistorisation() {
        try {
            // Fetch and update historical data for 5000 cryptocurrencies (maximum ammount to fetch from CMC api)
            cryptoService.updateDBCryptoEntries(amount = 5000)
            println("Daily historisation job completed successfully.")
        } catch (e: Exception) {
            println("Error occurred in daily historisation job: ${e.message}")
            e.printStackTrace()
        }
    }
}