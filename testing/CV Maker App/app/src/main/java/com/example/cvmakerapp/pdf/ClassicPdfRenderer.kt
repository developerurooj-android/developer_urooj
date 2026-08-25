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
        val jobTitlePaint = PdfTextHelper.createTextPaint(13f, design.accentColor, bold = false)
        val contactLabelPaint = PdfTextHelper.createTextPaint(9f, design.bodyColor)
        val sectionTitlePaint = PdfTextHelper.createTextPaint(10.5f, design.accentColor, bold = true)
        val subtitlePaint = PdfTextHelper.createTextPaint(10.5f, design.subtitleColor, bold = true)
        val labelPaint = PdfTextHelper.createTextPaint(9f, design.bodyColor)
        val bodyPaint = PdfTextHelper.createTextPaint(10f, design.bodyColor)
        val dividerPaint = PdfTextHelper.createLinePaint(design.dividerColor, 0.5f)
        val accentBarPaint = PdfTextHelper.createFillPaint(design.accentColor)
        val accentSoftBgPaint = PdfTextHelper.createFillPaint(
            android.graphics.Color.argb(14,
                android.graphics.Color.red(design.accentColor),
                android.graphics.Color.green(design.accentColor),
                android.graphics.Color.blue(design.accentColor))
        )
        val skillChipTextPaint = PdfTextHelper.createTextPaint(9f, design.sectionTitleColor, bold = true)

        writer.start()

        val imageSize = 80f
        val headerStartY = writer.y
        var profileImageDrawn = false
        if (!cvData.profileImageUri.isNullOrBlank() && context != null) {
            try {
                val bitmap = PdfImageHelper.loadBitmapFromUri(context, cvData.profileImageUri, 100)
                if (bitmap != null) {
                    writer.ensureSpace(imageSize + 10f)
                    PdfImageHelper.drawCircularImage(writer.canvas!!, bitmap, writer.marginLeft, headerStartY, imageSize)
                    profileImageDrawn = true
                }
            } catch (_: Exception) { }
        }

        val textStartX = if (profileImageDrawn) {
            writer.marginLeft + imageSize + 18f
        } else {
            writer.marginLeft
        }

        writer.canvas!!.drawText(
            cvData.fullName.uppercase(),
            textStartX,
            headerStartY + titlePaint.textSize,
            titlePaint
        )

        val nameLineY = headerStartY + titlePaint.textSize + 4f
        val accentUnderlineStart = textStartX
        val accentUnderlineEnd = textStartX + 120f
        writer.canvas!!.drawRect(accentUnderlineStart, nameLineY, accentUnderlineEnd, nameLineY + 3f, accentBarPaint)

        if (cvData.jobTitle.isNotBlank()) {
            writer.canvas!!.drawText(
                cvData.jobTitle,
                textStartX,
                nameLineY + 3f + jobTitlePaint.textSize + 6f,
                jobTitlePaint
            )
        }

        writer.advance(imageSize + 18f)
        writer.advance(6f)

        val contactItems = mutableListOf<String>()
        if (cvData.email.isNotBlank()) contactItems.add("✉  ${cvData.email}")
        if (cvData.phone.isNotBlank()) contactItems.add("☎  ${cvData.phone}")
        if (cvData.location.isNotBlank()) contactItems.add("📍 ${cvData.location}")
        if (cvData.linkedIn.isNotBlank()) contactItems.add("👤 ${cvData.linkedIn}")
        if (cvData.website.isNotBlank()) contactItems.add("🌐 ${cvData.website}")

        if (contactItems.isNotEmpty()) {
            val contactBoxTop = writer.y
            val contactBoxLeft = writer.marginLeft
            val contactBoxRight = writer.marginLeft + writer.contentWidth
            val itemsPerRow = 2
            val rows = (contactItems.size + itemsPerRow - 1) / itemsPerRow
            val rowHeight = 18f
            val contactBoxPadding = 10f
            val contactBoxHeight = rows * rowHeight + contactBoxPadding * 2f

            writer.canvas!!.drawRoundRect(
                contactBoxLeft, contactBoxTop,
                contactBoxRight, contactBoxTop + contactBoxHeight,
                8f, 8f,
                accentSoftBgPaint
            )

            val contentLeft = contactBoxLeft + contactBoxPadding
            val colWidth = (contactBoxRight - contactBoxPadding - contentLeft) / itemsPerRow

            contactItems.chunked(itemsPerRow).forEachIndexed { rowIdx, rowItems ->
                rowItems.forEachIndexed { colIdx, item ->
                    val itemX = contentLeft + colIdx * colWidth
                    val itemY = contactBoxTop + contactBoxPadding + rowIdx * rowHeight + contactLabelPaint.textSize * 0.75f + 2f
                    writer.canvas!!.drawText(item, itemX, itemY, contactLabelPaint)
                }
            }

            writer.advance(contactBoxHeight + 8f)
        }

        writer.drawLine(writer.marginLeft, writer.y, writer.marginLeft + writer.contentWidth, writer.y, dividerPaint)
        writer.advance(12f)

        fun drawSectionTitle(title: String) {
            writer.ensureSpace(16f)
            val yBar = writer.y
            writer.canvas!!.drawRect(writer.marginLeft, yBar + 1f, writer.marginLeft + 3.5f, yBar + 12f, accentBarPaint)
            writer.canvas!!.drawText(title, writer.marginLeft + 10f, yBar + sectionTitlePaint.textSize * 0.8f, sectionTitlePaint)
            writer.advance(14f)
        }

        if (cvData.summary.isNotBlank()) {
            drawSectionTitle("PROFESSIONAL SUMMARY")
            writer.drawWrappedText(cvData.summary, writer.marginLeft, writer.contentWidth, bodyPaint, 10f)
            writer.advance(10f)
        }

        writer.drawLine(writer.marginLeft, writer.y, writer.marginLeft + writer.contentWidth, writer.y, dividerPaint)
        writer.advance(12f)

        if (cvData.experiences.isNotEmpty()) {
            drawSectionTitle("PROFESSIONAL EXPERIENCE")

            cvData.experiences.forEach { experience ->
                writer.drawTextLine(experience.role, writer.marginLeft, subtitlePaint, 12f)
                writer.drawTextLine(experience.company, writer.marginLeft, labelPaint, 10f)

                if (experience.dates.isNotBlank()) {
                    val dateText = experience.dates
                    val datePaint = accentBarPaint
                    val dateBoxTop = writer.y - 1f
                    val datePaddingX = 8f
                    val dateBoxHeight = 15f
                    val dateTextWidth = labelPaint.measureText(dateText)
                    val dateBoxLeft = writer.marginLeft
                    val dateBoxRight = dateBoxLeft + dateTextWidth + datePaddingX * 2
                    writer.canvas!!.drawRoundRect(
                        dateBoxLeft, dateBoxTop,
                        dateBoxRight, dateBoxTop + dateBoxHeight,
                        6f, 6f,
                        accentSoftBgPaint
                    )
                    writer.canvas!!.drawText(dateText, dateBoxLeft + datePaddingX, dateBoxTop + labelPaint.textSize * 0.75f + 2f, labelPaint)
                    writer.advance(dateBoxHeight + 4f)
                } else {
                    writer.advance(2f)
                }

                if (experience.description.isNotBlank()) {
                    writer.drawWrappedText(
                        "•  ${experience.description}",
                        writer.marginLeft,
                        writer.contentWidth,
                        bodyPaint,
                        10f
                    )
                }
                writer.advance(6f)
            }
            writer.advance(4f)
        }

        writer.drawLine(writer.marginLeft, writer.y, writer.marginLeft + writer.contentWidth, writer.y, dividerPaint)
        writer.advance(12f)

        if (cvData.education.isNotEmpty()) {
            drawSectionTitle("EDUCATION")

            cvData.education.forEach { education ->
                writer.drawTextLine(education.degree, writer.marginLeft, subtitlePaint, 12f)
                writer.drawTextLine(education.school, writer.marginLeft, labelPaint, 10f)
                if (education.dates.isNotBlank()) {
                    val dateText = education.dates
                    val dateBoxTop = writer.y - 1f
                    val datePaddingX = 8f
                    val dateBoxHeight = 15f
                    val dateTextWidth = labelPaint.measureText(dateText)
                    val dateBoxLeft = writer.marginLeft
                    val dateBoxRight = dateBoxLeft + dateTextWidth + datePaddingX * 2
                    writer.canvas!!.drawRoundRect(
                        dateBoxLeft, dateBoxTop,
                        dateBoxRight, dateBoxTop + dateBoxHeight,
                        6f, 6f,
                        accentSoftBgPaint
                    )
                    writer.canvas!!.drawText(dateText, dateBoxLeft + datePaddingX, dateBoxTop + labelPaint.textSize * 0.75f + 2f, labelPaint)
                    writer.advance(dateBoxHeight + 4f)
                } else {
                    writer.advance(6f)
                }
            }
            writer.advance(4f)
        }

        writer.drawLine(writer.marginLeft, writer.y, writer.marginLeft + writer.contentWidth, writer.y, dividerPaint)
        writer.advance(12f)

        if (cvData.skills.isNotEmpty()) {
            drawSectionTitle("SKILLS")

            val chipPaddingX = 10f
            val chipPaddingY = 5f
            val chipRadius = 40f
            val chipGap = 6f
            var chipY = writer.y
            var currentX = writer.marginLeft
            val maxX = writer.marginLeft + writer.contentWidth

            cvData.skills.chunked(3).flatten().forEach { skill ->
                val textWidth = skillChipTextPaint.measureText(skill)
                val chipWidth = textWidth + chipPaddingX * 2
                val chipHeight = 18f

                if (currentX + chipWidth > maxX) {
                    currentX = writer.marginLeft
                    chipY += chipHeight + chipGap
                    writer.ensureSpace(chipHeight + 2f)
                }

                writer.canvas!!.drawRoundRect(
                    currentX, chipY,
                    currentX + chipWidth, chipY + chipHeight,
                    chipRadius, chipRadius,
                    accentSoftBgPaint
                )

                val textX = currentX + chipPaddingX
                val textY = chipY + chipHeight / 2f + skillChipTextPaint.textSize * 0.35f
                writer.canvas!!.drawText(skill, textX, textY, skillChipTextPaint)

                currentX += chipWidth + chipGap
            }

            writer.advance(chipY - writer.y + 22f)
        }

        writer.finish()
    }
}
