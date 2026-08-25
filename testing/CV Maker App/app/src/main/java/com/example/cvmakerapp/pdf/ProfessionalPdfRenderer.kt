package com.example.cvmakerapp.pdf

import android.content.Context
import android.graphics.pdf.PdfDocument
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.CvTemplate

object ProfessionalPdfRenderer {

    fun render(document: PdfDocument, cvData: CvData, context: Context?) {
        val design = PdfDesigns.forTemplate(CvTemplate.PROFESSIONAL)

        val gradientTop = design.headerBackground ?: design.accentColor
        val gR = android.graphics.Color.red(gradientTop)
        val gG = android.graphics.Color.green(gradientTop)
        val gB = android.graphics.Color.blue(gradientTop)
        val gradientBottom = android.graphics.Color.argb(255,
            (gR * 0.65f).toInt().coerceAtLeast(0),
            (gG * 0.70f).toInt().coerceAtLeast(0),
            (gB * 0.85f).toInt().coerceAtLeast(0)
        )

        val topBarHeight = 10f

        val writer = PdfPageWriter(
            document = document,
            marginLeft = design.margin,
            marginRight = design.margin,
            marginTop = design.margin + 14f,
            marginBottom = design.margin,
            drawPageBackground = { canvas, width, height, pageIndex ->
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(),
                    PdfTextHelper.createFillPaint(design.pageBackground))
                if (pageIndex == 1) {
                    val steps = 20
                    for (i in 0 until steps) {
                        val t = i / steps.toFloat()
                        val yFrom = topBarHeight * i / steps.toFloat()
                        val yTo = topBarHeight * (i + 1) / steps.toFloat() + 0.5f
                        val r = (gR * (1 - t) + android.graphics.Color.red(gradientBottom) * t).toInt()
                        val g = (gG * (1 - t) + android.graphics.Color.green(gradientBottom) * t).toInt()
                        val b = (gB * (1 - t) + android.graphics.Color.blue(gradientBottom) * t).toInt()
                        canvas.drawRect(0f, yFrom, width.toFloat(), yTo,
                            PdfTextHelper.createFillPaint(android.graphics.Color.argb(255, r, g, b)))
                    }
                }
            }
        )

        val namePaint = PdfTextHelper.createTextPaint(20f, design.sectionTitleColor, bold = true)
        val jobPaint = PdfTextHelper.createTextPaint(11f, design.subtitleColor)
        val contactPaint = PdfTextHelper.createTextPaint(8.5f, design.bodyColor)
        val sectionTitlePaint = PdfTextHelper.createTextPaint(10.5f, design.accentColor, bold = true)
        val rolePaint = PdfTextHelper.createTextPaint(11f, design.sectionTitleColor, bold = true)
        val companyPaint = PdfTextHelper.createTextPaint(9f, design.subtitleColor, bold = true)
        val bodyPaint = PdfTextHelper.createTextPaint(9.5f, design.bodyColor)
        val accentBarPaint = PdfTextHelper.createFillPaint(design.accentColor)
        val dividerPaint = PdfTextHelper.createLinePaint(design.dividerColor, 0.5f)
        val softAccentBg = PdfTextHelper.createFillPaint(
            android.graphics.Color.argb(10,
                android.graphics.Color.red(design.accentColor),
                android.graphics.Color.green(design.accentColor),
                android.graphics.Color.blue(design.accentColor))
        )
        val skillChipPaint = PdfTextHelper.createTextPaint(9f, design.sectionTitleColor, bold = true)

        writer.start()

        if (!cvData.profileImageUri.isNullOrBlank() && context != null) {
            try {
                val bitmap = PdfImageHelper.loadBitmapFromUri(context, cvData.profileImageUri, 90)
                if (bitmap != null) {
                    val imageSize = 66f
                    val imageX = writer.pageWidth - writer.marginRight - imageSize - 4f
                    val borderPaint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
                        color = design.accentColor
                        style = android.graphics.Paint.Style.STROKE
                        strokeWidth = 1.5f
                    }
                    writer.canvas!!.drawCircle(
                        imageX + imageSize / 2f, writer.marginTop - 4f + imageSize / 2f,
                        imageSize / 2f + 3f, borderPaint
                    )
                    PdfImageHelper.drawCircularImage(writer.canvas!!, bitmap, imageX, writer.marginTop - 4f, imageSize)
                }
            } catch (_: Exception) { }
        }

        writer.drawTextLine(cvData.fullName.uppercase(), writer.marginLeft, namePaint, 22f)
        if (cvData.jobTitle.isNotBlank()) {
            writer.drawTextLine(cvData.jobTitle, writer.marginLeft, jobPaint, 12f)
        }

        writer.advance(8f)

        val contactPairs = mutableListOf<Pair<String, String>>()
        if (cvData.email.isNotBlank()) contactPairs.add("✉" to cvData.email)
        if (cvData.phone.isNotBlank()) contactPairs.add("☎" to cvData.phone)
        if (cvData.location.isNotBlank()) contactPairs.add("📍" to cvData.location)
        if (cvData.linkedIn.isNotBlank()) contactPairs.add("👤" to cvData.linkedIn)
        if (cvData.website.isNotBlank()) contactPairs.add("🌐" to cvData.website)

        if (contactPairs.isNotEmpty()) {
            val boxTop = writer.y
            val boxLeft = writer.marginLeft
            val boxRight = writer.marginLeft + writer.contentWidth
            val itemsPerRow = 2
            val rows = (contactPairs.size + itemsPerRow - 1) / itemsPerRow
            val rowH = 18f
            val pad = 10f
            val boxHeight = rows * rowH + pad * 2f
            writer.canvas!!.drawRoundRect(boxLeft, boxTop, boxRight, boxTop + boxHeight,
                10f, 10f, softAccentBg)
            val innerLeft = boxLeft + pad
            val colW = (boxRight - pad - innerLeft) / itemsPerRow
            contactPairs.chunked(itemsPerRow).forEachIndexed { rIdx, row ->
                row.forEachIndexed { cIdx, (ic, value) ->
                    val tLeft = innerLeft + cIdx * colW
                    val tY = boxTop + pad + rIdx * rowH + contactPaint.textSize * 0.75f + 1f
                    writer.canvas!!.drawText(ic, tLeft, tY,
                        PdfTextHelper.createTextPaint(10f, design.accentColor, bold = true))
                    writer.canvas!!.drawText(value, tLeft + 16f, tY, contactPaint)
                }
            }
            writer.advance(boxHeight + 8f)
        }

        writer.drawLine(writer.marginLeft, writer.y, writer.marginLeft + writer.contentWidth, writer.y, dividerPaint)
        writer.advance(12f)

        fun drawProSectionTitle(title: String) {
            writer.ensureSpace(16f)
            val titleY = writer.y
            writer.canvas!!.drawRect(writer.marginLeft, titleY + 1f, writer.marginLeft + 3.5f, titleY + 13f, accentBarPaint)
            writer.drawTextLine(title, writer.marginLeft + 10f, sectionTitlePaint, 12f)
            writer.advance(4f)
        }

        if (cvData.summary.isNotBlank()) {
            drawProSectionTitle("PROFESSIONAL SUMMARY")
            writer.drawWrappedText(cvData.summary, writer.marginLeft, writer.contentWidth, bodyPaint, 10f)
            writer.advance(8f)
        }

        if (cvData.experiences.isNotEmpty()) {
            drawProSectionTitle("PROFESSIONAL EXPERIENCE")
            cvData.experiences.forEach { exp ->
                writer.drawTextLine(exp.role, writer.marginLeft, rolePaint, 12f)
                writer.drawTextLine(exp.company, writer.marginLeft, companyPaint, 10f)
                if (exp.dates.isNotBlank()) {
                    val dateTop = writer.y - 1f
                    val pad = 8f
                    val dateH = 15f
                    val dateW = contactPaint.measureText(exp.dates) + pad * 2
                    writer.canvas!!.drawRoundRect(
                        writer.marginLeft, dateTop, writer.marginLeft + dateW, dateTop + dateH,
                        40f, 40f, softAccentBg
                    )
                    writer.canvas!!.drawText(exp.dates, writer.marginLeft + pad, dateTop + contactPaint.textSize * 0.75f + 2f,
                        PdfTextHelper.createTextPaint(8.5f, design.sectionTitleColor, bold = true))
                    writer.advance(dateH + 2f)
                } else writer.advance(2f)
                if (exp.description.isNotBlank()) {
                    writer.drawWrappedText("•  ${exp.description}", writer.marginLeft + 4f, writer.contentWidth - 8f, bodyPaint, 9.5f)
                }
                writer.advance(6f)
            }
            writer.advance(4f)
        }

        if (cvData.education.isNotEmpty()) {
            drawProSectionTitle("EDUCATION")
            cvData.education.forEach { edu ->
                writer.drawTextLine(edu.degree, writer.marginLeft, rolePaint, 12f)
                writer.drawTextLine(edu.school, writer.marginLeft, bodyPaint, 9.5f)
                if (edu.dates.isNotBlank()) {
                    val dateTop = writer.y - 1f
                    val pad = 8f
                    val dateH = 15f
                    val dateW = contactPaint.measureText(edu.dates) + pad * 2
                    writer.canvas!!.drawRoundRect(
                        writer.marginLeft, dateTop, writer.marginLeft + dateW, dateTop + dateH,
                        40f, 40f, softAccentBg
                    )
                    writer.canvas!!.drawText(edu.dates, writer.marginLeft + pad, dateTop + contactPaint.textSize * 0.75f + 2f,
                        PdfTextHelper.createTextPaint(8.5f, design.sectionTitleColor, bold = true))
                    writer.advance(dateH + 2f)
                } else writer.advance(6f)
            }
            writer.advance(4f)
        }

        if (cvData.skills.isNotEmpty()) {
            drawProSectionTitle("CORE SKILLS")

            val cols = 2
            val colGap = 14f
            val colWidth = (writer.contentWidth - colGap) / cols

            cvData.skills.chunked(cols).forEach { rowSkills ->
                rowSkills.forEachIndexed { idx, skill ->
                    val colX = writer.marginLeft + idx * (colWidth + colGap)
                    val rowY = writer.y
                    writer.ensureSpace(16f)
                    val barPaint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
                        color = android.graphics.Color.argb(
                            22,
                            android.graphics.Color.red(design.accentColor),
                            android.graphics.Color.green(design.accentColor),
                            android.graphics.Color.blue(design.accentColor)
                        )
                        style = android.graphics.Paint.Style.FILL
                    }
                    val barW = 30f
                    writer.canvas!!.drawRoundRect(colX, rowY + 4f, colX + barW, rowY + 4f + 10f, 50f, 50f, barPaint)
                    writer.canvas!!.drawRect(colX, rowY + 4f, colX + (barW * 0.7f), rowY + 4f + 10f, accentBarPaint)
                    writer.canvas!!.drawText(skill, colX + barW + 10f, rowY + skillChipPaint.textSize * 0.85f, skillChipPaint)
                }
                writer.advance(18f)
            }
        }

        writer.finish()
    }
}
