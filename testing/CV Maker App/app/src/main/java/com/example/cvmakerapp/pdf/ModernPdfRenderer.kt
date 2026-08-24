package com.example.cvmakerapp.pdf

import android.content.Context
import android.graphics.pdf.PdfDocument
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.CvTemplate

object ModernPdfRenderer {

    fun render(document: PdfDocument, cvData: CvData, context: Context?) {
        val design = PdfDesigns.forTemplate(CvTemplate.MODERN)
        val headerHeight = 100f

        val writer = PdfPageWriter(
            document = document,
            marginLeft = design.margin,
            marginRight = design.margin,
            marginTop = design.margin,
            marginBottom = design.margin,
            drawPageBackground = { canvas, width, height, pageIndex ->
                design.headerBackground?.let { headerColor ->
                    val fill = PdfTextHelper.createFillPaint(headerColor)
                    canvas.drawRect(0f, 0f, width.toFloat(), headerHeight, fill)
                }
            }
        )

        val headerTitlePaint = PdfTextHelper.createTextPaint(20f, design.headerTextColor, bold = true)
        val headerSubtitlePaint = PdfTextHelper.createTextPaint(11f, design.headerTextColor)
        val contactPaint = PdfTextHelper.createTextPaint(8f, design.bodyColor)
        val sectionTitlePaint = PdfTextHelper.createTextPaint(10.5f, design.sectionTitleColor, bold = true)
        val subtitlePaint = PdfTextHelper.createTextPaint(10f, design.subtitleColor, bold = true)
        val bodyPaint = PdfTextHelper.createTextPaint(9.5f, design.bodyColor)
        val dividerPaint = PdfTextHelper.createLinePaint(design.accentColor, 0.5f)

        writer.start()

        val headerX = writer.marginLeft
        var headerY = 20f

        // Profile Image in header
        if (!cvData.profileImageUri.isNullOrBlank() && context != null) {
            try {
                val bitmap = PdfImageHelper.loadBitmapFromUri(context, cvData.profileImageUri, 90)
                if (bitmap != null) {
                    PdfImageHelper.drawCircularImage(writer.canvas!!, bitmap, headerX, headerY, 60f)
                    android.util.Log.d("ModernPdfRenderer", "Profile image drawn successfully")
                } else {
                    android.util.Log.w("ModernPdfRenderer", "Failed to load bitmap from URI: ${cvData.profileImageUri}")
                }
            } catch (e: Exception) {
                android.util.Log.e("ModernPdfRenderer", "Error drawing profile image: ${e.message}", e)
                e.printStackTrace()
            }
        } else {
            android.util.Log.d("ModernPdfRenderer", "No profile image URI or context available")
        }

        val nameX = headerX + 70f
        writer.canvas!!.drawText(cvData.fullName.ifBlank { "Your Name" }, nameX, headerY + 30f, headerTitlePaint)
        if (cvData.jobTitle.isNotBlank()) {
            writer.canvas!!.drawText(cvData.jobTitle, nameX, headerY + 50f, headerSubtitlePaint)
        }

        writer.setY(headerHeight + 12f)

        // Contact information
        listOfNotNull(
            cvData.email.takeIf { it.isNotBlank() },
            cvData.phone.takeIf { it.isNotBlank() },
            cvData.location.takeIf { it.isNotBlank() },
            cvData.linkedIn.takeIf { it.isNotBlank() },
            cvData.website.takeIf { it.isNotBlank() }
        ).forEach { line ->
            writer.drawTextLine(line, writer.marginLeft, contactPaint, 9f)
        }

        writer.advance(8f)

        // Summary
        if (cvData.summary.isNotBlank()) {
            writer.drawTextLine("SUMMARY", writer.marginLeft, sectionTitlePaint, 11f)
            writer.drawWrappedText(cvData.summary, writer.marginLeft, writer.contentWidth, bodyPaint, 9.5f)
            writer.advance(10f)
        }

        // Experience
        if (cvData.experiences.isNotEmpty()) {
            writer.drawTextLine("EXPERIENCE", writer.marginLeft, sectionTitlePaint, 11f)
            cvData.experiences.forEach { exp ->
                writer.drawTextLine(exp.role, writer.marginLeft, subtitlePaint, 10.5f)
                writer.drawTextLine(exp.company, writer.marginLeft, bodyPaint, 9f)
                writer.drawTextLine(exp.dates, writer.marginLeft, contactPaint, 8.5f)
                if (exp.description.isNotBlank()) {
                    writer.drawWrappedText("• ${exp.description}", writer.marginLeft + 2f, writer.contentWidth - 4f, bodyPaint, 9.5f)
                }
                writer.advance(4f)
            }
            writer.advance(8f)
        }

        // Education
        if (cvData.education.isNotEmpty()) {
            writer.drawTextLine("EDUCATION", writer.marginLeft, sectionTitlePaint, 11f)
            cvData.education.forEach { edu ->
                writer.drawTextLine(edu.degree, writer.marginLeft, subtitlePaint, 10.5f)
                writer.drawTextLine(edu.school, writer.marginLeft, bodyPaint, 9f)
                writer.drawTextLine(edu.dates, writer.marginLeft, contactPaint, 8.5f)
                writer.advance(4f)
            }
            writer.advance(8f)
        }

        // Skills
        if (cvData.skills.isNotEmpty()) {
            writer.drawTextLine("SKILLS", writer.marginLeft, sectionTitlePaint, 11f)
            writer.drawWrappedText(cvData.skills.joinToString(" • "), writer.marginLeft, writer.contentWidth, bodyPaint, 9.5f)
        }

        writer.finish()
    }
}
