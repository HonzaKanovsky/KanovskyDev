package dev.kanovsky.portfolioTracker.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import dev.kanovsky.portfolioTracker.dto.ItemSectionDTO
import dev.kanovsky.portfolioTracker.dto.ResumeRequestDTO
import dev.kanovsky.portfolioTracker.dto.SidebarSectionDTO
import dev.kanovsky.portfolioTracker.service.PdfGeneratorService
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.util.*

@RestController
@RequestMapping("/api/resume")
class ResumeController(
    private val pdfGeneratorService: PdfGeneratorService,
    private val templateEngine: TemplateEngine,
    private val objectMapper: ObjectMapper
) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun generatePdf(
        @ModelAttribute resumeRequestDTO: ResumeRequestDTO,
        @RequestParam("profilePicture") profilePicture: MultipartFile?
    ): ResponseEntity<ByteArrayResource> {


        return try {
            val imageBase64 = profilePicture?.let { convertToBase64(it) } ?: ""

            // ✅ Properly deserialize JSON into DTOs
            val sidebarSections: List<SidebarSectionDTO> = objectMapper.readValue(
                resumeRequestDTO.sidebarItemsDTO, object : TypeReference<List<SidebarSectionDTO>>() {}
            )

            val itemSections: List<ItemSectionDTO> = objectMapper.readValue(
                resumeRequestDTO.itemSectionDTO, object : TypeReference<List<ItemSectionDTO>>() {}
            )
            val phoneIconBase64 = convertFileToBase64("/static/images/phone-icon.png")
            val emailIconBase64 = convertFileToBase64("/static/images/email-icon.png")
            val pinIconBase64 = convertFileToBase64("/static/images/pin-icon.png")
            val webIconBase64 = convertFileToBase64("/static/images/web-icon.png")

            val context = Context().apply {
                setVariable("name", resumeRequestDTO.name)
                setVariable("aboutMe", resumeRequestDTO.aboutMe)
                setVariable("imageBase64", imageBase64)

                setVariable(
                    "contact", mapOf(
                        "phoneNumber" to resumeRequestDTO.phoneNumber,
                        "email" to resumeRequestDTO.email,
                        "address" to resumeRequestDTO.address,
                        "website" to resumeRequestDTO.website
                    )
                )

                // ✅ Now Thymeleaf will correctly read structured DTOs
                setVariable("sidebarSections", sidebarSections)
                setVariable("itemSections", itemSections)


                setVariable("phoneIconBase64", phoneIconBase64)
                setVariable("emailIconBase64", emailIconBase64)
                setVariable("pinIconBase64", pinIconBase64)
                setVariable("webIconBase64", webIconBase64)

            }
            val htmlContent = templateEngine.process("resume", context)
            val pdfBytes = pdfGeneratorService.generatePdfFromHtml(htmlContent)
            val resource = ByteArrayResource(pdfBytes)

            ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resume.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource)
        } catch (ex: Exception) {
            //logger.error("Error reading files: ${ex.message}", ex)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ByteArrayResource("File read error".toByteArray()))
        }

    }

    private fun convertToBase64(file: MultipartFile): String {
        return Base64.getEncoder().encodeToString(file.bytes)
    }

    private fun convertFileToBase64(resourcePath: String): String {
        return try {
            val inputStream = javaClass.getResourceAsStream(resourcePath)
                ?: throw IllegalArgumentException("File not found: $resourcePath")
            val bytes = inputStream.readBytes()
            Base64.getEncoder().encodeToString(bytes)
        } catch (e: Exception) {
            println("Error encoding file: $resourcePath - ${e.message}")
            ""
        }
    }
}
