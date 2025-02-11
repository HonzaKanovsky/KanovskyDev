package dev.kanovsky.portfolioTracker.controller

import dev.kanovsky.portfolioTracker.service.PdfGeneratorService
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

/**
 * Controller responsible for generating PDF resumes.
 **/
@RestController
@RequestMapping("/api/resume")
class ResumeController(
    private val pdfGeneratorService: PdfGeneratorService
) {

    /**
     * Generates a PDF resume from the provided JSON data and optional profile picture.
     * @param resumeRequestString The resume request data as a JSON string.
     * @param profilePicture An optional profile picture file.
     * @return A ResponseEntity containing the generated PDF file or an error response.
     **/
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun generatePdf(
        @RequestPart("resumeRequestDTO") resumeRequestString: String, // JSON as String
        @RequestPart("profilePicture") profilePicture: MultipartFile?
    ): ResponseEntity<ByteArrayResource> {
        return try {
            // Generate the PDF and return it as a downloadable file
            ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resume.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfGeneratorService.createPdf(resumeRequestString, profilePicture))
        } catch (ex: Exception) {
            // Log the error and return an internal server error response
            println("Error: ${ex.message}")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ByteArrayResource("File read error".toByteArray()))
        }
    }


}
