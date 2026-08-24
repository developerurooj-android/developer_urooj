package com.example.cvmakerapp.pdf

import android.graphics.Color
import com.example.cvmakerapp.data.CvTemplate
import com.example.cvmakerapp.data.TemplateDesigns

data class PdfDesignSpec(
    val accentColor: Int,
    val headerBackground: Int?,
    val headerTextColor: Int,
    val sectionTitleColor: Int,
    val bodyColor: Int,
    val subtitleColor: Int,
    val dividerColor: Int,
    val sidebarBackground: Int?,
    val pageBackground: Int,
    val margin: Float,
    val sectionSpacing: Float,
    val profileImageSize: Float
)

object PdfDesigns {

    private fun composeColorToInt(composeColor: androidx.compose.ui.graphics.Color): Int {
        return Color.argb(
            (composeColor.alpha * 255).toInt(),
            (composeColor.red * 255).toInt(),
            (composeColor.green * 255).toInt(),
            (composeColor.blue * 255).toInt()
        )
    }

    private fun fromTemplateDesign(design: com.example.cvmakerapp.data.TemplateDesign): PdfDesignSpec {
        return PdfDesignSpec(
            accentColor = composeColorToInt(design.accentColor),
            headerBackground = design.headerBackground?.let { composeColorToInt(it) },
            headerTextColor = composeColorToInt(design.headerTextColor),
            sectionTitleColor = composeColorToInt(design.sectionTitleColor),
            bodyColor = composeColorToInt(design.bodyColor),
            subtitleColor = composeColorToInt(design.subtitleColor),
            dividerColor = composeColorToInt(design.dividerColor),
            sidebarBackground = design.sidebarBackground?.let { composeColorToInt(it) },
            pageBackground = composeColorToInt(design.pageBackground),
            margin = 40f,
            sectionSpacing = 14f,
            profileImageSize = when (design.profileImageSize.value) {
                in 80f..130f -> 80f
                in 60f..79f -> 64f
                else -> 72f
            }
        )
    }

    fun forTemplate(template: CvTemplate): PdfDesignSpec = when (template) {
        CvTemplate.CLASSIC -> fromTemplateDesign(TemplateDesigns.Classic)
        CvTemplate.TWO_COLUMN -> fromTemplateDesign(TemplateDesigns.TwoColumn).copy(
            margin = 20f,
            profileImageSize = 80f
        )
        CvTemplate.MODERN -> fromTemplateDesign(TemplateDesigns.Modern).copy(profileImageSize = 72f)
        CvTemplate.MINIMAL -> fromTemplateDesign(TemplateDesigns.Minimal).copy(
            margin = 48f,
            profileImageSize = 64f
        )
        CvTemplate.PROFESSIONAL -> fromTemplateDesign(TemplateDesigns.Professional).copy(
            profileImageSize = 80f
        )
    }
}
