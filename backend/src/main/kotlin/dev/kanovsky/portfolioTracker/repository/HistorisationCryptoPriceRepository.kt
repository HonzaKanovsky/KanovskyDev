package dev.kanovsky.portfolioTracker.repository

import dev.kanovsky.portfolioTracker.model.Crypto
import dev.kanovsky.portfolioTracker.model.HistorisationCryptoPrice
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate


/**
 * Repository for managing historical cryptocurrency price data.
 **/
@Repository
interface HistorisationCryptoPriceRepository : JpaRepository<HistorisationCryptoPrice, Long> {

    /**
     * Finds the most recent historical price entry for a given cryptocurrency.
     * @param crypto The cryptocurrency entity.
     * @return The latest HistorisationCryptoPrice entry or `null` if no data exists.
     **/
    fun findFirstByCryptoOrderByTimestampDesc(crypto: Crypto): HistorisationCryptoPrice?

    /**
     * Retrieves historical price data for a cryptocurrency within a specific date range.
     * @param crypto The cryptocurrency entity.
     * @param start The start date for filtering.
     * @param end The end date for filtering.
     * @return A list of historical prices ordered by timestamp in descending order.
     **/
    fun findByCryptoAndTimestampBetweenOrderByTimestampDesc(
        crypto: Crypto,
        start: LocalDate,
        end: LocalDate
    ): List<HistorisationCryptoPrice>

    /**
     * Retrieves a paginated list of historical cryptocurrency prices for a given date.
     * @param pageable Pagination details.
     * @param timestamp The date for which to retrieve historical prices.
     * @return A paginated list of HistorisationCryptoPrice entries.
     **/
    fun findHistorisationCryptoPricesByTimestamp(
        pageable: Pageable,
        timestamp: LocalDate
    ): Page<HistorisationCryptoPrice>

    /**
     * Finds all historical price entries for a specific date.
     * @param date The date for which to retrieve data.
     * @return A list of historical prices on the specified date.
     **/
    fun findAllByTimestamp(date: LocalDate): List<HistorisationCryptoPrice>

    /**
     * Finds the latest available timestamp before a given date.
     * @param currentDate The reference date.
     * @return The latest timestamp before `currentDate`, or `null` if none exist.
     **/
    @Query("SELECT MAX(h.timestamp) FROM HistorisationCryptoPrice h WHERE h.timestamp < :currentDate")
    fun findLatestTimestampBefore(currentDate: LocalDate): LocalDate?

    /**
     * Retrieves the latest recorded cryptocurrency prices.
     *
     * @param pageable The pagination information.
     * @return A paginated list (`Page`) of `HistorisationCryptoPrice` records with the latest timestamp.
     */
    @Query("SELECT h FROM HistorisationCryptoPrice h WHERE h.timestamp = (SELECT MAX(h2.timestamp) FROM HistorisationCryptoPrice h2)")
    fun findLatestHistorisationCryptoPrices(pageable: Pageable?): Page<HistorisationCryptoPrice?>
}