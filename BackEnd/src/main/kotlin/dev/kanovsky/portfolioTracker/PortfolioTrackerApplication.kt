package dev.kanovsky.portfolioTracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class PortfolioTrackerApplication

fun main(args: Array<String>) {
    runApplication<PortfolioTrackerApplication>(*args)
}
