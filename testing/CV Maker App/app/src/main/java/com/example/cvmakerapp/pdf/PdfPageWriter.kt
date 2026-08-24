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
    val marginBottom: Float = 40f,
    private val drawPageBackground: ((Canvas, Int, Int, Int) -> Unit)? = null
) {
    companion object {
        const val A4_WIDTH = 595
        const val A4_HEIGHT = 842
    }

    private var pageNumber = 0
    private var currentPage: PdfDocument.Page? = null
    var canvas: Canvas? = null
        private set

    var y: Float = marginTop
        private set

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
        drawPageBackground?.invoke(canvas!!, pageWidth, pageHeight, pageNumber)
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

    fun setY(value: Float) {
        y = value
    }

    fun drawLine(x1: Float, y1: Float, x2: Float, y2: Float, paint: Paint) {
        canvas?.drawLine(x1, y1, x2, y2, paint)
    }

    fun drawRect(left: Float, top: Float, right: Float, bottom: Float, paint: Paint) {
        canvas?.drawRect(left, top, right, bottom, paint)
    }

    fun drawTextLine(text: String, x: Float, paint: Paint, lineHeight: Float = paint.textSize + 4f) {
        if (text.isBlank()) return
        ensureSpace(lineHeight)
        canvas?.drawText(text, x, y + paint.textSize * 0.75f, paint)
        y += lineHeight
    }

    fun drawWrappedText(
        text: String,
        x: Float,
        maxWidth: Float,
        paint: Paint,
        lineHeight: Float = paint.textSize + 4f,
        indent: Float = 0f
    ) {
        if (text.isBlank()) return
        val lines = PdfTextHelper.wrapText(text, paint, maxWidth.toInt())
        for (line in lines) {
            ensureSpace(lineHeight)
            canvas?.drawText(line, x + indent, y + paint.textSize * 0.75f, paint)
            y += lineHeight
        }
    }

    fun finish() {
        currentPage?.let { document.finishPage(it) }
        currentPage = null
        canvas = null
    }
}

// ================== PDF LAYOUT HELPERS ==================

class PdfLayoutManager(
    val pageWidth: Int = PdfPageWriter.A4_WIDTH,
    val pageHeight: Int = PdfPageWriter.A4_HEIGHT,
    val marginLeft: Float = 40f,
    val marginRight: Float = 40f,
    val marginTop: Float = 40f,
    val marginBottom: Float = 40f
) {
    val contentWidth: Float get() = (pageWidth - marginLeft - marginRight)
    val maxContentHeight: Float get() = (pageHeight - marginTop - marginBottom)
    val maxY: Float get() = pageHeight - marginBottom
    
    fun getLeftColumnWidth(totalWidth: Float = contentWidth, ratio: Float = 0.35f): Float {
        return totalWidth * ratio
    }
    
    fun getRightColumnStart(leftWidth: Float): Float {
        return marginLeft + leftWidth
    }
    
    fun getRightColumnWidth(leftWidth: Float): Float {
        return contentWidth - leftWidth
    }
}

object PdfImageHelper {

    fun loadBitmapFromUri(context: Context, uri: String, size: Int = 120): Bitmap? {
        return try {
            // Handle different URI types
            val imageUri = when {
                uri.startsWith("file://") -> uri
                uri.startsWith("/") -> "file://$uri"
                else -> uri
            }
            
            // Try loading with Glide
            val bitmap = try {
                Glide.with(context)
                    .asBitmap()
                    .load(imageUri)
                    .override(size, size)
                    .fitCenter()
                    .submit(size, size)
                    .get(15, java.util.concurrent.TimeUnit.SECONDS)
            } catch (glideException: Exception) {
                android.util.Log.e("PdfImageHelper", "Glide failed: ${glideException.message}")
                
                // Fallback: Try direct file loading for file:// URIs
                if (imageUri.startsWith("file://")) {
                    val filePath = imageUri.replace("file://", "")
                    loadBitmapFromFile(filePath, size)
                } else {
                    // Try as content URI
                    loadBitmapFromContentUri(context, imageUri, size)
                }
            }
            
            bitmap
        } catch (e: Exception) {
            android.util.Log.e("PdfImageHelper", "Failed to load bitmap from URI: $uri", e)
            e.printStackTrace()
            null
        }
    }

    private fun loadBitmapFromFile(filePath: String, size: Int): Bitmap? {
        return try {
            val options = android.graphics.BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            android.graphics.BitmapFactory.decodeFile(filePath, options)
            
            options.inSampleSize = calculateInSampleSize(options, size, size)
            options.inJustDecodeBounds = false
            
            val bitmap = android.graphics.BitmapFactory.decodeFile(filePath, options)
            if (bitmap != null) {
                android.graphics.Bitmap.createScaledBitmap(bitmap, size, size, true)
            } else {
                null
            }
        } catch (e: Exception) {
            android.util.Log.e("PdfImageHelper", "Failed to load from file: $filePath", e)
            null
        }
    }

    private fun loadBitmapFromContentUri(context: Context, uri: String, size: Int): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(android.net.Uri.parse(uri))
            if (inputStream != null) {
                val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                if (bitmap != null) {
                    android.graphics.Bitmap.createScaledBitmap(bitmap, size, size, true)
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            android.util.Log.e("PdfImageHelper", "Failed to load from content URI: $uri", e)
            null
        }
    }

    private fun calculateInSampleSize(
        options: android.graphics.BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
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
