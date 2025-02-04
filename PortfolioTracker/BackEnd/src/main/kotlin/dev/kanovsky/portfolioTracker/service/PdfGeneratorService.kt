package dev.kanovsky.portfolioTracker.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.lowagie.text.pdf.BaseFont
import dev.kanovsky.portfolioTracker.dto.ItemSectionDTO
import dev.kanovsky.portfolioTracker.dto.ResumeRequestDTO
import dev.kanovsky.portfolioTracker.dto.SidebarSectionDTO
import org.springframework.core.io.ByteArrayResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.xhtmlrenderer.pdf.ITextFontResolver
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayOutputStream
import java.util.*

@Service
class PdfGeneratorService(
    private val objectMapper: ObjectMapper,
    private val templateEngine: TemplateEngine
) {

    fun createPdf(
        resumeRequestDTO: ResumeRequestDTO,
        profilePicture: MultipartFile?
    ): ByteArrayResource {
        val context = prepareContentForPDF(profilePicture, resumeRequestDTO)

        val htmlContent = templateEngine.process("resume", context)
        val pdfBytes = generatePdfFromHtml(htmlContent)
        val resource = ByteArrayResource(pdfBytes)

        return resource
    }

    private fun prepareContentForPDF(profilePicture: MultipartFile?, resumeRequestDTO: ResumeRequestDTO): Context {
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

        return context
    }

    private fun generatePdfFromHtml(htmlContent: String): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val renderer = ITextRenderer()

        // Register a font that supports Czech characters
        val fontPath = "/fonts/TitilliumWeb-Light.ttf" // Place this file inside "resources/fonts"
        val fontResolver = renderer.sharedContext.fontResolver as ITextFontResolver
        fontResolver.addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED)

        renderer.setDocumentFromString(htmlContent)
        renderer.layout()
        renderer.createPDF(outputStream)
        renderer.finishPDF()
        return outputStream.toByteArray()
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
