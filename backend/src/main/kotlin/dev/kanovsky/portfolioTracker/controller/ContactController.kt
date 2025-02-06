package dev.kanovsky.portfolioTracker.controller

import dev.kanovsky.portfolioTracker.dto.ContactRequestDTO
import dev.kanovsky.portfolioTracker.service.EmailService
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["http://localhost:5173"])
@RestController
@RequestMapping("/api/contact")
class ContactController(private val emailService: EmailService) {

    @PostMapping
    fun sendEmail(@RequestBody request: ContactRequestDTO): String {
        emailService.sendEmail(request.name, request.email, request.phoneNumber, request.message)
        return "Email sent successfully!"
    }
}
