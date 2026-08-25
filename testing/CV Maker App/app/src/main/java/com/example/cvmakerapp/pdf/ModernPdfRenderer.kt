package com.example.cvmakerapp.pdf

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.CvTemplate

object ModernPdfRenderer {

    fun render(
        document: PdfDocument,
        cvData: CvData,
        context: Context?
    ) {
        val design = PdfDesigns.forTemplate(CvTemplate.MODERN)
        
        val margin = design.margin
        val writer = PdfPageWriter(
            document = document,
            marginLeft = margin,
            marginRight = margin,
            marginTop = 0f, 
            marginBottom = 30f
        )

        // Typography Standardized from preview
        val namePaint = PdfTextHelper.createTextPaint(28f, 0xFF0F172A.toInt(), bold = true) // OnBackground
        val jobPaint = PdfTextHelper.createTextPaint(20f, design.accentColor, bold = true)
        val sectionTitlePaint = PdfTextHelper.createTextPaint(14f, design.accentColor, bold = true)
        val subtitlePaint = PdfTextHelper.createTextPaint(16f, 0xFF0F172A.toInt(), bold = true) // Role/Degree
        val bodyPaint = PdfTextHelper.createTextPaint(14f, design.bodyColor)
        val datePaint = PdfTextHelper.createTextPaint(12f, design.accentColor, bold = true)
        
        val headerBgPaint = PdfTextHelper.createFillPaint(design.headerBackground ?: design.accentColor)
        val cardBgPaint = PdfTextHelper.createFillPaint(0xFFFFFFFF.toInt())
        val pageBgPaint = PdfTextHelper.createFillPaint(design.pageBackground)
        val accentBarPaint = PdfTextHelper.createFillPaint(design.accentColor)
        val softAccentPaint = PdfTextHelper.createFillPaint(design.accentColorSoft)

        writer.start()

        // Page background
        writer.canvas!!.drawRect(0f, 0f, writer.pageWidth.toFloat(), writer.pageHeight.toFloat(), pageBgPaint)

        // ---------------------------------------------------------
        // HEADER: TEAL BOX + WHITE CARD
        // ---------------------------------------------------------
        val headerBoxH = 180f
        writer.canvas!!.drawRect(0f, 0f, writer.pageWidth.toFloat(), headerBoxH, headerBgPaint)
        
        val headerCardTop = 40f
        val headerCardH = 100f
        writer.canvas!!.drawRoundRect(margin, headerCardTop, writer.pageWidth - margin, headerCardTop + headerCardH, 14f, 14f, cardBgPaint)

        val imageSize = design.profileImageSize
        val imageX = margin + 20f
        val imageY = headerCardTop + (headerCardH - imageSize) / 2f
        var profileImageDrawn = false
        if (!cvData.profileImageUri.isNullOrBlank() && context != null) {
            try {
                val bitmap = PdfImageHelper.loadBitmapFromUri(context, cvData.profileImageUri, 150)
                if (bitmap != null) {
                    PdfImageHelper.drawCircularImage(writer.canvas!!, bitmap, imageX, imageY, imageSize)
                    profileImageDrawn = true
                }
            } catch (_: Exception) { }
        }

        val textStartX = if (profileImageDrawn) imageX + imageSize + 20f else margin + 25f
        val nameBaselineY = headerCardTop + 45f
        writer.canvas!!.drawText(cvData.fullName, textStartX, nameBaselineY, namePaint)
        
        if (cvData.jobTitle.isNotBlank()) {
            val yJob = nameBaselineY + 28f
            writer.canvas!!.drawCircle(textStartX + 4f, yJob - 6f, 3f, accentBarPaint)
            writer.canvas!!.drawText(cvData.jobTitle, textStartX + 14f, yJob, jobPaint)
        }

        writer.y = headerCardTop + headerCardH + 18f

        // ---------------------------------------------------------
        // CONTACT CARD
        // ---------------------------------------------------------
        val contactItems = listOfNotNull(
            cvData.email.takeIf { it.isNotBlank() }?.let { "✉" to it },
            cvData.phone.takeIf { it.isNotBlank() }?.let { "☎" to it },
            cvData.location.takeIf { it.isNotBlank() }?.let { "📍" to it },
            cvData.linkedIn.takeIf { it.isNotBlank() }?.let { "👤" to it },
            cvData.website.takeIf { it.isNotBlank() }?.let { "🌐" to it }
        )

        if (contactItems.isNotEmpty()) {
            val itemsPerRow = 2
            val colW = writer.contentWidth / itemsPerRow
            val rows = (contactItems.size + itemsPerRow - 1) / itemsPerRow
            val boxH = rows * 28f + 16f
            
            writer.canvas!!.drawRoundRect(margin, writer.y, writer.pageWidth - margin, writer.y + boxH, 14f, 14f, cardBgPaint)
            writer.advance(16f)

            contactItems.chunked(itemsPerRow).forEach { row ->
                row.forEachIndexed { i, (icon, value) ->
                    val x = margin + 20f + (i * colW)
                    // Icon Box
                    writer.canvas!!.drawRoundRect(x, writer.y, x + 24f, writer.y + 24f, 6f, 6f, softAccentPaint)
                    writer.canvas!!.drawText(icon, x + 6f, writer.y + 17f, PdfTextHelper.createTextPaint(12f, design.accentColor, bold = true))
                    
                    val valLines = PdfTextHelper.wrapText(value, bodyPaint, (colW - 50).toInt())
                    writer.canvas!!.drawText(valLines[0], x + 30f, writer.y + 17f, bodyPaint)
                }
                writer.advance(28f)
            }
            writer.advance(10f)
        }

        // ---------------------------------------------------------
        // SECTION CARD HELPER
        // ---------------------------------------------------------
        fun drawModernSection(title: String, block: () -> Unit) {
            writer.ensureSpace(60f)
            val titleY = writer.y
            // Section Marker
            writer.canvas!!.drawRect(margin, titleY, margin + 5f, titleY + 22f, accentBarPaint)
            writer.canvas!!.drawText(title.uppercase(), margin + 18f, titleY + 17f, sectionTitlePaint)
            writer.advance(32f)

            // Measure block if possible or just use a card per item
            block()
            writer.advance(15f)
        }

        fun drawContentCard(block: () -> Unit) {
            val cardTop = writer.y
            block()
            val cardBottom = writer.y
            writer.canvas!!.drawRoundRect(margin, cardTop - 5f, writer.pageWidth - margin, cardBottom + 5f, 14f, 14f, cardBgPaint)
            // Re-draw text logic in cards is hard with current writer, so we'll draw background before content
        }

        if (cvData.summary.isNotBlank()) {
            drawModernSection("Summary") {
                val lines = PdfTextHelper.wrapText(cvData.summary, bodyPaint, (writer.contentWidth - 40).toInt())
                val h = lines.size * 22f + 20f
                writer.canvas!!.drawRoundRect(margin, writer.y, writer.pageWidth - margin, writer.y + h, 14f, 14f, cardBgPaint)
                lines.forEachIndexed { i, line ->
                    writer.canvas!!.drawText(line, margin + 20f, writer.y + 20f + (i * 22f), bodyPaint)
                }
                writer.advance(h)
            }
        }

        if (cvData.experiences.isNotEmpty()) {
            drawModernSection("Experience") {
                cvData.experiences.forEach { exp ->
                    val descLines = if (exp.description.isNotBlank()) PdfTextHelper.wrapText(exp.description, bodyPaint, (writer.contentWidth - 40).toInt()) else emptyList()
                    val h = 65f + (descLines.size * 21f)
                    writer.ensureSpace(h + 10f)
                    writer.canvas!!.drawRoundRect(margin, writer.y, writer.pageWidth - margin, writer.y + h, 14f, 14f, cardBgPaint)
                    
                    writer.canvas!!.drawText(exp.role, margin + 20f, writer.y + 25f, subtitlePaint)
                    writer.canvas!!.drawText(exp.company, margin + 20f, writer.y + 45f, bodyPaint)
                    
                    val dW = datePaint.measureText(exp.dates)
                    writer.canvas!!.drawRoundRect(writer.pageWidth - margin - dW - 20f, writer.y + 12f, writer.pageWidth - margin - 10f, writer.y + 32f, 6f, 6f, softAccentPaint)
                    writer.canvas!!.drawText(exp.dates, writer.pageWidth - margin - dW - 15f, writer.y + 26f, datePaint)
                    
                    descLines.forEachIndexed { i, line ->
                        writer.canvas!!.drawText(line, margin + 20f, writer.y + 65f + (i * 21f), bodyPaint)
                    }
                    writer.advance(h + 10f)
                }
            }
        }

        if (cvData.education.isNotEmpty()) {
            drawModernSection("Education") {
                cvData.education.forEach { edu ->
                    val h = 60f
                    writer.ensureSpace(h + 10f)
                    writer.canvas!!.drawRoundRect(margin, writer.y, writer.pageWidth - margin, writer.y + h, 14f, 14f, cardBgPaint)
                    writer.canvas!!.drawText(edu.degree, margin + 20f, writer.y + 25f, subtitlePaint)
                    writer.canvas!!.drawText(edu.school, margin + 20f, writer.y + 45f, bodyPaint)
                    val dW = bodyPaint.measureText(edu.dates)
                    writer.canvas!!.drawText(edu.dates, writer.pageWidth - margin - dW - 20f, writer.y + 25f, bodyPaint)
                    writer.advance(h + 10f)
                }
            }
        }

        if (cvData.skills.isNotEmpty()) {
            drawModernSection("Skills") {
                val h = 45f
                writer.ensureSpace(h + 10f)
                writer.canvas!!.drawRoundRect(margin, writer.y, writer.pageWidth - margin, writer.y + h, 14f, 14f, cardBgPaint)
                var x = margin + 20f
                cvData.skills.forEach { skill ->
                    writer.canvas!!.drawCircle(x + 4f, writer.y + 22f, 3f, accentBarPaint)
                    writer.canvas!!.drawText(skill, x + 12f, writer.y + 27f, bodyPaint)
                    x += bodyPaint.measureText(skill) + 30f
                }
                writer.advance(h + 10f)
            }
        }

        writer.finish()
    }
}
