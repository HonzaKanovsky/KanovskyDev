package dev.kanovsky.portfolioTracker.service

import com.lowagie.text.pdf.BaseFont
import org.springframework.stereotype.Service
import org.xhtmlrenderer.pdf.ITextFontResolver
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayOutputStream

@Service
class PdfGeneratorService {

    fun generatePdfFromHtml(htmlContent: String): ByteArray {
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
}
