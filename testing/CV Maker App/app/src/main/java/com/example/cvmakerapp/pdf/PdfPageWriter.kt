package com.example.cvmakerapp.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.bumptech.glide.Glide

class PdfPageWriter(
    private val document: PdfDocument,
    val pageWidth: Int = A4_WIDTH,
    val pageHeight: Int = A4_HEIGHT,
    val marginLeft: Float = 40f,
    val marginRight: Float = 40f,
    val marginTop: Float = 40f,
    val marginBottom: Float = 40f
) {
    companion object {
        const val A4_WIDTH = 595 // Standard A4 points (approx 8.27in)
        const val A4_HEIGHT = 842 // Standard A4 points (approx 11.69in)
    }

    private var pageNumber = 0
    private var currentPage: PdfDocument.Page? = null
    var canvas: Canvas? = null
        private set

    var y: Float = marginTop

    val contentWidth: Float
        get() = pageWidth - marginLeft - marginRight

    val maxY: Float
        get() = pageHeight - marginBottom

    fun start() {
        newPage()
    }

    fun newPage() {
        currentPage?.let { document.finishPage(it) }
        pageNumber++
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        currentPage = document.startPage(pageInfo)
        canvas = currentPage!!.canvas
        y = marginTop
    }

    fun ensureSpace(needed: Float) {
        if (y + needed > maxY) {
            newPage()
        }
    }

    fun advance(by: Float) {
        y += by
    }

    fun drawLine(x1: Float, y1: Float, x2: Float, y2: Float, paint: Paint) {
        canvas?.drawLine(x1, y1, x2, y2, paint)
    }

    fun drawTextLine(text: String, x: Float, paint: Paint, lineHeight: Float = paint.textSize) {
        if (text.isBlank()) return
        ensureSpace(lineHeight)
        canvas?.drawText(text, x, y + paint.textSize * 0.8f, paint)
        y += lineHeight
    }

    fun finish() {
        currentPage?.let { document.finishPage(it) }
        currentPage = null
        canvas = null
    }
}

object PdfImageHelper {

    fun loadBitmapFromUri(context: Context, uri: String, size: Int = 200): Bitmap? {
        return try {
            android.util.Log.d("PdfImageHelper", "Loading image from URI: $uri")
            val parsedUri = android.net.Uri.parse(uri)

            // Try to open input stream directly first (often more reliable for local files in PDF context)
            val inputStream = context.contentResolver.openInputStream(parsedUri)
            if (inputStream != null) {
                val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                if (bitmap != null) {
                    return Bitmap.createScaledBitmap(bitmap, size, size, true)
                }
            }

            // Fallback to Glide if stream fails
            Glide.with(context)
                .asBitmap()
                .load(parsedUri)
                .override(size, size)
                .centerCrop()
                .submit()
                .get(5, java.util.concurrent.TimeUnit.SECONDS)
        } catch (e: Exception) {
            android.util.Log.e("PdfImageHelper", "Failed to load bitmap from URI: $uri", e)
            null
        }
    }

    fun drawCircularImage(
        canvas: Canvas,
        bitmap: Bitmap,
        x: Float,
        y: Float,
        size: Float
    ) {
        try {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            val scaled = Bitmap.createScaledBitmap(bitmap, size.toInt(), size.toInt(), true)
            val path = android.graphics.Path().apply {
                addCircle(x + size / 2f, y + size / 2f, size / 2f, android.graphics.Path.Direction.CW)
            }
            canvas.save()
            canvas.clipPath(path)
            canvas.drawBitmap(scaled, x, y, paint)
            canvas.restore()
        } catch (e: Exception) {
            android.util.Log.e("PdfImageHelper", "Failed to draw circular image", e)
        }
    }
}
