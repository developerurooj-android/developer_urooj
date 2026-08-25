package com.example.cvmakerapp.pdf

import android.content.Context
import android.graphics.pdf.PdfDocument
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.CvTemplate

object TwoColumnPdfRenderer {

    fun render(document: PdfDocument, cvData: CvData, context: Context?) {
        val design = PdfDesigns.forTemplate(CvTemplate.TWO_COLUMN)

        val pageWidth = PdfPageWriter.A4_WIDTH
        val pageHeight = PdfPageWriter.A4_HEIGHT
        val marginLeft = 15f
        val marginRight = 15f
        val marginTop = 24f
        val marginBottom = 20f

        val leftColumnWidth = (pageWidth * 0.36f).toInt()
        val rightColumnStartX = leftColumnWidth.toFloat()
        val rightColumnWidth = pageWidth - leftColumnWidth - marginRight
        val leftContentMaxWidth = leftColumnWidth - marginLeft * 2 - 4f

        val sidebarBgTopColor = design.sidebarBackground ?: 0xFFEFF6FF.toInt()
        val red = android.graphics.Color.red(sidebarBgTopColor)
        val green = android.graphics.Color.green(sidebarBgTopColor)
        val blue = android.graphics.Color.blue(sidebarBgTopColor)
        val sidebarBottomColor = android.graphics.Color.argb(
            255,
            (red * 0.82f).toInt().coerceAtLeast(0),
            (green * 0.86f).toInt().coerceAtLeast(0),
            (blue * 0.92f).toInt().coerceAtLeast(0)
        )

        val sidebarTopPaint = PdfTextHelper.createFillPaint(sidebarBgTopColor)
        val sidebarBotPaint = PdfTextHelper.createFillPaint(sidebarBottomColor)
        sidebarBotPaint // reference to avoid unused warning

        val sidebarTextColor = design.headerTextColor.takeIf {
            android.graphics.Color.red(it) + android.graphics.Color.green(it) + android.graphics.Color.blue(it) < 300
        } ?: design.sectionTitleColor

        val titlePaint = PdfTextHelper.createTextPaint(17f, design.sectionTitleColor, bold = true)
        val jobTitlePaint = PdfTextHelper.createTextPaint(10.5f, design.accentColor)
        val leftSectionPaint = PdfTextHelper.createTextPaint(9.5f, sidebarTextColor, bold = true)
        val contactPaint = PdfTextHelper.createTextPaint(8f, sidebarTextColor)
        val skillsPaint = PdfTextHelper.createTextPaint(8.5f, sidebarTextColor)
        val bodyPaint = PdfTextHelper.createTextPaint(9f, design.bodyColor)
        val subtitlePaint = PdfTextHelper.createTextPaint(10.5f, design.subtitleColor, bold = true)
        val sectionTitlePaint = PdfTextHelper.createTextPaint(10f, design.accentColor, bold = true)
        val dividerPaint = PdfTextHelper.createLinePaint(design.dividerColor, 0.5f)
        val accentBarPaint = PdfTextHelper.createFillPaint(design.accentColor)
        val softAccentBg = PdfTextHelper.createFillPaint(
            android.graphics.Color.argb(12,
                android.graphics.Color.red(design.accentColor),
                android.graphics.Color.green(design.accentColor),
                android.graphics.Color.blue(design.accentColor))
        )

        var pageNumber = 0
        var currentPage: PdfDocument.Page? = null
        var canvas: android.graphics.Canvas? = null
        var yLeft = marginTop
        var yRight = marginTop

        fun drawSidebarBackground() {
            val steps = 80
            for (i in 0 until steps) {
                val t = i.toFloat() / steps
                val yFrom = pageHeight * i / steps.toFloat()
                val yTo = pageHeight * (i + 1) / steps.toFloat() + 0.5f
                val r = (red * (1 - t) + (android.graphics.Color.red(sidebarBottomColor) * t)).toInt()
                val g = (green * (1 - t) + (android.graphics.Color.green(sidebarBottomColor) * t)).toInt()
                val b = (blue * (1 - t) + (android.graphics.Color.blue(sidebarBottomColor) * t)).toInt()
                canvas!!.drawRect(0f, yFrom, leftColumnWidth.toFloat(), yTo,
                    PdfTextHelper.createFillPaint(android.graphics.Color.argb(255, r, g, b)))
            }
        }

        fun newPage() {
            currentPage?.let { document.finishPage(it) }
            pageNumber++
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
            currentPage = document.startPage(pageInfo)
            canvas = currentPage!!.canvas
            drawSidebarBackground()
            yLeft = marginTop
            yRight = marginTop
        }

        fun drawLeftText(text: String, paint: android.graphics.Paint, lineHeight: Float) {
            if (text.isBlank()) return
            if (yLeft + lineHeight > pageHeight - marginBottom) newPage()
            canvas!!.drawText(text, marginLeft + 3f, yLeft + paint.textSize * 0.75f, paint)
            yLeft += lineHeight
        }

        fun drawLeftWrapped(text: String, paint: android.graphics.Paint, maxWidth: Float) {
            if (text.isBlank()) return
            val lines = PdfTextHelper.wrapText(text, paint, maxWidth.toInt())
            lines.forEach { line ->
                if (yLeft + paint.textSize + 6f > pageHeight - marginBottom) newPage()
                canvas!!.drawText(line, marginLeft + 3f, yLeft + paint.textSize * 0.75f, paint)
                yLeft += paint.textSize + 4f
            }
        }

        fun drawLeftSectionTitle(title: String) {
            drawLeftText(title, leftSectionPaint, 11f)
            val barY = yLeft - 2f
            canvas!!.drawRect(marginLeft + 3f, barY, marginLeft + 40f, barY + 2.2f, accentBarPaint)
            yLeft += 6f
        }

        fun drawRightText(text: String, paint: android.graphics.Paint, lineHeight: Float) {
            if (text.isBlank()) return
            if (yRight + lineHeight > pageHeight - marginBottom) newPage()
            canvas!!.drawText(text, rightColumnStartX + marginLeft, yRight + paint.textSize * 0.75f, paint)
            yRight += lineHeight
        }

        fun drawRightWrapped(text: String, paint: android.graphics.Paint, maxWidth: Float, lineHeight: Float = paint.textSize + 4f) {
            if (text.isBlank()) return
            val lines = PdfTextHelper.wrapText(text, paint, maxWidth.toInt())
            lines.forEach { line ->
                if (yRight + lineHeight > pageHeight - marginBottom) newPage()
                canvas!!.drawText(line, rightColumnStartX + marginLeft, yRight + paint.textSize * 0.75f, paint)
                yRight += lineHeight
            }
        }

        fun drawRightSectionTitle(title: String) {
            val barX = rightColumnStartX + marginLeft
            val yBar = yRight + 1f
            canvas!!.drawRect(barX, yBar, barX + 3.5f, yBar + 12f, accentBarPaint)
            drawRightText(title, sectionTitlePaint, 13f)
            yRight += 2f
        }

        newPage()

        // LEFT SIDEBAR

        var imagePadCenter = 0f
        if (!cvData.profileImageUri.isNullOrBlank() && context != null) {
            try {
                val bitmap = PdfImageHelper.loadBitmapFromUri(context, cvData.profileImageUri, 100)
                if (bitmap != null) {
                    val imageSize = 72f
                    imagePadCenter = (leftColumnWidth - imageSize) / 2f
                    val borderPaint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
                        color = sidebarTextColor
                        style = android.graphics.Paint.Style.STROKE
                        strokeWidth = 2f
                    }
                    canvas!!.drawCircle(
                        imagePadCenter + imageSize / 2f, yLeft + imageSize / 2f,
                        imageSize / 2f + 3f, borderPaint
                    )
                    PdfImageHelper.drawCircularImage(canvas!!, bitmap, imagePadCenter, yLeft, imageSize)
                    yLeft += imageSize + 14f
                }
            } catch (_: Exception) { }
        }

        // Name centered in sidebar
        val nameMaxWidth = leftColumnWidth - marginLeft * 2f - 6f
        val nameText = cvData.fullName.uppercase().takeIf { it.isNotBlank() } ?: "YOUR NAME"
        val nameLines = PdfTextHelper.wrapText(nameText, titlePaint, nameMaxWidth.toInt())
        nameLines.forEachIndexed { i, line ->
            val lineW = titlePaint.measureText(line)
            val centeredX = marginLeft + 3f + (nameMaxWidth - lineW) / 2f
            if (yLeft + titlePaint.textSize + 6f > pageHeight - marginBottom) newPage()
            canvas!!.drawText(line, centeredX, yLeft + titlePaint.textSize * 0.78f, titlePaint)
            yLeft += titlePaint.textSize + if (i == nameLines.lastIndex) 2f else 0f
        }
        yLeft += 6f

        if (cvData.jobTitle.isNotBlank()) {
            val jobText = cvData.jobTitle
            val jobW = jobTitlePaint.measureText(jobText)
            val jobBoxW = jobW + 16f
            val jobBoxH = 18f
            val centeredJobX = marginLeft + 3f + (nameMaxWidth - jobBoxW) / 2f
            canvas!!.drawRoundRect(centeredJobX, yLeft, centeredJobX + jobBoxW, yLeft + jobBoxH, 40f, 40f,
                PdfTextHelper.createFillPaint(
                    android.graphics.Color.argb(30,
                        android.graphics.Color.red(design.accentColor),
                        android.graphics.Color.green(design.accentColor),
                        android.graphics.Color.blue(design.accentColor))
                )
            )
            canvas!!.drawText(jobText, centeredJobX + 8f, yLeft + jobBoxH / 2f + jobTitlePaint.textSize * 0.35f, jobTitlePaint)
            yLeft += jobBoxH + 10f
        }

        yLeft += 6f
        drawLeftSectionTitle("CONTACT")

        val iconPaint = PdfTextHelper.createTextPaint(9f, design.accentColor, bold = true)
        if (cvData.email.isNotBlank()) {
            canvas!!.drawText("✉", marginLeft + 3f, yLeft + iconPaint.textSize * 0.75f, iconPaint)
            drawLeftWrapped(cvData.email, contactPaint, leftContentMaxWidth - 10f)
            yLeft -= 2f
        }
        if (cvData.phone.isNotBlank()) {
            canvas!!.drawText("☎", marginLeft + 3f, yLeft + iconPaint.textSize * 0.75f, iconPaint)
            drawLeftText(cvData.phone, contactPaint, 10f)
            yLeft -= 2f
        }
        if (cvData.location.isNotBlank()) {
            canvas!!.drawText("📍", marginLeft + 3f, yLeft + iconPaint.textSize * 0.75f, iconPaint)
            drawLeftText(cvData.location, contactPaint, 10f)
            yLeft -= 2f
        }
        if (cvData.linkedIn.isNotBlank()) {
            canvas!!.drawText("👤", marginLeft + 3f, yLeft + iconPaint.textSize * 0.75f, iconPaint)
            drawLeftWrapped(cvData.linkedIn, contactPaint, leftContentMaxWidth - 10f)
            yLeft -= 2f
        }
        if (cvData.website.isNotBlank()) {
            canvas!!.drawText("🌐", marginLeft + 3f, yLeft + iconPaint.textSize * 0.75f, iconPaint)
            drawLeftWrapped(cvData.website, contactPaint, leftContentMaxWidth - 10f)
            yLeft -= 2f
        }

        yLeft += 8f

        if (cvData.skills.isNotEmpty()) {
            drawLeftSectionTitle("SKILLS")
            cvData.skills.forEach { skill ->
                canvas!!.drawText("•", marginLeft + 3f, yLeft + skillsPaint.textSize * 0.75f,
                    PdfTextHelper.createTextPaint(10f, design.accentColor, bold = true))
                drawLeftWrapped(skill, skillsPaint, leftContentMaxWidth - 10f)
            }
        }

        // RIGHT COLUMN

        drawRightText(cvData.fullName.uppercase(), titlePaint, 0f)
        yRight -= 20f
        if (cvData.jobTitle.isNotBlank()) {
            drawRightText(cvData.jobTitle, jobTitlePaint, 13f)
        }

        yRight += 6f
        canvas!!.drawLine(rightColumnStartX + marginLeft, yRight, pageWidth - marginRight, yRight, dividerPaint)
        yRight += 12f

        if (cvData.summary.isNotBlank()) {
            drawRightSectionTitle("PROFESSIONAL SUMMARY")
            drawRightWrapped(cvData.summary, bodyPaint, rightColumnWidth - marginLeft * 2, 10f)
            yRight += 6f
        }

        if (cvData.experiences.isNotEmpty()) {
            drawRightSectionTitle("PROFESSIONAL EXPERIENCE")
            cvData.experiences.forEach { exp ->
                drawRightText(exp.role, subtitlePaint, 12f)
                drawRightText(exp.company, jobTitlePaint, 10f)
                if (exp.dates.isNotBlank()) {
                    val dateX = rightColumnStartX + marginLeft
                    val dateBoxTop = yRight - 1f
                    val datePad = 8f
                    val dateBoxH = 15f
                    val dateTextW = contactPaint.measureText(exp.dates)
                    canvas!!.drawRoundRect(dateX, dateBoxTop, dateX + dateTextW + datePad * 2, dateBoxTop + dateBoxH,
                        40f, 40f, softAccentBg)
                    canvas!!.drawText(exp.dates, dateX + datePad, dateBoxTop + dateBoxH / 2f + contactPaint.textSize * 0.35f,
                        PdfTextHelper.createTextPaint(8.5f, design.sectionTitleColor, bold = true))
                    yRight = dateBoxTop + dateBoxH + 4f
                }
                if (exp.description.isNotBlank()) {
                    drawRightWrapped("•  ${exp.description}", bodyPaint, rightColumnWidth - marginLeft * 2 - 6f, 9.5f)
                }
                yRight += 5f
            }
            yRight += 3f
        }

        if (cvData.education.isNotEmpty()) {
            drawRightSectionTitle("EDUCATION")
            cvData.education.forEach { edu ->
                drawRightText(edu.degree, subtitlePaint, 12f)
                drawRightText(edu.school, bodyPaint, 9.5f)
                if (edu.dates.isNotBlank()) {
                    val dateX = rightColumnStartX + marginLeft
                    val dateBoxTop = yRight - 1f
                    val datePad = 8f
                    val dateBoxH = 15f
                    val dateTextW = contactPaint.measureText(edu.dates)
                    canvas!!.drawRoundRect(dateX, dateBoxTop, dateX + dateTextW + datePad * 2, dateBoxTop + dateBoxH,
                        40f, 40f, softAccentBg)
                    canvas!!.drawText(edu.dates, dateX + datePad, dateBoxTop + dateBoxH / 2f + contactPaint.textSize * 0.35f,
                        PdfTextHelper.createTextPaint(8.5f, design.sectionTitleColor, bold = true))
                    yRight = dateBoxTop + dateBoxH + 5f
                } else {
                    yRight += 5f
                }
            }
        }

        currentPage?.let { document.finishPage(it) }
    }
}
