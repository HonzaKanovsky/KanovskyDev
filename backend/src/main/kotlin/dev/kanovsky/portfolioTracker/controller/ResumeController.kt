package dev.kanovsky.portfolioTracker.controller

import dev.kanovsky.portfolioTracker.service.PdfGeneratorService
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@CrossOrigin(origins = ["http://localhost:5173"])
@RestController
@RequestMapping("/api/resume")
class ResumeController(
    private val pdfGeneratorService: PdfGeneratorService
) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun generatePdf(
        @RequestPart("resumeRequestDTO") resumeRequestString: String, // JSON as String
        @RequestPart("profilePicture") profilePicture: MultipartFile?
    ): ResponseEntity<ByteArrayResource> {
        return try {
            ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resume.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfGeneratorService.createPdf(resumeRequestString, profilePicture))
        } catch (ex: Exception) {
            println("Error: ${ex.message}")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ByteArrayResource("File read error".toByteArray()))
        }
    }


}
