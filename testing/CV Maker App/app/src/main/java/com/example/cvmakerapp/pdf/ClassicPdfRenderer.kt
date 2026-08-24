package com.example.cvmakerapp.pdf

import android.content.Context
import android.graphics.pdf.PdfDocument
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.CvTemplate

object ClassicPdfRenderer {

    fun render(document: PdfDocument, cvData: CvData, context: Context? = null) {
        val design = PdfDesigns.forTemplate(CvTemplate.CLASSIC)
        
        val writer = PdfPageWriter(
            document = document,
            marginLeft = design.margin,
            marginRight = design.margin,
            marginTop = design.margin,
            marginBottom = design.margin
        )

        val titlePaint = PdfTextHelper.createTextPaint(26f, design.sectionTitleColor, bold = true)
        val jobTitlePaint = PdfTextHelper.createTextPaint(13f, design.bodyColor)
        val contactLabelPaint = PdfTextHelper.createTextPaint(9f, design.bodyColor)
        val sectionTitlePaint = PdfTextHelper.createTextPaint(11f, design.sectionTitleColor, bold = true)
        val subtitlePaint = PdfTextHelper.createTextPaint(10.5f, design.subtitleColor, bold = true)
        val labelPaint = PdfTextHelper.createTextPaint(9f, design.bodyColor)
        val bodyPaint = PdfTextHelper.createTextPaint(10f, design.bodyColor)
        val dividerPaint = PdfTextHelper.createLinePaint(design.dividerColor, 0.5f)

        writer.start()

        // Header Section: Profile Image + Name/Job on same line
        val imageSize = 80f
        val headerStartY = writer.y

        // Draw Profile Image on left
        var profileImageDrawn = false
        if (!cvData.profileImageUri.isNullOrBlank() && context != null) {
            try {
                val bitmap = PdfImageHelper.loadBitmapFromUri(context, cvData.profileImageUri, 100)
                if (bitmap != null) {
                    writer.ensureSpace(imageSize + 10f)
                    PdfImageHelper.drawCircularImage(writer.canvas!!, bitmap, writer.marginLeft, headerStartY, imageSize)
                    profileImageDrawn = true
                    android.util.Log.d("ClassicPdfRenderer", "Profile image drawn successfully")
                } else {
                    android.util.Log.w("ClassicPdfRenderer", "Failed to load bitmap from URI: ${cvData.profileImageUri}")
                }
            } catch (e: Exception) {
                android.util.Log.e("ClassicPdfRenderer", "Error drawing profile image: ${e.message}", e)
                e.printStackTrace()
            }
        } else {
            android.util.Log.d("ClassicPdfRenderer", "No profile image URI or context available")
        }

        // Draw Name and Job Title to the right of image
        val textStartX = if (profileImageDrawn) {
            writer.marginLeft + imageSize + 16f
        } else {
            writer.marginLeft
        }
        
        writer.canvas!!.drawText(
            cvData.fullName.uppercase(),
            textStartX,
            headerStartY + titlePaint.textSize,
            titlePaint
        )
        
        if (cvData.jobTitle.isNotBlank()) {
            writer.canvas!!.drawText(
                cvData.jobTitle,
                textStartX,
                headerStartY + titlePaint.textSize + jobTitlePaint.textSize + 4f,
                jobTitlePaint
            )
        }

        writer.advance(imageSize + 12f)

        // Contact Information (with icons/labels)
        writer.advance(6f)
        
        if (cvData.email.isNotBlank()) {
            writer.drawTextLine("✉  ${cvData.email}", writer.marginLeft, contactLabelPaint, 10f)
        }
        if (cvData.phone.isNotBlank()) {
            writer.drawTextLine("☎  ${cvData.phone}", writer.marginLeft, contactLabelPaint, 10f)
        }
        if (cvData.location.isNotBlank()) {
            writer.drawTextLine("📍 ${cvData.location}", writer.marginLeft, contactLabelPaint, 10f)
        }
        if (cvData.linkedIn.isNotBlank()) {
            writer.drawTextLine("👤 ${cvData.linkedIn}", writer.marginLeft, contactLabelPaint, 10f)
        }
        if (cvData.website.isNotBlank()) {
            writer.drawTextLine("🌐 ${cvData.website}", writer.marginLeft, contactLabelPaint, 10f)
        }

        writer.advance(8f)
        writer.drawLine(writer.marginLeft, writer.y, writer.marginLeft + writer.contentWidth, writer.y, dividerPaint)
        writer.advance(12f)

        // Professional Summary
        if (cvData.summary.isNotBlank()) {
            writer.drawTextLine("PROFESSIONAL SUMMARY", writer.marginLeft, sectionTitlePaint, 12f)
            writer.advance(4f)
            writer.drawWrappedText(cvData.summary, writer.marginLeft, writer.contentWidth, bodyPaint, 10f)
            writer.advance(12f)
        }

        // Divider
        writer.drawLine(writer.marginLeft, writer.y, writer.marginLeft + writer.contentWidth, writer.y, dividerPaint)
        writer.advance(12f)

        // Professional Experience
        if (cvData.experiences.isNotEmpty()) {
            writer.drawTextLine("PROFESSIONAL EXPERIENCE", writer.marginLeft, sectionTitlePaint, 12f)
            writer.advance(6f)
            
            cvData.experiences.forEach { experience ->
                writer.drawTextLine(experience.role, writer.marginLeft, subtitlePaint, 11f)
                writer.drawTextLine(experience.company, writer.marginLeft, labelPaint, 9.5f)
                writer.drawTextLine(experience.dates, writer.marginLeft, labelPaint, 9.5f)
                if (experience.description.isNotBlank()) {
                    writer.drawWrappedText(
                        "• ${experience.description}",
                        writer.marginLeft,
                        writer.contentWidth,
                        bodyPaint,
                        10f
                    )
                }
                writer.advance(6f)
            }
            writer.advance(6f)
        }

        // Divider
        writer.drawLine(writer.marginLeft, writer.y, writer.marginLeft + writer.contentWidth, writer.y, dividerPaint)
        writer.advance(12f)

        // Education
        if (cvData.education.isNotEmpty()) {
            writer.drawTextLine("EDUCATION", writer.marginLeft, sectionTitlePaint, 12f)
            writer.advance(6f)
            
            cvData.education.forEach { education ->
                writer.drawTextLine(education.degree, writer.marginLeft, subtitlePaint, 11f)
                writer.drawTextLine(education.school, writer.marginLeft, labelPaint, 9.5f)
                writer.drawTextLine(education.dates, writer.marginLeft, labelPaint, 9.5f)
                writer.advance(6f)
            }
            writer.advance(6f)
        }

        // Divider
        writer.drawLine(writer.marginLeft, writer.y, writer.marginLeft + writer.contentWidth, writer.y, dividerPaint)
        writer.advance(12f)

        // Skills
        if (cvData.skills.isNotEmpty()) {
            writer.drawTextLine("SKILLS", writer.marginLeft, sectionTitlePaint, 12f)
            writer.advance(4f)
            writer.drawWrappedText(
                cvData.skills.joinToString(" • "),
                writer.marginLeft,
                writer.contentWidth,
                bodyPaint,
                10f
            )
        }

        writer.finish()
    }
}
