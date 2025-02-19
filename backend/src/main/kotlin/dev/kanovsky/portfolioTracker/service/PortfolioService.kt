package dev.kanovsky.portfolioTracker.service

import dev.kanovsky.portfolioTracker.dto.PortfolioEntryDTO
import dev.kanovsky.portfolioTracker.dto.UserDetailDTO
import dev.kanovsky.portfolioTracker.exceptions.*
import dev.kanovsky.portfolioTracker.model.PortfolioEntry
import dev.kanovsky.portfolioTracker.model.User
import dev.kanovsky.portfolioTracker.repository.CryptoRepository
import dev.kanovsky.portfolioTracker.repository.HistorisationCryptoPriceRepository
import dev.kanovsky.portfolioTracker.repository.PortfolioEntryRepository
import dev.kanovsky.portfolioTracker.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal

/**
 * Service responsible for managing user portfolios.
 **/
@Service
class PortfolioService(
    private val portfolioEntryRepository: PortfolioEntryRepository,
    private val userRepository: UserRepository,
    private val cryptoRepository: CryptoRepository,
    private val historisationCryptoPriceRepository: HistorisationCryptoPriceRepository,
    private val authorisationService: AuthorisationService
) {
    private val logger = LoggerFactory.getLogger(PortfolioService::class.java)

    /**
     * Retrieves a user's portfolio entries.
     * @param userId The user's ID.
     * @param pageable Pagination details.
     * @param token The JWT token for authentication.
     * @return User details with portfolio entries or an error.
     **/
    fun getPortfolioEntriesByUserId(userId: Long, pageable: Pageable, token: String): Result<UserDetailDTO> {
        val user =
            userRepository.findUserById(userId) ?: return Result.failure(UserNotFoundException("User does not exist"))

        if (!authorisationService.checkAllowedAccess(userId, token)) {
            return Result.failure(UnauthorizedAccessException("It is not possible to access this data"))
        }

        return createUserDetailResponse(user, pageable)
    }

    /**
     * Adds a new cryptocurrency entry to the user's portfolio.
     * @param userId The user's ID.
     * @param cryptoId The cryptocurrency's ID.
     * @param amount The amount of cryptocurrency.
     * @param pageable Pagination details.
     * @param token The JWT token for authentication.
     * @return Updated user portfolio or an error.
     **/
    fun addPortfolioEntry(
        userId: Long,
        cryptoId: Long,
        amount: Double,
        pageable: Pageable,
        token: String
    ): Result<UserDetailDTO> {
        val user =
            userRepository.findUserById(userId)
                ?: run {
                    logger.warn("Failed to add portfolio entry: User with ID $userId not found")
                    return Result.failure(UserNotFoundException("User does not exist"))
                }
        val crypto = cryptoRepository.findCryptoById(cryptoId)
            ?: run {
                logger.warn("Failed to add portfolio entry: Crypto with ID $cryptoId not found")
                return Result.failure(CryptoNotFoundException("Crypto currency does not exist"))
            }

        if (!authorisationService.checkAllowedAccess(userId, token)) {
            return Result.failure(UnauthorizedAccessException("It is not possible to access this data"))
        }

        if (portfolioEntryRepository.findByUserAndCrypto(user, crypto) != null) {
            logger.warn("Unauthorized access attempt to add portfolio entry for user $userId")
            return Result.failure(DuplicatePortfolioEntryException("This crypto currency is already present in user's portfolio"))
        }

        val newEntry = PortfolioEntry(user = user, crypto = crypto, amount = BigDecimal.valueOf(amount))
        portfolioEntryRepository.save(newEntry)

        logger.info("User $userId added $amount of ${crypto.symbol} to portfolio")
        return createUserDetailResponse(user, pageable)
    }

    /**
     * Updates an existing cryptocurrency entry in the user's portfolio.
     * @param userId The user's ID.
     * @param cryptoId The cryptocurrency's ID.
     * @param amount The new amount of cryptocurrency.
     * @param pageable Pagination details.
     * @param token The JWT token for authentication.
     * @return Updated user portfolio or an error.
     **/
    fun updatePortfolioEntry(
        userId: Long,
        cryptoId: Long,
        amount: BigDecimal,
        pageable: Pageable,
        token: String
    ): Result<UserDetailDTO> {
        val user =
            userRepository.findUserById(userId) ?: return Result.failure(UserNotFoundException("User does not exist"))
        val crypto = cryptoRepository.findCryptoById(cryptoId)
            ?: return Result.failure(CryptoNotFoundException("Crypto currency does not exist"))

        if (!authorisationService.checkAllowedAccess(userId, token)) {
            return Result.failure(UnauthorizedAccessException("It is not possible to access this data"))
        }

        val portfolioEntry = portfolioEntryRepository.findByUserAndCrypto(user, crypto)
            ?: return Result.failure(PortfolioEntryNotFoundException("This crypto currency is not present in user's portfolio"))

        portfolioEntry.amount = amount
        portfolioEntryRepository.save(portfolioEntry)

        return createUserDetailResponse(user, pageable)
    }

    /**
     * Removes a cryptocurrency entry from the user's portfolio.
     * @param userId The user's ID.
     * @param cryptoId The cryptocurrency's ID.
     * @param pageable Pagination details.
     * @param token The JWT token for authentication.
     * @return Updated user portfolio or an error.
     **/
    fun removePortfolioEntry(userId: Long, cryptoId: Long, pageable: Pageable, token: String): Result<UserDetailDTO> {
        val user =
            userRepository.findUserById(userId) ?: return Result.failure(UserNotFoundException("User does not exist"))
        val crypto = cryptoRepository.findCryptoById(cryptoId)
            ?: return Result.failure(CryptoNotFoundException("Crypto currency does not exist"))

        if (!authorisationService.checkAllowedAccess(userId, token)) {
            return Result.failure(UnauthorizedAccessException("It is not possible to access this data"))
        }

        val portfolioEntry = portfolioEntryRepository.findByUserAndCrypto(user, crypto)
            ?: return Result.failure(PortfolioEntryNotFoundException("This crypto currency is not present in user's portfolio"))

        portfolioEntryRepository.delete(portfolioEntry)

        return createUserDetailResponse(user, pageable)
    }

    /**
     * Creates a UserDetailDTO response, including the user's portfolio details and total value.
     * @param user The user entity.
     * @param pageable Pagination details.
     * @return A result containing UserDetailDTO with portfolio details.
     **/
    private fun createUserDetailResponse(user: User, pageable: Pageable): Result<UserDetailDTO> {
        val userDto = user.toDto()
        val portfolioEntries = portfolioEntryRepository.findByUser(user, pageable)

        val portfolioEntriesDtoList = mutableListOf<PortfolioEntryDTO>()
        var portfolioValue = BigDecimal.ZERO

        for (entry in portfolioEntries) {
            val latestValue = historisationCryptoPriceRepository.findFirstByCryptoOrderByTimestampDesc(entry.crypto)
            if (latestValue != null) {
                val entryDto = entry.toDto()
                entryDto.currentPrice = latestValue.price * entry.amount
                portfolioValue += entryDto.currentPrice
                portfolioEntriesDtoList.add(entryDto)
            }
        }

        val portfolioEntriesDto: Page<PortfolioEntryDTO> =
            PageImpl(portfolioEntriesDtoList, pageable, portfolioEntries.totalElements)

        return Result.success(
            UserDetailDTO(
                user = userDto,
                portfolio = portfolioEntriesDto,
                portfolioValue = portfolioValue
            )
        )
    }
}
