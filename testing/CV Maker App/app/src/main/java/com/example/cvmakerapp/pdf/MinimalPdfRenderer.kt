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
            marginBottom = design.margin,
            drawPageBackground = { canvas, width, height, _ ->
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(),
                    PdfTextHelper.createFillPaint(design.pageBackground))
            }
        )

        val namePaint = PdfTextHelper.createTextPaint(22f, design.sectionTitleColor, bold = false)
        val jobPaint = PdfTextHelper.createTextPaint(11f, design.subtitleColor)
        val contactPaint = PdfTextHelper.createTextPaint(8.5f, design.bodyColor)
        val sectionTitlePaint = PdfTextHelper.createTextPaint(9.5f, design.sectionTitleColor, bold = false)
        val rolePaint = PdfTextHelper.createTextPaint(10.5f, design.sectionTitleColor, bold = true)
        val bodyPaint = PdfTextHelper.createTextPaint(9.5f, design.bodyColor)
        val linePaint = PdfTextHelper.createLinePaint(design.dividerColor, 0.25f)
        val accentPaint = PdfTextHelper.createLinePaint(design.accentColor, 0.4f)
        accentPaint // avoid unused warning

        writer.start()

        writer.drawTextLine(cvData.fullName.ifBlank { "Your Name" }, writer.marginLeft, namePaint, 24f)
        if (cvData.jobTitle.isNotBlank()) {
            writer.drawTextLine(cvData.jobTitle, writer.marginLeft, jobPaint, 12f)
        }

        if (!cvData.profileImageUri.isNullOrBlank() && context != null) {
            try {
                val bitmap = PdfImageHelper.loadBitmapFromUri(context, cvData.profileImageUri, 100)
                if (bitmap != null) {
                    val imageSize = 52f
                    val imageX = writer.pageWidth - writer.marginRight - imageSize - 4f
                    PdfImageHelper.drawCircularImage(writer.canvas!!, bitmap, imageX, writer.marginTop, imageSize)
                }
            } catch (_: Exception) { }
        }

        writer.advance(10f)

        val contactItems = listOfNotNull(
            cvData.email.takeIf { it.isNotBlank() },
            cvData.phone.takeIf { it.isNotBlank() },
            cvData.location.takeIf { it.isNotBlank() },
            cvData.linkedIn.takeIf { it.isNotBlank() },
            cvData.website.takeIf { it.isNotBlank() }
        )
        if (contactItems.isNotEmpty()) {
            writer.drawWrappedText(
                contactItems.joinToString("   •   "),
                writer.marginLeft,
                writer.contentWidth,
                contactPaint,
                11f
            )
        }

        writer.advance(10f)
        writer.drawLine(writer.marginLeft, writer.y, writer.marginLeft + writer.contentWidth, writer.y, linePaint)
        writer.advance(14f)

        fun drawMinimalSection(title: String, block: () -> Unit) {
            val yTitle = writer.y
            writer.canvas!!.drawText(title.lowercase(), writer.marginLeft, yTitle + sectionTitlePaint.textSize * 0.8f, sectionTitlePaint)
            writer.advance(14f)
            val yContentStart = writer.y
            block()
            writer.drawLine(writer.marginLeft, writer.y + 2f, writer.marginLeft + writer.contentWidth, writer.y + 2f, linePaint)
            writer.advance(14f)
        }

        if (cvData.summary.isNotBlank()) {
            drawMinimalSection("summary") {
                writer.drawWrappedText(cvData.summary, writer.marginLeft, writer.contentWidth, bodyPaint, 11f)
            }
        }

        if (cvData.experiences.isNotEmpty()) {
            drawMinimalSection("experience") {
                cvData.experiences.forEach { exp ->
                    writer.drawTextLine(exp.role, writer.marginLeft, rolePaint, 11.5f)
                    writer.drawTextLine(exp.company, writer.marginLeft, bodyPaint, 9.5f)
                    if (exp.dates.isNotBlank()) {
                        writer.drawTextLine(exp.dates, writer.marginLeft, contactPaint, 9f)
                    }
                    if (exp.description.isNotBlank()) {
                        writer.drawWrappedText(exp.description, writer.marginLeft, writer.contentWidth, bodyPaint, 10.5f)
                    }
                    writer.advance(6f)
                }
            }
        }

        if (cvData.education.isNotEmpty()) {
            drawMinimalSection("education") {
                cvData.education.forEach { edu ->
                    writer.drawTextLine(edu.degree, writer.marginLeft, rolePaint, 11.5f)
                    writer.drawTextLine(edu.school, writer.marginLeft, bodyPaint, 9.5f)
                    if (edu.dates.isNotBlank()) writer.drawTextLine(edu.dates, writer.marginLeft, contactPaint, 9f)
                    writer.advance(6f)
                }
            }
        }

        if (cvData.skills.isNotEmpty()) {
            drawMinimalSection("skills") {
                writer.drawWrappedText(
                    cvData.skills.joinToString("   •   "),
                    writer.marginLeft,
                    writer.contentWidth,
                    bodyPaint,
                    11f
                )
            }
        }

        writer.finish()
    }
}
