package dev.kanovsky.portfolioTracker.service

import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

/**
 * Service responsible for sending emails using JavaMailSender.
 **/
@Service
class EmailService(private val mailSender: JavaMailSender) {

    /**
     * Sends an email with the given contact details.
     * @param name The sender's name.
     * @param email The sender's email address.
     * @param phoneNumber The sender's phone number.
     * @param message The message content.
     **/
    fun sendEmail(name: String, email: String, phoneNumber: String, message: String) {
        val mimeMessage: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, true)

        // Set email sender (from environment variable for security)
        helper.setFrom(System.getenv("EMAIL_USERNAME"))

        // Set recipient email
        helper.setTo("business@kanovsky.dev")

        // Set email subject
        helper.setSubject("New Contact Form Submission")

        // Construct the email content
        helper.setText(
            """
            Name: $name
            Email: $email
            Phone: $phoneNumber
            
            Message:
            $message
            """.trimIndent(),
            false  // `false` indicates plain text email
        )

        // Send the email
        mailSender.send(mimeMessage)
    }
}
