package dev.kanovsky.portfolioTracker

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class PortfolioTrackerApplication

private val logger = LoggerFactory.getLogger(PortfolioTrackerApplication::class.java)

fun main(args: Array<String>) {
    logger.info("Starting Portfolio Tracker Application...")
    runApplication<PortfolioTrackerApplication>(*args)
    logger.info("Portfolio Tracker Application Started Successfully!")
}
