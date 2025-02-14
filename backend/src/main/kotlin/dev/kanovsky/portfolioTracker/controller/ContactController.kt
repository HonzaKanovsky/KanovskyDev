package dev.kanovsky.portfolioTracker.controller

import dev.kanovsky.portfolioTracker.annotations.ApiDescription
import dev.kanovsky.portfolioTracker.dto.ContactRequestDTO
import dev.kanovsky.portfolioTracker.service.EmailService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller responsible for handling contact form submissions.
 **/
@RestController
@RequestMapping("/api/contact")
class ContactController(private val emailService: EmailService) {

    /**
     * Handles contact form submissions by sending an email.
     * @param request The contact details provided in the request body.
     * @return A success message confirming the email was sent.
     **/
    @PostMapping
    @ApiDescription("Handles contact form submissions by sending an email.")
    fun sendEmail(@RequestBody request: ContactRequestDTO): String {
        emailService.sendEmail(request.name, request.email, request.phoneNumber, request.message)
        return "Email sent successfully!"
    }
}
