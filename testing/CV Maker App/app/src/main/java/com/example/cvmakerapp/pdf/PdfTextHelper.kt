package com.example.cvmakerapp.pdf

import android.graphics.Paint
import android.graphics.Typeface

object PdfTextHelper {

    val defaultTypeface: Typeface = Typeface.create("sans-serif", Typeface.NORMAL)
    val defaultTypefaceBold: Typeface = Typeface.create("sans-serif", Typeface.BOLD)

    fun wrapText(
        text: String,
        paint: Paint,
        maxWidth: Int
    ): List<String> {
        if (text.isBlank()) return emptyList()

        val lines = mutableListOf<String>()
        val paragraphs = text.split("\n")

        for (paragraph in paragraphs) {
            val words = paragraph.split(" ")
            var currentLine = ""

            for (word in words) {
                // Handle extremely long words/links that exceed maxWidth by themselves
                if (paint.measureText(word) > maxWidth) {
                    if (currentLine.isNotEmpty()) {
                        lines.add(currentLine)
                        currentLine = ""
                    }
                    
                    // Break the long word into sub-chunks
                    var remainingWord = word
                    while (remainingWord.isNotEmpty()) {
                        var breakIndex = paint.breakText(remainingWord, true, maxWidth.toFloat(), null)
                        if (breakIndex == 0 && remainingWord.isNotEmpty()) breakIndex = 1
                        
                        lines.add(remainingWord.substring(0, breakIndex))
                        remainingWord = remainingWord.substring(breakIndex)
                    }
                    continue
                }

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
        typeface = if (bold) defaultTypefaceBold else defaultTypeface
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
