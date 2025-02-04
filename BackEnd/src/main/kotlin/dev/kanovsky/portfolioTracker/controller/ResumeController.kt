package dev.kanovsky.portfolioTracker.controller

import dev.kanovsky.portfolioTracker.dto.ResumeRequestDTO
import dev.kanovsky.portfolioTracker.service.PdfGeneratorService
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/resume")
class ResumeController(
    private val pdfGeneratorService: PdfGeneratorService
) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun generatePdf(
        @ModelAttribute resumeRequestDTO: ResumeRequestDTO,
        @RequestParam("profilePicture") profilePicture: MultipartFile?
    ): ResponseEntity<ByteArrayResource> {
        return try {
            pdfGeneratorService.createPdf(resumeRequestDTO, profilePicture)

            ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resume.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfGeneratorService.createPdf(resumeRequestDTO, profilePicture))
        } catch (ex: Exception) {
            //logger.error("Error reading files: ${ex.message}", ex)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ByteArrayResource("File read error".toByteArray()))
        }

    }


}
