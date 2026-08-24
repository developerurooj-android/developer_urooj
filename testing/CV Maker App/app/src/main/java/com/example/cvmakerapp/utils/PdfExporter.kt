package com.example.cvmakerapp.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.graphics.pdf.PdfDocument
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.CvTemplate
import com.example.cvmakerapp.pdf.ClassicPdfRenderer
import com.example.cvmakerapp.pdf.MinimalPdfRenderer
import com.example.cvmakerapp.pdf.ModernPdfRenderer
import com.example.cvmakerapp.pdf.ProfessionalPdfRenderer
import com.example.cvmakerapp.pdf.TwoColumnPdfRenderer

object PdfExporter {

    fun exportCv(
        context: Context,
        cvData: CvData,
        screenshotBitmap: Bitmap? = null
    ): Boolean {
        return try {
            val document = PdfDocument()

            when (cvData.template) {
                CvTemplate.CLASSIC -> ClassicPdfRenderer.render(document, cvData, context)
                CvTemplate.TWO_COLUMN -> TwoColumnPdfRenderer.render(document, cvData, context)
                CvTemplate.MODERN -> ModernPdfRenderer.render(document, cvData, context)
                CvTemplate.MINIMAL -> MinimalPdfRenderer.render(document, cvData, context)
                CvTemplate.PROFESSIONAL -> ProfessionalPdfRenderer.render(document, cvData, context)
            }

            val cleanName = cvData.fullName
                .trim()
                .replace(" ", "_")
                .ifBlank { "My_CV" }

            val fileName = "${cleanName}_CV.pdf"
            val resolver = context.contentResolver

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }
            }

            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            } else {
                val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloads.exists()) {
                    downloads.mkdirs()
                }

                val file = java.io.File(downloads, fileName)
                document.writeTo(java.io.FileOutputStream(file))
                document.close()
                return true
            }

            if (uri == null) {
                document.close()
                return false
            }

            resolver.openOutputStream(uri)?.use { outputStream ->
                document.writeTo(outputStream)
            }

            document.close()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val updateValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.IS_PENDING, 0)
                }
                resolver.update(uri, updateValues, null, null)
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
