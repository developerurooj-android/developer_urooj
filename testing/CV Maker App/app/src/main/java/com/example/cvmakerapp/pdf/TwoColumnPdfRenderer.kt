package com.example.cvmakerapp.pdf

import android.content.Context
import android.graphics.pdf.PdfDocument
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.CvTemplate

object TwoColumnPdfRenderer {

    fun render(document: PdfDocument, cvData: CvData, context: Context?) {
        val design = PdfDesigns.forTemplate(CvTemplate.TWO_COLUMN)
        
        // Layout calculations
        val pageWidth = PdfPageWriter.A4_WIDTH
        val pageHeight = PdfPageWriter.A4_HEIGHT
        val marginLeft = 15f
        val marginRight = 15f
        val marginTop = 20f
        val marginBottom = 20f
        
        val leftColumnWidth = (pageWidth * 0.33f).toInt()
        val rightColumnStartX = leftColumnWidth.toFloat()
        val rightColumnWidth = pageWidth - leftColumnWidth - marginRight
        
        // Left column content width (accounting for margins)
        val leftContentMaxWidth = leftColumnWidth - marginLeft * 2 - 4f
        
        // Paint definitions
        val leftSidebarBg = PdfTextHelper.createFillPaint(design.sidebarBackground ?: 0xFFF0F0F0.toInt())
        val titlePaint = PdfTextHelper.createTextPaint(18f, design.sectionTitleColor, bold = true)
        val jobTitlePaint = PdfTextHelper.createTextPaint(11f, design.subtitleColor)
        val sectionTitlePaint = PdfTextHelper.createTextPaint(9.5f, design.accentColor, bold = true)
        val contactPaint = PdfTextHelper.createTextPaint(8f, design.bodyColor)
        val skillsPaint = PdfTextHelper.createTextPaint(8.5f, design.bodyColor)
        val bodyPaint = PdfTextHelper.createTextPaint(9f, design.bodyColor)
        val subtitlePaint = PdfTextHelper.createTextPaint(10f, design.subtitleColor, bold = true)
        val dividerPaint = PdfTextHelper.createLinePaint(design.dividerColor, 0.5f)
        
        // Multi-page tracking
        var pageNumber = 0
        var currentPage: PdfDocument.Page? = null
        var canvas: android.graphics.Canvas? = null
        var yLeft = marginTop
        var yRight = marginTop
        
        fun newPage() {
            currentPage?.let { document.finishPage(it) }
            pageNumber++
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
            currentPage = document.startPage(pageInfo)
            canvas = currentPage!!.canvas
            
            // Redraw sidebar on every page
            canvas!!.drawRect(0f, 0f, leftColumnWidth.toFloat(), pageHeight.toFloat(), leftSidebarBg)
            yLeft = marginTop
            yRight = marginTop
        }
        
        fun drawLeftText(text: String, paint: android.graphics.Paint, lineHeight: Float) {
            if (text.isBlank()) return
            if (yLeft + lineHeight > pageHeight - marginBottom) {
                newPage()
            }
            canvas!!.drawText(text, marginLeft + 3f, yLeft + paint.textSize * 0.75f, paint)
            yLeft += lineHeight
        }
        
        fun drawLeftWrapped(text: String, paint: android.graphics.Paint, maxWidth: Float) {
            if (text.isBlank()) return
            val lines = PdfTextHelper.wrapText(text, paint, maxWidth.toInt())
            lines.forEach { line ->
                if (yLeft + paint.textSize + 6f > pageHeight - marginBottom) {
                    newPage()
                }
                canvas!!.drawText(line, marginLeft + 3f, yLeft + paint.textSize * 0.75f, paint)
                yLeft += paint.textSize + 4f
            }
        }
        
        fun drawRightText(text: String, paint: android.graphics.Paint, lineHeight: Float) {
            if (text.isBlank()) return
            if (yRight + lineHeight > pageHeight - marginBottom) {
                newPage()
            }
            canvas!!.drawText(text, rightColumnStartX + marginLeft, yRight + paint.textSize * 0.75f, paint)
            yRight += lineHeight
        }
        
        fun drawRightWrapped(text: String, paint: android.graphics.Paint, maxWidth: Float, lineHeight: Float = paint.textSize + 4f) {
            if (text.isBlank()) return
            val lines = PdfTextHelper.wrapText(text, paint, maxWidth.toInt())
            lines.forEach { line ->
                if (yRight + lineHeight > pageHeight - marginBottom) {
                    newPage()
                }
                canvas!!.drawText(line, rightColumnStartX + marginLeft, yRight + paint.textSize * 0.75f, paint)
                yRight += lineHeight
            }
        }
        
        // Start rendering
        newPage()
        
        // LEFT SIDEBAR
        
        // Profile Image
        if (!cvData.profileImageUri.isNullOrBlank() && context != null) {
            try {
                val bitmap = PdfImageHelper.loadBitmapFromUri(context, cvData.profileImageUri, 100)
                if (bitmap != null) {
                    val imageSize = 70f
                    val imagePad = (leftColumnWidth - imageSize) / 2f
                    PdfImageHelper.drawCircularImage(canvas!!, bitmap, imagePad, yLeft, imageSize)
                    yLeft += imageSize + 12f
                    android.util.Log.d("TwoColumnPdfRenderer", "Profile image drawn successfully")
                } else {
                    android.util.Log.w("TwoColumnPdfRenderer", "Failed to load bitmap from URI: ${cvData.profileImageUri}")
                }
            } catch (e: Exception) {
                android.util.Log.e("TwoColumnPdfRenderer", "Error drawing profile image: ${e.message}", e)
                e.printStackTrace()
            }
        } else {
            android.util.Log.d("TwoColumnPdfRenderer", "No profile image URI or context available")
        }
        
        // Contact Section (Left)
        yLeft += 4f
        drawLeftText("CONTACT", sectionTitlePaint, 10f)
        canvas!!.drawLine(marginLeft, yLeft - 2f, leftColumnWidth - marginLeft, yLeft - 2f, dividerPaint)
        yLeft += 5f
        
        if (cvData.email.isNotBlank()) {
            drawLeftWrapped(cvData.email, contactPaint, leftContentMaxWidth)
        }
        if (cvData.phone.isNotBlank()) {
            drawLeftText(cvData.phone, contactPaint, 9f)
        }
        if (cvData.location.isNotBlank()) {
            drawLeftText(cvData.location, contactPaint, 9f)
        }
        if (cvData.linkedIn.isNotBlank()) {
            drawLeftWrapped(cvData.linkedIn, contactPaint, leftContentMaxWidth)
        }
        if (cvData.website.isNotBlank()) {
            drawLeftWrapped(cvData.website, contactPaint, leftContentMaxWidth)
        }
        
        yLeft += 4f
        
        // Skills Section (Left)
        if (cvData.skills.isNotEmpty()) {
            drawLeftText("SKILLS", sectionTitlePaint, 10f)
            canvas!!.drawLine(marginLeft, yLeft - 2f, leftColumnWidth - marginLeft, yLeft - 2f, dividerPaint)
            yLeft += 5f
            
            cvData.skills.forEach { skill ->
                drawLeftWrapped("• $skill", skillsPaint, leftContentMaxWidth - 8f)
            }
        }
        
        // RIGHT COLUMN
        
        // Name and Job Title
        drawRightText(cvData.fullName.uppercase(), titlePaint, 20f)
        if (cvData.jobTitle.isNotBlank()) {
            drawRightText(cvData.jobTitle, jobTitlePaint, 12f)
        }
        
        yRight += 6f
        canvas!!.drawLine(rightColumnStartX + marginLeft, yRight, pageWidth - marginRight, yRight, dividerPaint)
        yRight += 10f
        
        // Professional Summary
        if (cvData.summary.isNotBlank()) {
            drawRightText("PROFESSIONAL SUMMARY", sectionTitlePaint, 10f)
            yRight += 3f
            drawRightWrapped(cvData.summary, bodyPaint, rightColumnWidth - marginLeft * 2, 10f)
            yRight += 8f
        }
        
        // Experience
        if (cvData.experiences.isNotEmpty()) {
            drawRightText("PROFESSIONAL EXPERIENCE", sectionTitlePaint, 10f)
            yRight += 4f
            
            cvData.experiences.forEach { exp ->
                drawRightText(exp.role, subtitlePaint, 11f)
                drawRightText(exp.company, jobTitlePaint, 9.5f)
                drawRightText(exp.dates, contactPaint, 8.5f)
                if (exp.description.isNotBlank()) {
                    drawRightWrapped("• ${exp.description}", bodyPaint, rightColumnWidth - marginLeft * 2 - 8f, 9f)
                }
                yRight += 4f
            }
            yRight += 4f
        }
        
        // Education
        if (cvData.education.isNotEmpty()) {
            drawRightText("EDUCATION", sectionTitlePaint, 10f)
            yRight += 4f
            
            cvData.education.forEach { edu ->
                drawRightText(edu.degree, subtitlePaint, 11f)
                drawRightText(edu.school, bodyPaint, 9f)
                drawRightText(edu.dates, contactPaint, 8.5f)
                yRight += 4f
            }
        }
        
        currentPage?.let { document.finishPage(it) }
    }
}
