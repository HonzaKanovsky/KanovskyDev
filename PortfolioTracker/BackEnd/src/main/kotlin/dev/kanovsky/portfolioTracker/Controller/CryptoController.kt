package dev.kanovsky.portfolioTracker.Controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cryptos")
class CryptoController {

    @GetMapping("/test")
    fun getAll(): String{
        return "Test"
    }
}