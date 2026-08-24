package com.example.cvmakerapp.pdf

import android.content.Context
import android.graphics.pdf.PdfDocument
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.CvTemplate

object MinimalPdfRenderer {

    fun render(document: PdfDocument, cvData: CvData, context: Context?) {
        val design = PdfDesigns.forTemplate(CvTemplate.MINIMAL)
        val writer = PdfPageWriter(
            document = document,
            marginLeft = design.margin,
            marginRight = design.margin,
            marginTop = design.margin,
            marginBottom = design.margin
        )

        val namePaint = PdfTextHelper.createTextPaint(20f, design.sectionTitleColor, bold = true)
        val jobPaint = PdfTextHelper.createTextPaint(11f, design.bodyColor)
        val contactPaint = PdfTextHelper.createTextPaint(8.5f, design.bodyColor)
        val sectionTitlePaint = PdfTextHelper.createTextPaint(9.5f, design.sectionTitleColor, bold = true)
        val rolePaint = PdfTextHelper.createTextPaint(10f, design.sectionTitleColor, bold = true)
        val bodyPaint = PdfTextHelper.createTextPaint(9.5f, design.bodyColor)
        val linePaint = PdfTextHelper.createLinePaint(design.dividerColor, 0.3f)

        writer.start()

        writer.drawTextLine(cvData.fullName.ifBlank { "Your Name" }, writer.marginLeft, namePaint, 22f)
        if (cvData.jobTitle.isNotBlank()) {
            writer.drawTextLine(cvData.jobTitle, writer.marginLeft, jobPaint, 11f)
        }

        // Profile Image (top right)
        if (!cvData.profileImageUri.isNullOrBlank() && context != null) {
            try {
                val bitmap = PdfImageHelper.loadBitmapFromUri(context, cvData.profileImageUri, 100)
                if (bitmap != null) {
                    val imageSize = 55f
                    val imageX = writer.pageWidth - writer.marginRight - imageSize - 4f
                    PdfImageHelper.drawCircularImage(writer.canvas!!, bitmap, imageX, writer.marginTop, imageSize)
                    android.util.Log.d("MinimalPdfRenderer", "Profile image drawn successfully")
                } else {
                    android.util.Log.w("MinimalPdfRenderer", "Failed to load bitmap from URI: ${cvData.profileImageUri}")
                }
            } catch (e: Exception) {
                android.util.Log.e("MinimalPdfRenderer", "Error drawing profile image: ${e.message}", e)
                e.printStackTrace()
            }
        } else {
            android.util.Log.d("MinimalPdfRenderer", "No profile image URI or context available")
        }

        writer.advance(8f)

        // Contact information
        val contactItems = listOfNotNull(
            cvData.email.takeIf { it.isNotBlank() },
            cvData.phone.takeIf { it.isNotBlank() },
            cvData.location.takeIf { it.isNotBlank() },
            cvData.linkedIn.takeIf { it.isNotBlank() },
            cvData.website.takeIf { it.isNotBlank() }
        )
        if (contactItems.isNotEmpty()) {
            writer.drawWrappedText(
                contactItems.joinToString("  |  "),
                writer.marginLeft,
                writer.contentWidth,
                contactPaint,
                10f
            )
        }

        writer.advance(8f)
        writer.drawLine(writer.marginLeft, writer.y, writer.marginLeft + writer.contentWidth, writer.y, linePaint)
        writer.advance(12f)

        // Summary
        if (cvData.summary.isNotBlank()) {
            writer.drawTextLine("summary", writer.marginLeft, sectionTitlePaint, 10f)
            writer.advance(4f)
            writer.drawWrappedText(cvData.summary, writer.marginLeft, writer.contentWidth, bodyPaint, 10f)
            writer.advance(8f)
            writer.drawLine(writer.marginLeft, writer.y, writer.marginLeft + writer.contentWidth, writer.y, linePaint)
            writer.advance(12f)
        }

        // Experience
        if (cvData.experiences.isNotEmpty()) {
            writer.drawTextLine("experience", writer.marginLeft, sectionTitlePaint, 10f)
            writer.advance(4f)
            cvData.experiences.forEach { exp ->
                writer.drawTextLine(exp.role, writer.marginLeft, rolePaint, 10.5f)
                writer.drawTextLine(exp.company, writer.marginLeft, bodyPaint, 9f)
                writer.drawTextLine(exp.dates, writer.marginLeft, contactPaint, 8.5f)
                if (exp.description.isNotBlank()) {
                    writer.drawWrappedText(exp.description, writer.marginLeft, writer.contentWidth, bodyPaint, 9.5f)
                }
                writer.advance(5f)
            }
            writer.advance(8f)
            writer.drawLine(writer.marginLeft, writer.y, writer.marginLeft + writer.contentWidth, writer.y, linePaint)
            writer.advance(12f)
        }

        // Education
        if (cvData.education.isNotEmpty()) {
            writer.drawTextLine("education", writer.marginLeft, sectionTitlePaint, 10f)
            writer.advance(4f)
            cvData.education.forEach { edu ->
                writer.drawTextLine(edu.degree, writer.marginLeft, rolePaint, 10.5f)
                writer.drawTextLine(edu.school, writer.marginLeft, bodyPaint, 9f)
                writer.drawTextLine(edu.dates, writer.marginLeft, contactPaint, 8.5f)
                writer.advance(5f)
            }
            writer.advance(8f)
            writer.drawLine(writer.marginLeft, writer.y, writer.marginLeft + writer.contentWidth, writer.y, linePaint)
            writer.advance(12f)
        }

        // Skills
        if (cvData.skills.isNotEmpty()) {
            writer.drawTextLine("skills", writer.marginLeft, sectionTitlePaint, 10f)
            writer.advance(4f)
            writer.drawWrappedText(
                cvData.skills.joinToString(", "),
                writer.marginLeft,
                writer.contentWidth,
                bodyPaint,
                10f
            )
        }

        writer.finish()
    }
}
