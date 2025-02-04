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

@Service
class PdfGeneratorService(
    private val objectMapper: ObjectMapper,
    private val templateEngine: TemplateEngine
) {

    fun createPdf(
        resumeRequestString: String,
        profilePicture: MultipartFile?
    ): ByteArrayResource {
        // Convert JSON String to ResumeRequestDTO object
        val objectMapper = ObjectMapper()
        val resumeDtoObj = objectMapper.readValue(resumeRequestString, ResumeRequestDTO::class.java)

        val context = prepareContentForPDF(profilePicture, resumeDtoObj)

        val htmlContent = templateEngine.process("resume", context)
        val pdfBytes = generatePdfFromHtml(htmlContent)
        val resource = ByteArrayResource(pdfBytes)

        return resource
    }

    private fun prepareContentForPDF(profilePicture: MultipartFile?, resumeRequestDTO: ResumeRequestDTO): Context {
        val imageBase64 = profilePicture?.let { convertToBase64(it) } ?: ""

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
            setVariable("sidebarSections", resumeRequestDTO.sidebarItemsDTO)
            setVariable("itemSections", resumeRequestDTO.itemSectionDTO)


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

        // Load font from classpath
        val fontResource = ClassPathResource("fonts/TitilliumWeb-Light.ttf")
        val fontInputStream: InputStream = fontResource.inputStream
        val tempFontFile = File.createTempFile("TitilliumWeb-Light", ".ttf")

        // Copy font to a temporary file
        tempFontFile.outputStream().use { output -> fontInputStream.copyTo(output) }

        // Register the font
        renderer.fontResolver.addFont(tempFontFile.absolutePath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED)

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
