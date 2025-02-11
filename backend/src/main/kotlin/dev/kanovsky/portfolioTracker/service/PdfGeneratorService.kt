package dev.kanovsky.portfolioTracker.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.lowagie.text.pdf.BaseFont
import dev.kanovsky.portfolioTracker.dto.ResumeRequestDTO
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.util.*

/**
 * Service for generating PDFs from HTML templates using Thymeleaf and iTextRenderer.
 **/
@Service
class PdfGeneratorService(
    private val objectMapper: ObjectMapper,
    private val templateEngine: TemplateEngine
) {

    /**
     * Generates a PDF from the provided resume request and profile picture.
     * @param resumeRequestString JSON string containing resume data.
     * @param profilePicture Optional profile picture file.
     * @return A ByteArrayResource containing the generated PDF.
     **/
    fun createPdf(
        resumeRequestString: String,
        profilePicture: MultipartFile?
    ): ByteArrayResource {
        // Convert JSON String to ResumeRequestDTO object
        val resumeDtoObj = objectMapper.readValue(resumeRequestString, ResumeRequestDTO::class.java)

        // Prepare content for the PDF using Thymeleaf
        val context = prepareContentForPDF(profilePicture, resumeDtoObj)
        val htmlContent = templateEngine.process("resume", context)

        // Convert HTML to PDF
        val pdfBytes = generatePdfFromHtml(htmlContent)
        return ByteArrayResource(pdfBytes)
    }

    /**
     * Prepares content for the Thymeleaf template using resume data.
     * @param profilePicture Optional profile picture.
     * @param resumeRequestDTO Resume data object.
     * @return A Thymeleaf Context containing all necessary variables.
     **/
    private fun prepareContentForPDF(profilePicture: MultipartFile?, resumeRequestDTO: ResumeRequestDTO): Context {
        // Convert profile picture to Base64 if provided
        val imageBase64 = profilePicture?.let { convertToBase64(it) } ?: ""

        // Convert icons to Base64 for embedding in the PDF
        val phoneIconBase64 = convertFileToBase64("/static/images/phone-icon.png")
        val emailIconBase64 = convertFileToBase64("/static/images/email-icon.png")
        val pinIconBase64 = convertFileToBase64("/static/images/pin-icon.png")
        val webIconBase64 = convertFileToBase64("/static/images/web-icon.png")

        // Populate Thymeleaf context with resume data
        return Context().apply {
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
            setVariable("sidebarSections", resumeRequestDTO.sidebarItemsDTO)
            setVariable("itemSections", resumeRequestDTO.itemSectionDTO)
            setVariable("phoneIconBase64", phoneIconBase64)
            setVariable("emailIconBase64", emailIconBase64)
            setVariable("pinIconBase64", pinIconBase64)
            setVariable("webIconBase64", webIconBase64)

        }
    }

    /**
     * Converts an HTML string to a PDF byte array using iTextRenderer.
     * @param htmlContent The HTML content to be converted into PDF.
     * @return A byte array containing the generated PDF.
     **/
    private fun generatePdfFromHtml(htmlContent: String): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val renderer = ITextRenderer()

        // Load and register a custom font for PDF rendering
        val fontResource = ClassPathResource("fonts/TitilliumWeb-Light.ttf")
        val fontInputStream: InputStream = fontResource.inputStream
        val tempFontFile = File.createTempFile("TitilliumWeb-Light", ".ttf")

        // Copy the font to a temporary file
        tempFontFile.outputStream().use { output -> fontInputStream.copyTo(output) }

        // Register the font with iTextRenderer
        renderer.fontResolver.addFont(tempFontFile.absolutePath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED)

        // Convert HTML to PDF
        renderer.setDocumentFromString(htmlContent)
        renderer.layout()
        renderer.createPDF(outputStream)
        renderer.finishPDF()

        return outputStream.toByteArray()
    }

    /**
     * Converts a MultipartFile to a Base64 string.
     * @param file The MultipartFile to be converted.
     * @return A Base64-encoded string representation of the file.
     **/
    private fun convertToBase64(file: MultipartFile): String {
        return Base64.getEncoder().encodeToString(file.bytes)
    }

    /**
     * Converts a file from classpath to a Base64 string.
     * @param resourcePath The path to the file in the classpath.
     * @return A Base64-encoded string of the file, or an empty string if an error occurs.
     **/
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
