package com.example.cvmakerapp.pdf

import android.content.Context
import android.graphics.pdf.PdfDocument
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.CvTemplate

object ModernPdfRenderer {

    fun render(document: PdfDocument, cvData: CvData, context: Context?) {
        val design = PdfDesigns.forTemplate(CvTemplate.MODERN)
        val headerHeight = 140f
        val cardPadding = 12f

        val writer = PdfPageWriter(
            document = document,
            marginLeft = design.margin,
            marginRight = design.margin,
            marginTop = design.margin,
            marginBottom = design.margin,
            drawPageBackground = { canvas, width, height, pageIndex ->
                if (pageIndex == 1) {
                    val headerColor = design.headerBackground ?: return@drawPageBackground
                    canvas.drawRect(0f, 0f, width.toFloat(), headerHeight, PdfTextHelper.createFillPaint(headerColor))
                }
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), PdfTextHelper.createFillPaint(design.pageBackground))
                if (pageIndex == 1) {
                    val headerColor = design.headerBackground ?: return@drawPageBackground
                    canvas.drawRect(0f, 0f, width.toFloat(), headerHeight, PdfTextHelper.createFillPaint(headerColor))
                }
            }
        )

        val headerTitlePaint = PdfTextHelper.createTextPaint(20f, design.headerTextColor, bold = true)
        val headerSubtitlePaint = PdfTextHelper.createTextPaint(11.5f, design.headerTextColor)
        val contactTextPaint = PdfTextHelper.createTextPaint(8.5f, design.bodyColor)
        val sectionTitlePaint = PdfTextHelper.createTextPaint(10.5f, design.accentColor, bold = true)
        val subtitlePaint = PdfTextHelper.createTextPaint(10.5f, design.subtitleColor, bold = true)
        val bodyPaint = PdfTextHelper.createTextPaint(9.5f, design.bodyColor)
        val dividerPaint = PdfTextHelper.createLinePaint(design.accentColor, 0.8f)
        val accentBarPaint = PdfTextHelper.createFillPaint(design.accentColor)
        val softBgPaint = PdfTextHelper.createFillPaint(
            android.graphics.Color.argb(10,
                android.graphics.Color.red(design.accentColor),
                android.graphics.Color.green(design.accentColor),
                android.graphics.Color.blue(design.accentColor))
        )
        val cardBgPaint = PdfTextHelper.createFillPaint(
            android.graphics.Color.argb(6,
                android.graphics.Color.red(design.bodyColor),
                android.graphics.Color.green(design.bodyColor),
                android.graphics.Color.blue(design.bodyColor))
        )
        val skillChipPaint = PdfTextHelper.createTextPaint(9f, design.sectionTitleColor, bold = true)

        writer.start()

        val headerX = writer.marginLeft
        val headerY = 18f

        var profileImageDrawn = false
        if (!cvData.profileImageUri.isNullOrBlank() && context != null) {
            try {
                val bitmap = PdfImageHelper.loadBitmapFromUri(context, cvData.profileImageUri, 90)
                if (bitmap != null) {
                    val cardTop = headerY
                    val cardLeft = headerX
                    val cardBottom = headerY + 88f
                    val cardRight = headerX + 88f
                    writer.canvas!!.drawRoundRect(cardLeft - 1f, cardTop - 1f, cardRight + 1f, cardBottom + 1f, 10f, 10f,
                        PdfTextHelper.createFillPaint(
                            android.graphics.Color.argb(40, 0, 0, 0)
                        )
                    )
                    writer.canvas!!.drawRoundRect(cardLeft, cardTop, cardRight, cardBottom, 10f, 10f,
                        PdfTextHelper.createFillPaint(design.headerTextColor)
                    )
                    PdfImageHelper.drawCircularImage(writer.canvas!!, bitmap, cardLeft + 8f, cardTop + 8f, 72f)
                    profileImageDrawn = true
                }
            } catch (_: Exception) { }
        }

        val nameX = if (profileImageDrawn) headerX + 100f else headerX
        val nameTopY = headerY + 18f
        writer.canvas!!.drawText(cvData.fullName.ifBlank { "Your Name" }, nameX, nameTopY + headerTitlePaint.textSize, headerTitlePaint)
        if (cvData.jobTitle.isNotBlank()) {
            writer.canvas!!.drawText(cvData.jobTitle, nameX, nameTopY + headerTitlePaint.textSize + 26f, headerSubtitlePaint)
        }

        writer.setY(headerHeight + 16f)

        val contactPairs = mutableListOf<Pair<String, String>>()
        if (cvData.email.isNotBlank()) contactPairs.add("✉" to cvData.email)
        if (cvData.phone.isNotBlank()) contactPairs.add("☎" to cvData.phone)
        if (cvData.location.isNotBlank()) contactPairs.add("📍" to cvData.location)
        if (cvData.linkedIn.isNotBlank()) contactPairs.add("👤" to cvData.linkedIn)
        if (cvData.website.isNotBlank()) contactPairs.add("🌐" to cvData.website)

        if (contactPairs.isNotEmpty()) {
            val contactBoxTop = writer.y
            val contactBoxLeft = writer.marginLeft
            val contactBoxRight = writer.marginLeft + writer.contentWidth
            val itemsPerRow = 2
            val rows = (contactPairs.size + itemsPerRow - 1) / itemsPerRow
            val tileHeight = 30f
            val tileGap = 8f
            val rowGap = 6f
            val contactBoxHeight = cardPadding * 2 + rows * tileHeight + (rows - 1) * rowGap

            writer.canvas!!.drawRoundRect(
                contactBoxLeft, contactBoxTop, contactBoxRight, contactBoxTop + contactBoxHeight,
                12f, 12f, cardBgPaint
            )

            val innerLeft = contactBoxLeft + cardPadding
            val tileFullWidth = contactBoxRight - cardPadding - innerLeft
            val tileWidth = (tileFullWidth - tileGap * (itemsPerRow - 1)) / itemsPerRow

            contactPairs.chunked(itemsPerRow).forEachIndexed { rowIdx, rowTiles ->
                rowTiles.forEachIndexed { colIdx, (icon, value) ->
                    val tileLeft = innerLeft + colIdx * (tileWidth + tileGap)
                    val tileTop = contactBoxTop + cardPadding + rowIdx * (tileHeight + rowGap)
                    writer.canvas!!.drawRoundRect(
                        tileLeft, tileTop, tileLeft + 28f, tileTop + tileHeight,
                        7f, 7f, softBgPaint
                    )
                    val iconX = tileLeft + 7f
                    val iconY = tileTop + tileHeight / 2f + contactTextPaint.textSize * 0.32f
                    writer.canvas!!.drawText(icon, iconX, iconY, PdfTextHelper.createTextPaint(10f, design.accentColor, bold = true))
                    val valueX = tileLeft + 36f
                    val valueY = tileTop + tileHeight / 2f + contactTextPaint.textSize * 0.32f
                    writer.canvas!!.drawText(value, valueX, valueY, contactTextPaint)
                }
            }
            writer.advance(contactBoxHeight + 12f)
        }

        fun drawSectionCard(title: String, contentBlock: () -> Unit) {
            writer.ensureSpace(30f)
            val cardTop = writer.y
            writer.canvas!!.drawText(title, writer.marginLeft + 10f, cardTop + sectionTitlePaint.textSize * 0.8f, sectionTitlePaint)
            writer.canvas!!.drawRect(writer.marginLeft, cardTop + 1f, writer.marginLeft + 3.5f, cardTop + 13f, accentBarPaint)
            writer.advance(14f)
            val contentTop = writer.y
            contentBlock()
            val contentBottom = writer.y + 4f

            writer.canvas!!.drawLine(
                writer.marginLeft + 20f,
                cardTop + sectionTitlePaint.textSize + 4f,
                writer.marginLeft + writer.contentWidth,
                cardTop + sectionTitlePaint.textSize + 4f,
                dividerPaint
            )

            writer.y = contentBottom
        }

        if (cvData.summary.isNotBlank()) {
            drawSectionCard("PROFESSIONAL SUMMARY") {
                writer.drawWrappedText(cvData.summary, writer.marginLeft, writer.contentWidth, bodyPaint, 10f)
                writer.advance(6f)
            }
            writer.advance(10f)
        }

        if (cvData.experiences.isNotEmpty()) {
            drawSectionCard("PROFESSIONAL EXPERIENCE") {
                cvData.experiences.forEach { exp ->
                    writer.drawTextLine(exp.role, writer.marginLeft, subtitlePaint, 12f)

                    val dateBoxTop = writer.y
                    val datePad = 7f
                    val dateH = 14f
                    val dateWidth = contactTextPaint.measureText(exp.dates) + datePad * 2
                    writer.canvas!!.drawRoundRect(
                        writer.marginLeft, dateBoxTop, writer.marginLeft + dateWidth, dateBoxTop + dateH,
                        50f, 50f, softBgPaint
                    )
                    writer.canvas!!.drawText(exp.dates, writer.marginLeft + datePad, dateBoxTop + contactTextPaint.textSize * 0.75f + 2f,
                        PdfTextHelper.createTextPaint(8.5f, design.sectionTitleColor, bold = true)
                    )
                    writer.advance(dateH + 2f)

                    writer.drawTextLine(exp.company, writer.marginLeft, bodyPaint, 10f)
                    if (exp.description.isNotBlank()) {
                        writer.drawWrappedText("•  ${exp.description}", writer.marginLeft + 2f, writer.contentWidth - 4f, bodyPaint, 9.5f)
                    }
                    writer.advance(6f)
                }
            }
            writer.advance(10f)
        }

        if (cvData.education.isNotEmpty()) {
            drawSectionCard("EDUCATION") {
                cvData.education.forEach { edu ->
                    writer.drawTextLine(edu.degree, writer.marginLeft, subtitlePaint, 12f)
                    val dateBoxTop = writer.y
                    val datePad = 7f
                    val dateH = 14f
                    val dateWidth = contactTextPaint.measureText(edu.dates) + datePad * 2
                    writer.canvas!!.drawRoundRect(
                        writer.marginLeft, dateBoxTop, writer.marginLeft + dateWidth, dateBoxTop + dateH,
                        50f, 50f, softBgPaint
                    )
                    writer.canvas!!.drawText(edu.dates, writer.marginLeft + datePad, dateBoxTop + contactTextPaint.textSize * 0.75f + 2f,
                        PdfTextHelper.createTextPaint(8.5f, design.sectionTitleColor, bold = true)
                    )
                    writer.advance(dateH + 2f)
                    writer.drawTextLine(edu.school, writer.marginLeft, bodyPaint, 10f)
                    writer.advance(6f)
                }
            }
            writer.advance(10f)
        }

        if (cvData.skills.isNotEmpty()) {
            drawSectionCard("CORE SKILLS") {
                var chipX = writer.marginLeft
                var chipY = writer.y
                val maxX = writer.marginLeft + writer.contentWidth
                val chipPadX = 10f
                val chipH = 18f
                val chipGap = 6f
                cvData.skills.forEach { skill ->
                    val w = skillChipPaint.measureText(skill) + chipPadX * 2
                    if (chipX + w > maxX) {
                        chipX = writer.marginLeft
                        chipY += chipH + chipGap
                        writer.ensureSpace(chipH + 2f)
                    }
                    writer.canvas!!.drawRoundRect(chipX, chipY, chipX + w, chipY + chipH, 40f, 40f, softBgPaint)
                    writer.canvas!!.drawText(skill, chipX + chipPadX, chipY + chipH / 2f + skillChipPaint.textSize * 0.35f, skillChipPaint)
                    chipX += w + chipGap
                }
                writer.advance(chipY - writer.y + chipH + 4f)
            }
        }

        writer.finish()
    }
}
