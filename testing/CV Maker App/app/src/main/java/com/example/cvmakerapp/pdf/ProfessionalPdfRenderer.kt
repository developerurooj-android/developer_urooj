package com.example.cvmakerapp.pdf

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.ui.theme.CvIconSystem

object ProfessionalPdfRenderer {

    fun render(document: PdfDocument, cvData: CvData, context: Context?) {
        val black = 0xFF000000.toInt()
        val navyBlue = 0xFF1E40AF.toInt() 
        val lightBlueAccent = 0xFFEFF6FF.toInt() 
        val charcoal = 0xFF333333.toInt()
        val mediumGrey = 0xFF666666.toInt()

        val margin = 40f
        val writer = PdfPageWriter(
            document = document,
            marginLeft = margin,
            marginRight = margin,
            marginTop = 15f, 
            marginBottom = margin
        )

        // Typography: Times New Roman
        val namePaint = PdfTextHelper.createTextPaint(24f, navyBlue, bold = true)
        val jobTitlePaint = PdfTextHelper.createTextPaint(14f, mediumGrey, bold = true)
        val sectionTitlePaint = PdfTextHelper.createTextPaint(13f, navyBlue, bold = true)
        val bodyPaint = PdfTextHelper.createTextPaint(11f, charcoal)
        val bodyBoldPaint = PdfTextHelper.createTextPaint(11f, black, bold = true)
        
        val navyFillPaint = PdfTextHelper.createFillPaint(navyBlue)
        val softBlueFillPaint = PdfTextHelper.createFillPaint(lightBlueAccent)

        writer.start()

        // ---------------------------------------------------------
        // TOP ACCENT BAR
        // ---------------------------------------------------------
        writer.canvas!!.drawRect(0f, 0f, writer.pageWidth.toFloat(), 8f, navyFillPaint)
        writer.advance(25f)

        // ---------------------------------------------------------
        // HEADER
        // ---------------------------------------------------------
        val headerStartY = writer.y
        val imageSize = 88f
        
        if (!cvData.profileImageUri.isNullOrBlank() && context != null) {
            try {
                val bitmap = PdfImageHelper.loadBitmapFromUri(context, cvData.profileImageUri, 120)
                if (bitmap != null) {
                    val imageX = writer.pageWidth - margin - imageSize
                    val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = 0xFFE5E7EB.toInt() 
                        style = Paint.Style.STROKE
                        strokeWidth = 2f
                    }
                    writer.canvas!!.drawCircle(imageX + imageSize / 2f, headerStartY + imageSize / 2f, imageSize / 2f + 2f, borderPaint)
                    PdfImageHelper.drawCircularImage(writer.canvas!!, bitmap, imageX, headerStartY, imageSize)
                }
            } catch (_: Exception) { }
        }

        writer.canvas!!.drawText(cvData.fullName.ifBlank { "YOUR NAME" }.uppercase(), margin, headerStartY + namePaint.textSize * 0.8f, namePaint)
        
        if (cvData.jobTitle.isNotBlank()) {
            val yJob = headerStartY + namePaint.textSize + 8f
            writer.canvas!!.drawRect(margin, yJob, margin + 4f, yJob + 16f, navyFillPaint)
            writer.canvas!!.drawText(cvData.jobTitle, margin + 12f, yJob + 13f, jobTitlePaint)
        }

        writer.y = headerStartY + 55f

        // ---------------------------------------------------------
        // CONTACT BLOCK: FIXED ALIGNMENT
        // ---------------------------------------------------------
        val contacts = listOfNotNull(
            cvData.email.takeIf { it.isNotBlank() }?.let { "Email" to (CvIconSystem.Email.emoji to it) },
            cvData.phone.takeIf { it.isNotBlank() }?.let { "Phone" to (CvIconSystem.Phone.emoji to it) },
            cvData.location.takeIf { it.isNotBlank() }?.let { "Location" to (CvIconSystem.Location.emoji to it) },
            cvData.linkedIn.takeIf { it.isNotBlank() }?.let { "LinkedIn" to (CvIconSystem.LinkedIn.emoji to it) },
            cvData.website.takeIf { it.isNotBlank() }?.let { "Website" to (CvIconSystem.Website.emoji to it) }
        )

        if (contacts.isNotEmpty()) {
            val boxW = 180f
            val itemsPerRow = CvIconSystem.PdfConstants.CONTACT_ITEMS_PER_ROW
            val colW = boxW / itemsPerRow
            
            // Unified size
            val uniformFontSize = CvIconSystem.PdfConstants.ICON_SIZE
            val iconPaint = PdfTextHelper.createTextPaint(uniformFontSize, navyBlue, bold = false)
            val labelPaint = PdfTextHelper.createTextPaint(uniformFontSize, black, bold = true)
            val valuePaint = PdfTextHelper.createTextPaint(uniformFontSize, charcoal, bold = false)
            
            val rows = (contacts.size + itemsPerRow - 1) / itemsPerRow
            val boxH = rows * CvIconSystem.PdfConstants.ROW_HEIGHT + 12f
            
            writer.canvas!!.drawRoundRect(margin, writer.y, margin + boxW + 40f, writer.y + boxH, 8f, 8f, softBlueFillPaint)
            writer.advance(8f)
            
            contacts.chunked(itemsPerRow).forEach { row ->
                row.forEachIndexed { i, (label, contact) ->
                    val (icon, value) = contact
                    val x = margin + 10f + (i * (colW + 20f))
                    val iconW = 15f
                    val labelFull = "$label: "
                    val labelW = labelPaint.measureText(labelFull)
                    
                    val baselineY = writer.y + uniformFontSize * 0.8f
                    
                    // Perfect horizontal alignment
                    var xOffset = if (icon == CvIconSystem.Location.emoji) -0.05f else 0f
                    if (icon == CvIconSystem.Email.emoji) xOffset += 0.5f
                    writer.canvas!!.drawText(icon, x + xOffset, baselineY, iconPaint)
                    writer.canvas!!.drawText(labelFull, x + iconW, baselineY, labelPaint)
                    
                    val valLines = PdfTextHelper.wrapText(value, valuePaint, (colW - 10).toInt())
                    writer.canvas!!.drawText(valLines[0], x + iconW + labelW, baselineY, valuePaint)
                }
                writer.advance(CvIconSystem.PdfConstants.ROW_HEIGHT)
            }
            writer.advance(10f)
        } else {
            writer.advance(15f)
        }

        writer.drawLine(margin, writer.y, writer.pageWidth - margin, writer.y, PdfTextHelper.createLinePaint(0xFFE5E7EB.toInt(), 0.8f))
        writer.advance(15f)

        fun drawSectionTitle(title: String) {
            writer.ensureSpace(45f)
            val titleY = writer.y
            writer.canvas!!.drawRect(margin, titleY, margin + 5f, titleY + 22f, navyFillPaint)
            writer.canvas!!.drawText(title.uppercase(), margin + 15f, titleY + 16f, sectionTitlePaint)
            writer.advance(32f)
        }

        if (cvData.summary.isNotBlank()) {
            drawSectionTitle("PROFESSIONAL SUMMARY")
            writer.drawWrappedText(cvData.summary, margin, writer.contentWidth, bodyPaint)
            writer.advance(15f)
        }

        if (cvData.experiences.isNotEmpty()) {
            drawSectionTitle("PROFESSIONAL EXPERIENCE")
            cvData.experiences.forEach { exp ->
                writer.ensureSpace(60f)
                
                writer.canvas!!.drawText(exp.role, margin, writer.y + 11f, bodyBoldPaint)
                
                val dWidth = bodyPaint.measureText(exp.dates)
                writer.canvas!!.drawRoundRect(writer.pageWidth - margin - dWidth - 12f, writer.y, writer.pageWidth - margin, writer.y + 18f, 4f, 4f, softBlueFillPaint)
                writer.canvas!!.drawText(exp.dates, writer.pageWidth - margin - dWidth - 6f, writer.y + 13f, bodyPaint)
                writer.advance(18f)
                
                writer.canvas!!.drawText(exp.company, margin, writer.y + 11f, PdfTextHelper.createTextPaint(11f, navyBlue, bold = true))
                writer.advance(18f)
                
                if (exp.description.isNotBlank()) {
                    val lines = exp.description.split("\n").filter { it.isNotBlank() }
                    lines.forEach { line ->
                        writer.ensureSpace(14f)
                        writer.canvas!!.drawCircle(margin + 4f, writer.y + 6f, 2f, navyFillPaint)
                        writer.drawWrappedText(line.trim().removePrefix("•").trim(), margin, writer.contentWidth, bodyPaint, indent = 12f)
                    }
                }
                writer.advance(10f)
            }
        }

        if (cvData.education.isNotEmpty()) {
            drawSectionTitle("EDUCATION")
            cvData.education.forEach { edu ->
                writer.ensureSpace(40f)
                writer.canvas!!.drawText(edu.degree, margin, writer.y + 11f, bodyBoldPaint)
                writer.canvas!!.drawText(edu.dates, writer.pageWidth - margin - bodyPaint.measureText(edu.dates), writer.y + 11f, bodyPaint)
                writer.advance(16f)
                writer.canvas!!.drawText(edu.school, margin, writer.y + 11f, PdfTextHelper.createTextPaint(11f, navyBlue))
                writer.advance(22f)
            }
        }

        if (cvData.skills.isNotEmpty()) {
            drawSectionTitle("CORE SKILLS")
            cvData.skills.forEach { skill ->
                writer.ensureSpace(16f)
                writer.canvas!!.drawRect(margin, writer.y + 2f, margin + 5f, writer.y + 14f, navyFillPaint)
                writer.canvas!!.drawText(skill, margin + 12f, writer.y + 12f, bodyPaint)
                writer.advance(18f)
            }
        }

        writer.finish()
    }

    private fun PdfPageWriter.drawWrappedText(text: String, x: Float, maxWidth: Float, paint: Paint, indent: Float = 0f) {
        val lines = PdfTextHelper.wrapText(text, paint, (maxWidth - indent).toInt())
        lines.forEach { line ->
            ensureSpace(paint.textSize + 3f)
            canvas?.drawText(line, x + indent, y + paint.textSize * 0.8f, paint)
            y += paint.textSize + 3f
        }
    }
}
