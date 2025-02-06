package dev.kanovsky.portfolioTracker.service

import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailService(private val mailSender: JavaMailSender) {

    fun sendEmail(name: String, email: String, phoneNumber: String, message: String) {
        val mimeMessage: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, true)

        helper.setFrom(System.getenv("EMAIL_USERNAME"))
        helper.setTo("business@kanovsky.dev")
        helper.setSubject("New Contact Form Submission")
        helper.setText(
            """
            Name: $name
            Email: $email
            Phone: $phoneNumber
            
            Message:
            $message
            """.trimIndent(),
            false
        )

        mailSender.send(mimeMessage)
    }
}
