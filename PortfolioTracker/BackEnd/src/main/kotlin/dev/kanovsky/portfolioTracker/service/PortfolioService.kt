package dev.kanovsky.portfolioTracker.service

import dev.kanovsky.portfolioTracker.dto.ApiResponse
import dev.kanovsky.portfolioTracker.dto.PortfolioEntryDTO
import dev.kanovsky.portfolioTracker.dto.UserDetailDTO
import dev.kanovsky.portfolioTracker.model.PortfolioEntry
import dev.kanovsky.portfolioTracker.model.User
import dev.kanovsky.portfolioTracker.repository.CryptoRepository
import dev.kanovsky.portfolioTracker.repository.HistorisationCryptoPriceRepository
import dev.kanovsky.portfolioTracker.repository.PortfolioEntryRepository
import dev.kanovsky.portfolioTracker.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class PortfolioService(
    private val portfolioEntryRepository: PortfolioEntryRepository,
    private val userRepository: UserRepository,
    private val cryptoRepository: CryptoRepository,
    private val historisationCryptoPriceRepository: HistorisationCryptoPriceRepository,
    private val AuthorisationService: AuthorisationService
) {

    fun getPortfolioEntriesByUserId(userId: Long, pageable: Pageable, token: String): ApiResponse<UserDetailDTO> {
        val user =
            userRepository.findUserById(userId) ?: return ApiResponse(success = false, message = "User does not exist")

        if (!AuthorisationService.checkAllowedAccess(userId, token)) {
            return ApiResponse(success = false, message = "It is not possible to access this data")
        }
        return createUserDetailResponse(user, pageable)
    }

    fun addPortfolioEntry(
        userId: Long,
        cryptoId: Long,
        amount: Double,
        pageable: Pageable,
        token: String
    ): ApiResponse<UserDetailDTO> {
        val user =
            userRepository.findUserById(userId) ?: return ApiResponse(success = false, message = "User does not exist")
        val crypto =
            cryptoRepository.findCryptoById(cryptoId) ?: return ApiResponse(
                success = false,
                message = "Crypto currency does not exist",
                data = createUserDetailResponse(user, pageable).data
            )
        if (portfolioEntryRepository.findByUserAndCrypto(user, crypto) != null) {
            cryptoRepository.findCryptoById(cryptoId) ?: return ApiResponse(
                success = false,
                message = "This crypto currency is already present in users portfolio",
                data = createUserDetailResponse(user, pageable).data
            )
        }
        if (!AuthorisationService.checkAllowedAccess(userId, token)) {
            return ApiResponse(success = false, message = "It is not possible to access this data")
        }

        val portfolioEntry = portfolioEntryRepository.findByUserAndCrypto(user, crypto)
        if (portfolioEntry != null) {
            return ApiResponse(
                success = false,
                message = "This crypto currency is already present in users portfolio",
                data = createUserDetailResponse(user, pageable).data
            )
        } else {
            val newEntry = PortfolioEntry(
                user = user,
                crypto = crypto,
                amount = BigDecimal.valueOf(amount)
            )
            portfolioEntryRepository.save(newEntry)
        }

        return createUserDetailResponse(user, pageable)
    }

    fun updatePortfolioEntry(
        userId: Long,
        cryptoId: Long,
        amount: BigDecimal,
        pageable: Pageable,
        token: String
    ): ApiResponse<UserDetailDTO> {
        val user =
            userRepository.findUserById(userId) ?: return ApiResponse(success = false, message = "User does not exist")
        val crypto =
            cryptoRepository.findCryptoById(cryptoId) ?: return ApiResponse(
                success = false,
                message = "Crypto currency does not exist",
                data = createUserDetailResponse(user, pageable).data
            )

        if (!AuthorisationService.checkAllowedAccess(userId, token)) {
            return ApiResponse(success = false, message = "It is not possible to access this data")
        }
        val portfolioEntry = portfolioEntryRepository.findByUserAndCrypto(user, crypto)
        if (portfolioEntry == null) {
            return ApiResponse(
                success = false,
                message = "This crypto currency is not present in users portfolio",
                data = createUserDetailResponse(user, pageable).data
            )
        } else {
            portfolioEntry.amount = amount
            portfolioEntryRepository.save(portfolioEntry)
        }

        return createUserDetailResponse(user, pageable)
    }

    fun removePortfolioEntry(
        userId: Long,
        cryptoId: Long,
        pageable: Pageable,
        token: String
    ): ApiResponse<UserDetailDTO> {
        val user =
            userRepository.findUserById(userId) ?: return ApiResponse(success = false, message = "User does not exist")
        val crypto =
            cryptoRepository.findCryptoById(cryptoId) ?: return ApiResponse(
                success = false,
                message = "Crypto currency does not exist",
                data = createUserDetailResponse(user, pageable).data
            )

        if (!AuthorisationService.checkAllowedAccess(userId, token)) {
            return ApiResponse(success = false, message = "It is not possible to access this data")
        }
        val portfolioEntry = portfolioEntryRepository.findByUserAndCrypto(user, crypto)
        if (portfolioEntry == null) {
            return ApiResponse(
                success = false,
                message = "This crypto currency is not present in users portfolio",
                data = createUserDetailResponse(user, pageable).data
            )
        } else {
            portfolioEntryRepository.delete(portfolioEntry)
        }

        return createUserDetailResponse(user, pageable)
    }

    private fun createUserDetailResponse(
        user: User,
        pageable: Pageable
    ): ApiResponse<UserDetailDTO> {
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

        val portfolioEntriesDto: Page<PortfolioEntryDTO> = PageImpl(
            portfolioEntriesDtoList,
            pageable,
            portfolioEntries.totalElements
        )

        return ApiResponse(
            success = true,
            message = "Fetching portfolio data for user is successful",
            data = UserDetailDTO(user = userDto, portfolio = portfolioEntriesDto, portfolioValue = portfolioValue)
        )
    }
}