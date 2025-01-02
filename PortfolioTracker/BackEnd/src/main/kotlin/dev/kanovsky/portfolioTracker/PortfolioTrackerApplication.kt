package dev.kanovsky.portfolioTracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PortfolioTrackerApplication

fun main(args: Array<String>) {
	runApplication<PortfolioTrackerApplication>(*args)
}
