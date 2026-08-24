package com.example.cvmakerapp.pdf

import android.content.Context
import android.graphics.pdf.PdfDocument
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.CvTemplate

object ProfessionalPdfRenderer {

    fun render(document: PdfDocument, cvData: CvData, context: Context?) {
        val design = PdfDesigns.forTemplate(CvTemplate.PROFESSIONAL)

        val writer = PdfPageWriter(
            document = document,
            marginLeft = design.margin,
            marginRight = design.margin,
            marginTop = design.margin + 10f,
            marginBottom = design.margin,
            drawPageBackground = { canvas, width, _, pageIndex ->
                if (pageIndex == 1) {
                    design.headerBackground?.let { color ->
                        canvas.drawRect(0f, 0f, width.toFloat(), 6f, PdfTextHelper.createFillPaint(color))
                    }
                }
            }
        )

        val namePaint = PdfTextHelper.createTextPaint(19f, design.sectionTitleColor, bold = true)
        val jobPaint = PdfTextHelper.createTextPaint(11f, design.subtitleColor)
        val contactPaint = PdfTextHelper.createTextPaint(8.5f, design.bodyColor)
        val sectionTitlePaint = PdfTextHelper.createTextPaint(10f, design.accentColor, bold = true)
        val rolePaint = PdfTextHelper.createTextPaint(10.5f, design.sectionTitleColor, bold = true)
        val companyPaint = PdfTextHelper.createTextPaint(9f, design.subtitleColor, bold = true)
        val bodyPaint = PdfTextHelper.createTextPaint(9f, design.bodyColor)
        val accentBarPaint = PdfTextHelper.createFillPaint(design.accentColor)
        val dividerPaint = PdfTextHelper.createLinePaint(design.dividerColor, 0.5f)

        writer.start()

        // Profile Image (top right)
        if (!cvData.profileImageUri.isNullOrBlank() && context != null) {
            try {
                val bitmap = PdfImageHelper.loadBitmapFromUri(context, cvData.profileImageUri, 90)
                if (bitmap != null) {
                    val imageSize = 65f
                    val imageX = writer.pageWidth - writer.marginRight - imageSize - 4f
                    PdfImageHelper.drawCircularImage(writer.canvas!!, bitmap, imageX, writer.marginTop, imageSize)
                    android.util.Log.d("ProfessionalPdfRenderer", "Profile image drawn successfully")
                } else {
                    android.util.Log.w("ProfessionalPdfRenderer", "Failed to load bitmap from URI: ${cvData.profileImageUri}")
                }
            } catch (e: Exception) {
                android.util.Log.e("ProfessionalPdfRenderer", "Error drawing profile image: ${e.message}", e)
                e.printStackTrace()
            }
        } else {
            android.util.Log.d("ProfessionalPdfRenderer", "No profile image URI or context available")
        }

        // Name and Job Title
        writer.drawTextLine(cvData.fullName.uppercase(), writer.marginLeft, namePaint, 20f)
        if (cvData.jobTitle.isNotBlank()) {
            writer.drawTextLine(cvData.jobTitle, writer.marginLeft, jobPaint, 11.5f)
        }

        writer.advance(6f)

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

        writer.advance(10f)
        writer.drawLine(writer.marginLeft, writer.y, writer.marginLeft + writer.contentWidth, writer.y, dividerPaint)
        writer.advance(12f)

        // Summary
        if (cvData.summary.isNotBlank()) {
            writer.canvas!!.drawRect(writer.marginLeft, writer.y, writer.marginLeft + 3f, writer.y + 12f, accentBarPaint)
            writer.drawTextLine("PROFESSIONAL SUMMARY", writer.marginLeft + 10f, sectionTitlePaint, 11f)
            writer.advance(6f)
            writer.drawWrappedText(cvData.summary, writer.marginLeft, writer.contentWidth, bodyPaint, 9.5f)
            writer.advance(10f)
        }

        // Experience
        if (cvData.experiences.isNotEmpty()) {
            writer.canvas!!.drawRect(writer.marginLeft, writer.y, writer.marginLeft + 3f, writer.y + 12f, accentBarPaint)
            writer.drawTextLine("PROFESSIONAL EXPERIENCE", writer.marginLeft + 10f, sectionTitlePaint, 11f)
            writer.advance(6f)
            cvData.experiences.forEach { exp ->
                writer.drawTextLine(exp.role, writer.marginLeft, rolePaint, 11f)
                writer.drawTextLine(exp.company, writer.marginLeft, companyPaint, 9.5f)
                writer.drawTextLine(exp.dates, writer.marginLeft, contactPaint, 8.5f)
                if (exp.description.isNotBlank()) {
                    writer.drawWrappedText("• ${exp.description}", writer.marginLeft + 4f, writer.contentWidth - 8f, bodyPaint, 9.5f)
                }
                writer.advance(4f)
            }
            writer.advance(10f)
        }

        // Education
        if (cvData.education.isNotEmpty()) {
            writer.canvas!!.drawRect(writer.marginLeft, writer.y, writer.marginLeft + 3f, writer.y + 12f, accentBarPaint)
            writer.drawTextLine("EDUCATION", writer.marginLeft + 10f, sectionTitlePaint, 11f)
            writer.advance(6f)
            cvData.education.forEach { edu ->
                writer.drawTextLine(edu.degree, writer.marginLeft, rolePaint, 11f)
                writer.drawTextLine(edu.school, writer.marginLeft, bodyPaint, 9f)
                writer.drawTextLine(edu.dates, writer.marginLeft, contactPaint, 8.5f)
                writer.advance(4f)
            }
            writer.advance(10f)
        }

        // Skills
        if (cvData.skills.isNotEmpty()) {
            writer.canvas!!.drawRect(writer.marginLeft, writer.y, writer.marginLeft + 3f, writer.y + 12f, accentBarPaint)
            writer.drawTextLine("CORE SKILLS", writer.marginLeft + 10f, sectionTitlePaint, 11f)
            writer.advance(4f)
            cvData.skills.forEach { skill ->
                writer.drawTextLine("•  $skill", writer.marginLeft + 4f, bodyPaint, 9.5f)
            }
        }

        writer.finish()
    }
}
