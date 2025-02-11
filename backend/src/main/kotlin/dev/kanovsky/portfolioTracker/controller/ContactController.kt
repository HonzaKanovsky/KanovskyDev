package dev.kanovsky.portfolioTracker.controller

import dev.kanovsky.portfolioTracker.dto.ContactRequestDTO
import dev.kanovsky.portfolioTracker.service.EmailService
import org.springframework.web.bind.annotation.*

/**
 * Controller responsible for handling contact form submissions.
 **/
@CrossOrigin(origins = [])
@RestController
@RequestMapping("/api/contact")
class ContactController(private val emailService: EmailService) {

    /**
     * Handles contact form submissions by sending an email.
     * @param request The contact details provided in the request body.
     * @return A success message confirming the email was sent.
     **/
    @PostMapping
    fun sendEmail(@RequestBody request: ContactRequestDTO): String {
        emailService.sendEmail(request.name, request.email, request.phoneNumber, request.message)
        return "Email sent successfully!"
    }
}
