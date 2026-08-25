package com.example.cvmakerapp.pdf

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.CvTemplate

object ClassicPdfRenderer {

    fun render(document: PdfDocument, cvData: CvData, context: Context? = null) {
        val design = PdfDesigns.forTemplate(CvTemplate.CLASSIC)
        
        val margin = design.margin
        val writer = PdfPageWriter(
            document = document,
            marginLeft = margin,
            marginRight = margin,
            marginTop = margin,
            marginBottom = margin
        )

        // Standardized Typography from Material3 sp values
        // headlineLarge = 32sp, titleLarge = 22sp, titleMedium = 16sp, bodyMedium = 14sp, bodySmall = 12sp
        val namePaint = PdfTextHelper.createTextPaint(32f, design.headerTextColor, bold = true)
        val jobPaint = PdfTextHelper.createTextPaint(22f, design.subtitleColor, bold = false)
        val sectionTitlePaint = PdfTextHelper.createTextPaint(16f, design.sectionTitleColor, bold = true)
        val subtitlePaint = PdfTextHelper.createTextPaint(16f, design.headerTextColor, bold = true) // role
        val companyPaint = PdfTextHelper.createTextPaint(14f, design.accentColor, bold = true)
        val bodyPaint = PdfTextHelper.createTextPaint(14f, design.bodyColor)
        val datePaint = PdfTextHelper.createTextPaint(12f, design.bodyColor)
        
        val accentBarPaint = PdfTextHelper.createFillPaint(design.accentColor)
        val cardBgPaint = PdfTextHelper.createFillPaint(design.accentColorSoft)
        val dividerPaint = PdfTextHelper.createLinePaint(design.dividerColor, 0.8f)

        writer.start()

        // ---------------------------------------------------------
        // HEADER: IMAGE + NAME/JOB
        // ---------------------------------------------------------
        val headerStartY = writer.y
        val imageSize = design.profileImageSize
        var profileImageDrawn = false
        if (!cvData.profileImageUri.isNullOrBlank() && context != null) {
            try {
                val bitmap = PdfImageHelper.loadBitmapFromUri(context, cvData.profileImageUri, 150)
                if (bitmap != null) {
                    PdfImageHelper.drawCircularImage(writer.canvas!!, bitmap, writer.marginLeft, headerStartY, imageSize)
                    profileImageDrawn = true
                }
            } catch (_: Exception) { }
        }

        val textStartX = if (profileImageDrawn) writer.marginLeft + imageSize + 20f else writer.marginLeft
        writer.canvas!!.drawText(cvData.fullName.uppercase(), textStartX, headerStartY + namePaint.textSize * 0.8f, namePaint)
        
        if (cvData.jobTitle.isNotBlank()) {
            writer.canvas!!.drawText(cvData.jobTitle, textStartX, headerStartY + namePaint.textSize + 8f + jobPaint.textSize * 0.8f, jobPaint)
        }

        writer.y = Math.max(headerStartY + imageSize, headerStartY + namePaint.textSize + 24f + jobPaint.textSize) + 20f

        // ---------------------------------------------------------
        // CONTACT BOX (Rounded card from preview)
        // ---------------------------------------------------------
        val contactPairs = mutableListOf<Triple<String, String, String>>()
        if (cvData.email.isNotBlank()) contactPairs.add(Triple("Email", "✉", cvData.email))
        if (cvData.phone.isNotBlank()) contactPairs.add(Triple("Phone", "☎", cvData.phone))
        if (cvData.location.isNotBlank()) contactPairs.add(Triple("Location", "📍", cvData.location))
        if (cvData.linkedIn.isNotBlank()) contactPairs.add(Triple("LinkedIn", "👤", cvData.linkedIn))
        if (cvData.website.isNotBlank()) contactPairs.add(Triple("Website", "🌐", cvData.website))

        if (contactPairs.isNotEmpty()) {
            val itemsPerRow = 2
            val colWidth = writer.contentWidth / itemsPerRow
            val iconPaint = PdfTextHelper.createTextPaint(12f, design.accentColor, bold = true)
            val contactTextPaint = PdfTextHelper.createTextPaint(12f, design.headerTextColor)

            // Background Card
            val boxTop = writer.y
            val rows = (contactPairs.size + itemsPerRow - 1) / itemsPerRow
            val boxHeight = rows * 22f + 16f
            
            writer.canvas!!.drawRoundRect(writer.marginLeft, boxTop, writer.marginLeft + writer.contentWidth, boxTop + boxHeight, 10f, 10f, cardBgPaint)
            writer.advance(12f)

            contactPairs.chunked(itemsPerRow).forEach { row ->
                row.forEachIndexed { i, (_, icon, value) ->
                    val x = writer.marginLeft + 16f + (i * colWidth)
                    val baselineY = writer.y + contactTextPaint.textSize * 0.8f
                    writer.canvas!!.drawText(icon, x, baselineY, iconPaint)
                    
                    val valLines = PdfTextHelper.wrapText(value, contactTextPaint, (colWidth - 35).toInt())
                    writer.canvas!!.drawText(valLines[0], x + 18f, baselineY, contactTextPaint)
                }
                writer.advance(22f)
            }
            writer.advance(8f)
        }

        // Thick horizontal bar (3dp in preview)
        writer.canvas!!.drawRect(writer.marginLeft, writer.y, writer.marginLeft + writer.contentWidth, writer.y + 3f, accentBarPaint)
        writer.advance(18f)

        // ---------------------------------------------------------
        // SECTION HELPERS
        // ---------------------------------------------------------
        fun drawClassicSectionTitle(title: String) {
            writer.ensureSpace(50f)
            val titleY = writer.y
            // Vertical bar (4dp width, 18dp height)
            writer.canvas!!.drawRect(writer.marginLeft, titleY, writer.marginLeft + 4f, titleY + 18f, accentBarPaint)
            writer.canvas!!.drawText(title.uppercase(), writer.marginLeft + 14f, titleY + 14f, sectionTitlePaint)
            writer.advance(24f)
            // Divider (1dp)
            writer.drawLine(writer.marginLeft, writer.y, writer.marginLeft + writer.contentWidth, writer.y, dividerPaint)
            writer.advance(14f)
        }

        if (cvData.summary.isNotBlank()) {
            drawClassicSectionTitle("Professional Summary")
            writer.drawWrappedText(cvData.summary, writer.marginLeft, writer.contentWidth, bodyPaint)
            writer.advance(16f)
        }

        if (cvData.experiences.isNotEmpty()) {
            drawClassicSectionTitle("Professional Experience")
            cvData.experiences.forEach { exp ->
                writer.ensureSpace(60f)
                // Role
                writer.canvas!!.drawText(exp.role, writer.marginLeft, writer.y + subtitlePaint.textSize * 0.8f, subtitlePaint)
                // Date
                val dWidth = datePaint.measureText(exp.dates)
                writer.canvas!!.drawText(exp.dates, writer.marginLeft + writer.contentWidth - dWidth, writer.y + subtitlePaint.textSize * 0.8f, datePaint)
                writer.advance(subtitlePaint.textSize + 4f)
                
                // Company
                writer.canvas!!.drawText(exp.company, writer.marginLeft, writer.y + companyPaint.textSize * 0.8f, companyPaint)
                writer.advance(companyPaint.textSize + 8f)
                
                if (exp.description.isNotBlank()) {
                    val lines = exp.description.split("\n").filter { it.isNotBlank() }
                    lines.forEach { line ->
                        writer.ensureSpace(bodyPaint.textSize + 4f)
                        // Bullet point (5dp circle in preview)
                        writer.canvas!!.drawCircle(writer.marginLeft + 4f, writer.y + bodyPaint.textSize * 0.45f, 2.5f, accentBarPaint)
                        writer.drawWrappedText(line.trim().removePrefix("•").trim(), writer.marginLeft, writer.contentWidth, bodyPaint, indent = 15f)
                    }
                }
                writer.advance(14f)
            }
        }

        if (cvData.education.isNotEmpty()) {
            drawClassicSectionTitle("Education")
            cvData.education.forEach { edu ->
                writer.ensureSpace(45f)
                // Degree
                writer.canvas!!.drawText(edu.degree, writer.marginLeft, writer.y + subtitlePaint.textSize * 0.8f, subtitlePaint)
                // Date
                val dWidth = datePaint.measureText(edu.dates)
                writer.canvas!!.drawText(edu.dates, writer.marginLeft + writer.contentWidth - dWidth, writer.y + subtitlePaint.textSize * 0.8f, datePaint)
                writer.advance(subtitlePaint.textSize + 4f)
                
                // School
                writer.canvas!!.drawText(edu.school, writer.marginLeft, writer.y + companyPaint.textSize * 0.8f, companyPaint)
                writer.advance(companyPaint.textSize + 10f)
            }
        }

        if (cvData.skills.isNotEmpty()) {
            drawClassicSectionTitle("Skills")
            // Render skills in boxes like preview
            var currentX = writer.marginLeft
            val chipPad = 12f
            val chipH = 24f
            val chipGap = 12f
            
            cvData.skills.forEach { skill ->
                val textW = bodyPaint.measureText(skill)
                val chipW = textW + chipPad * 2
                
                if (currentX + chipW > writer.marginLeft + writer.contentWidth) {
                    currentX = writer.marginLeft
                    writer.advance(chipH + chipGap)
                    writer.ensureSpace(chipH)
                }
                
                writer.canvas!!.drawRoundRect(currentX, writer.y, currentX + chipW, writer.y + chipH, 6f, 6f, cardBgPaint)
                writer.canvas!!.drawText(skill, currentX + chipPad, writer.y + chipH / 2f + bodyPaint.textSize * 0.35f, bodyPaint)
                currentX += chipW + chipGap
            }
            writer.advance(chipH + 10f)
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
