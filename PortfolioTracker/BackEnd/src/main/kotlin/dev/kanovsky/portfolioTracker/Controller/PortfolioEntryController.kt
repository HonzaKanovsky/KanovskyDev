package dev.kanovsky.portfolioTracker.Controller

import dev.kanovsky.portfolioTracker.Model.PortfolioEntry
import dev.kanovsky.portfolioTracker.Service.PortfolioEntryService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/portfolios")
class PortfolioEntryController(private val portfolioEntryService: PortfolioEntryService) {

    @GetMapping("/{userId}")
    fun getPortfolioEntriesByUserId(@PathVariable userId: Long): List<PortfolioEntry> =
        portfolioEntryService.getPortfolioEntriesByUserId(userId)

    @PostMapping
    fun addPortfolioEntry(
        @RequestParam userId: Long,
        @RequestParam cryptoId: Long,
        @RequestParam amount: Double
    ): PortfolioEntry = portfolioEntryService.addPortfolioEntry(userId, cryptoId, amount)

    @DeleteMapping("/{id}")
    fun deletePortfolioEntry(@PathVariable id: Long) = portfolioEntryService.deletePortfolioEntry(id)
}