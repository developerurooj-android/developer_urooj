package com.example.cvmakerapp.pdf

import android.graphics.Paint

object PdfTextHelper {

    fun wrapText(
        text: String,
        paint: Paint,
        maxWidth: Int
    ): List<String> {
        if (text.isBlank()) return emptyList()

        val lines = mutableListOf<String>()
        val words = text.split(" ")
        var currentLine = ""

        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            if (paint.measureText(testLine) <= maxWidth) {
                currentLine = testLine
            } else {
                if (currentLine.isNotEmpty()) {
                    lines.add(currentLine)
                }
                currentLine = word
            }
        }

        if (currentLine.isNotEmpty()) {
            lines.add(currentLine)
        }

        return lines
    }

    fun createTextPaint(
        textSize: Float,
        color: Int,
        bold: Boolean = false
    ): Paint = Paint().apply {
        isAntiAlias = true
        this.textSize = textSize
        this.color = color
        isFakeBoldText = bold
    }

    fun createLinePaint(
        color: Int,
        strokeWidth: Float = 0.5f
    ): Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        this.strokeWidth = strokeWidth
        this.color = color
    }

    fun createFillPaint(color: Int): Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        this.color = color
    }
}
