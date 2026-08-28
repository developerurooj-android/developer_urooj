package com.example.cvmakerapp.pdf

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.CvTemplate
import com.example.cvmakerapp.ui.theme.CvIconSystem

object TwoColumnPdfRenderer {

    fun render(document: PdfDocument, cvData: CvData, context: Context?) {
        val design = PdfDesigns.forTemplate(CvTemplate.TWO_COLUMN)

        val pageWidth = PdfPageWriter.A4_WIDTH
        val pageHeight = PdfPageWriter.A4_HEIGHT
        
        // 32% Sidebar weight from preview
        val leftColumnWidth = (pageWidth * 0.32f).toInt()
        val rightColumnStartX = leftColumnWidth.toFloat()
        val margin = design.margin
        val rightColumnWidth = pageWidth - leftColumnWidth - margin
        
        val leftContentPadding = 18f
        val leftContentMaxWidth = leftColumnWidth - leftContentPadding * 2

        val writer = PdfPageWriter(
            document = document,
            marginLeft = margin,
            marginRight = margin,
            marginTop = 30f,
            marginBottom = 30f
        )

        // Typography matching preview
        val namePaint = PdfTextHelper.createTextPaint(28f, design.headerTextColor, bold = true)
        val jobPaint = PdfTextHelper.createTextPaint(16f, design.accentColor, bold = true)
        val sidebarTitlePaint = PdfTextHelper.createTextPaint(14f, design.accentColor, bold = true)
        val sidebarBodyPaint = PdfTextHelper.createTextPaint(12f, design.sidebarTextColor)
        val sectionTitlePaint = PdfTextHelper.createTextPaint(16f, design.accentColor, bold = true)
        val subtitlePaint = PdfTextHelper.createTextPaint(16f, design.headerTextColor, bold = true)
        val bodyPaint = PdfTextHelper.createTextPaint(14f, design.bodyColor)
        val datePaint = PdfTextHelper.createTextPaint(12f, design.bodyColor)
        
        val sidebarBgPaint = PdfTextHelper.createFillPaint(design.sidebarBackground ?: 0xFFF5F5F5.toInt())
        val accentBarPaint = PdfTextHelper.createFillPaint(design.accentColor)
        val dividerPaint = PdfTextHelper.createLinePaint(design.dividerColor, 0.8f)

        var pageNumber = 0
        var currentPage: PdfDocument.Page? = null
        var canvas: Canvas? = null
        var yLeft = 30f
        var yRight = 30f

        fun drawSidebarBackground() {
            canvas?.drawRect(0f, 0f, leftColumnWidth.toFloat(), pageHeight.toFloat(), sidebarBgPaint)
        }

        fun newPage() {
            currentPage?.let { document.finishPage(it) }
            pageNumber++
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
            currentPage = document.startPage(pageInfo)
            canvas = currentPage!!.canvas
            drawSidebarBackground()
            yLeft = 30f
            yRight = 30f
        }

        fun ensureLeftSpace(needed: Float) {
            if (yLeft + needed > pageHeight - 30f) newPage()
        }

        fun ensureRightSpace(needed: Float) {
            if (yRight + needed > pageHeight - 30f) newPage()
        }

        newPage()

        // ---------------------------------------------------------
        // SIDEBAR (LEFT)
        // ---------------------------------------------------------
        if (!cvData.profileImageUri.isNullOrBlank() && context != null) {
            try {
                val bitmap = PdfImageHelper.loadBitmapFromUri(context, cvData.profileImageUri, 120)
                if (bitmap != null) {
                    val imageSize = design.profileImageSize
                    val imageX = (leftColumnWidth - imageSize) / 2f
                    PdfImageHelper.drawCircularImage(canvas!!, bitmap, imageX, yLeft, imageSize)
                    val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = android.graphics.Color.WHITE
                        style = Paint.Style.STROKE
                        strokeWidth = 3f
                    }
                    canvas!!.drawCircle(imageX + imageSize / 2f, yLeft + imageSize / 2f, imageSize / 2f, borderPaint)
                    yLeft += imageSize + 25f
                }
            } catch (_: Exception) { }
        }

        fun drawLeftSectionTitle(title: String) {
            ensureLeftSpace(35f)
            canvas!!.drawText(title.uppercase(), leftContentPadding, yLeft + sidebarTitlePaint.textSize * 0.8f, sidebarTitlePaint)
            yLeft += sidebarTitlePaint.textSize + 4f
            canvas!!.drawRect(leftContentPadding, yLeft, leftContentPadding + 40f, yLeft + 2f, accentBarPaint)
            yLeft += 15f
        }

        drawLeftSectionTitle("CONTACT")
        
        val contactValues = listOf(
            CvIconSystem.Email.emoji to cvData.email,
            CvIconSystem.Phone.emoji to cvData.phone,
            CvIconSystem.Location.emoji to cvData.location,
            CvIconSystem.LinkedIn.emoji to cvData.linkedIn,
            CvIconSystem.Website.emoji to cvData.website
        )

        contactValues.forEach { (icon, value) ->
            if (value.isNotBlank()) {
                ensureLeftSpace(18f)
                val iconBoxPaint = PdfTextHelper.createFillPaint(android.graphics.Color.argb(30, android.graphics.Color.red(design.accentColor), android.graphics.Color.green(design.accentColor), android.graphics.Color.blue(design.accentColor)))
                canvas!!.drawRoundRect(leftContentPadding, yLeft, leftContentPadding + 16f, yLeft + 16f, 4f, 4f, iconBoxPaint)
                canvas!!.drawText(icon, leftContentPadding + 3f, yLeft + 12f, PdfTextHelper.createTextPaint(CvIconSystem.PdfConstants.ICON_SIZE, design.accentColor, bold = true))
                
                val wrapped = PdfTextHelper.wrapText(value, sidebarBodyPaint, (leftContentMaxWidth - 22).toInt())
                wrapped.forEach { line ->
                    ensureLeftSpace(sidebarBodyPaint.textSize)
                    canvas!!.drawText(line, leftContentPadding + 22f, yLeft + 12f, sidebarBodyPaint)
                    yLeft += sidebarBodyPaint.textSize + 2f
                }
                yLeft += 4f
            }
        }
        
        yLeft += 10f

        if (cvData.skills.isNotEmpty()) {
            drawLeftSectionTitle("SKILLS")
            cvData.skills.forEach { skill ->
                ensureLeftSpace(sidebarBodyPaint.textSize + 8f)
                canvas!!.drawCircle(leftContentPadding + 4f, yLeft + 6f, 2.5f, accentBarPaint)
                val wrapped = PdfTextHelper.wrapText(skill, sidebarBodyPaint, (leftContentMaxWidth - 15).toInt())
                wrapped.forEach { line ->
                    ensureLeftSpace(sidebarBodyPaint.textSize)
                    canvas!!.drawText(line, leftContentPadding + 15f, yLeft + sidebarBodyPaint.textSize * 0.8f, sidebarBodyPaint)
                    yLeft += sidebarBodyPaint.textSize + 2f
                }
                yLeft += 4f
            }
        }

        // ---------------------------------------------------------
        // MAIN COLUMN (RIGHT)
        // ---------------------------------------------------------
        canvas!!.drawText(cvData.fullName.uppercase(), rightColumnStartX + margin, yRight + namePaint.textSize * 0.8f, namePaint)
        yRight += namePaint.textSize + 6f
        if (cvData.jobTitle.isNotBlank()) {
            canvas!!.drawText(cvData.jobTitle, rightColumnStartX + margin, yRight + jobPaint.textSize * 0.8f, jobPaint)
            yRight += jobPaint.textSize + 15f
        } else {
            yRight += 15f
        }

        fun drawMainSectionTitle(title: String) {
            ensureRightSpace(50f)
            val barY = yRight + 2f
            writer.canvas!!.drawRect(rightColumnStartX + margin, barY, rightColumnStartX + margin + 4f, barY + 20f, accentBarPaint)
            canvas!!.drawText(title.uppercase(), rightColumnStartX + margin + 14f, yRight + sectionTitlePaint.textSize * 0.8f + 2f, sectionTitlePaint)
            yRight += sectionTitlePaint.textSize + 8f
            canvas!!.drawLine(rightColumnStartX + margin, yRight, pageWidth - margin, yRight, dividerPaint)
            yRight += 15f
        }

        if (cvData.summary.isNotBlank()) {
            drawMainSectionTitle("Summary")
            val lines = PdfTextHelper.wrapText(cvData.summary, bodyPaint, (rightColumnWidth - margin).toInt())
            lines.forEach { line ->
                ensureRightSpace(bodyPaint.textSize + 3f)
                canvas!!.drawText(line, rightColumnStartX + margin, yRight + bodyPaint.textSize * 0.8f, bodyPaint)
                yRight += bodyPaint.textSize + 3f
            }
            yRight += 12f
        }

        if (cvData.experiences.isNotEmpty()) {
            drawMainSectionTitle("Experience")
            cvData.experiences.forEach { exp ->
                ensureRightSpace(60f)
                canvas!!.drawText(exp.role, rightColumnStartX + margin, yRight + subtitlePaint.textSize * 0.8f, subtitlePaint)
                val dWidth = datePaint.measureText(exp.dates)
                canvas!!.drawText(exp.dates, pageWidth - margin - dWidth, yRight + datePaint.textSize * 0.8f, datePaint)
                yRight += subtitlePaint.textSize + 4f
                
                canvas!!.drawText(exp.company, rightColumnStartX + margin, yRight + bodyPaint.textSize * 0.8f, bodyPaint)
                yRight += bodyPaint.textSize + 8f
                
                if (exp.description.isNotBlank()) {
                    val descLines = PdfTextHelper.wrapText(exp.description, bodyPaint, (rightColumnWidth - margin - 15).toInt())
                    descLines.forEach { line ->
                        ensureRightSpace(bodyPaint.textSize + 3f)
                        canvas!!.drawCircle(rightColumnStartX + margin + 4f, yRight + 7f, 2.5f, accentBarPaint)
                        canvas!!.drawText(line, rightColumnStartX + margin + 15f, yRight + bodyPaint.textSize * 0.8f, bodyPaint)
                        yRight += bodyPaint.textSize + 3f
                    }
                }
                yRight += 12f
            }
        }

        if (cvData.education.isNotEmpty()) {
            drawMainSectionTitle("Education")
            cvData.education.forEach { edu ->
                ensureRightSpace(45f)
                canvas!!.drawText(edu.degree, rightColumnStartX + margin, yRight + subtitlePaint.textSize * 0.8f, subtitlePaint)
                val dWidth = datePaint.measureText(edu.dates)
                canvas!!.drawText(edu.dates, pageWidth - margin - dWidth, yRight + datePaint.textSize * 0.8f, datePaint)
                yRight += subtitlePaint.textSize + 4f
                canvas!!.drawText(edu.school, rightColumnStartX + margin, yRight + bodyPaint.textSize * 0.8f, bodyPaint)
                yRight += bodyPaint.textSize + 10f
            }
        }

        currentPage?.let { document.finishPage(it) }
    }
}
