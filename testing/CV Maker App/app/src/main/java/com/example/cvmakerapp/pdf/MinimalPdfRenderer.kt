package com.example.cvmakerapp.pdf

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.CvTemplate

object MinimalPdfRenderer {

    fun render(document: PdfDocument, cvData: CvData, context: Context?) {
        val design = PdfDesigns.forTemplate(CvTemplate.MINIMAL)
        
        val margin = design.margin
        val writer = PdfPageWriter(
            document = document,
            marginLeft = margin,
            marginRight = margin,
            marginTop = margin,
            marginBottom = margin
        )

        // Standardized Typography from Minimal preview (Light weights approximated)
        val namePaint = PdfTextHelper.createTextPaint(32f, design.headerTextColor, bold = false)
        val jobPaint = PdfTextHelper.createTextPaint(22f, design.bodyColor, bold = false)
        val contactPaint = PdfTextHelper.createTextPaint(11f, design.bodyColor, bold = false)
        val sectionTitlePaint = PdfTextHelper.createTextPaint(14f, design.accentColor, bold = true)
        val rolePaint = PdfTextHelper.createTextPaint(14f, 0xFF0F172A.toInt(), bold = true) // OnBackground
        val bodyPaint = PdfTextHelper.createTextPaint(14f, design.bodyColor)
        val datePaint = PdfTextHelper.createTextPaint(11f, design.bodyColor)
        val linePaint = PdfTextHelper.createLinePaint(design.dividerColor, 0.5f)

        writer.start()

        // ---------------------------------------------------------
        // HEADER: NAME & IMAGE
        // ---------------------------------------------------------
        val headerStartY = writer.y
        val imageSize = design.profileImageSize
        var profileImageDrawn = false
        if (!cvData.profileImageUri.isNullOrBlank() && context != null) {
            try {
                val bitmap = PdfImageHelper.loadBitmapFromUri(context, cvData.profileImageUri, 120)
                if (bitmap != null) {
                    val imageX = writer.pageWidth - writer.marginRight - imageSize
                    PdfImageHelper.drawCircularImage(writer.canvas!!, bitmap, imageX, headerStartY, imageSize)
                    profileImageDrawn = true
                }
            } catch (_: Exception) { }
        }

        val textStartX = writer.marginLeft
        writer.canvas!!.drawText(cvData.fullName, textStartX, headerStartY + 28f, namePaint)
        
        if (cvData.jobTitle.isNotBlank()) {
            writer.canvas!!.drawText(cvData.jobTitle, textStartX, headerStartY + 56f, jobPaint)
        }

        writer.y = Math.max(headerStartY + imageSize, headerStartY + 65f) + 20f

        // ---------------------------------------------------------
        // CONTACT INFO (Inline rows like preview)
        // ---------------------------------------------------------
        val contacts = listOfNotNull(
            cvData.email.takeIf { it.isNotBlank() },
            cvData.phone.takeIf { it.isNotBlank() },
            cvData.location.takeIf { it.isNotBlank() },
            cvData.linkedIn.takeIf { it.isNotBlank() },
            cvData.website.takeIf { it.isNotBlank() }
        )

        if (contacts.isNotEmpty()) {
            contacts.chunked(3).forEach { row ->
                var currentX = writer.marginLeft
                row.forEach { contact ->
                    writer.canvas!!.drawText(contact, currentX, writer.y + 11f, contactPaint)
                    currentX += contactPaint.measureText(contact) + 18f
                }
                writer.advance(18f)
            }
            writer.advance(10f)
        }

        writer.drawLine(writer.marginLeft, writer.y, writer.marginLeft + writer.contentWidth, writer.y, linePaint)
        writer.advance(20f)

        // ---------------------------------------------------------
        // MINIMAL SECTIONS
        // ---------------------------------------------------------
        fun drawMinimalSection(title: String, block: () -> Unit) {
            writer.ensureSpace(45f)
            writer.canvas!!.drawText(title.uppercase(), writer.marginLeft, writer.y + 12f, sectionTitlePaint)
            writer.advance(26f)
            block()
            writer.advance(12f)
            writer.drawLine(writer.marginLeft, writer.y, writer.marginLeft + writer.contentWidth, writer.y, linePaint)
            writer.advance(22f)
        }

        if (cvData.summary.isNotBlank()) {
            drawMinimalSection("Summary") {
                writer.drawWrappedText(cvData.summary, writer.marginLeft, writer.contentWidth, bodyPaint)
            }
        }

        if (cvData.experiences.isNotEmpty()) {
            drawMinimalSection("Experience") {
                cvData.experiences.forEach { exp ->
                    writer.ensureSpace(45f)
                    writer.canvas!!.drawText(exp.role, writer.marginLeft, writer.y + 11f, rolePaint)
                    val dWidth = datePaint.measureText(exp.dates)
                    writer.canvas!!.drawText(exp.dates, writer.marginLeft + writer.contentWidth - dWidth, writer.y + 11f, datePaint)
                    writer.advance(16f)
                    
                    writer.canvas!!.drawText(exp.company, writer.marginLeft, writer.y + 11f, bodyPaint)
                    writer.advance(20f)
                    
                    if (exp.description.isNotBlank()) {
                        writer.drawWrappedText(exp.description, writer.marginLeft, writer.contentWidth, bodyPaint)
                    }
                    writer.advance(12f)
                }
            }
        }

        if (cvData.education.isNotEmpty()) {
            drawMinimalSection("Education") {
                cvData.education.forEach { edu ->
                    writer.ensureSpace(40f)
                    writer.canvas!!.drawText(edu.degree, writer.marginLeft, writer.y + 11f, rolePaint)
                    val dWidth = datePaint.measureText(edu.dates)
                    writer.canvas!!.drawText(edu.dates, writer.marginLeft + writer.contentWidth - dWidth, writer.y + 11f, datePaint)
                    writer.advance(16f)
                    writer.canvas!!.drawText(edu.school, writer.marginLeft, writer.y + 11f, bodyPaint)
                    writer.advance(10f)
                }
            }
        }

        if (cvData.skills.isNotEmpty()) {
            drawMinimalSection("Skills") {
                val skillText = cvData.skills.joinToString("   ·   ")
                writer.drawWrappedText(skillText, writer.marginLeft, writer.contentWidth, bodyPaint)
            }
        }

        writer.finish()
    }

    private fun PdfPageWriter.drawWrappedText(text: String, x: Float, maxWidth: Float, paint: Paint, indent: Float = 0f) {
        val lines = PdfTextHelper.wrapText(text, paint, (maxWidth - indent).toInt())
        lines.forEach { line ->
            ensureSpace(paint.textSize + 4f)
            canvas?.drawText(line, x + indent, y + paint.textSize * 0.8f, paint)
            y += paint.textSize + 4f
        }
    }
}
