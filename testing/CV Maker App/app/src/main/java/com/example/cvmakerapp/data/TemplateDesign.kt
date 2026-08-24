package com.example.cvmakerapp.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cvmakerapp.ui.theme.OnBackground
import com.example.cvmakerapp.ui.theme.OnSurfaceVariant
import com.example.cvmakerapp.ui.theme.OutlineVariant
import com.example.cvmakerapp.ui.theme.Primary
import com.example.cvmakerapp.ui.theme.PrimaryContainer
import com.example.cvmakerapp.ui.theme.Secondary
import com.example.cvmakerapp.ui.theme.SurfaceContainerLowest
import com.example.cvmakerapp.ui.theme.SurfaceTint

data class TemplateDesign(
    val displayName: String,
    val accentColor: Color,
    val headerBackground: Color?,
    val headerTextColor: Color,
    val sectionTitleColor: Color,
    val bodyColor: Color,
    val subtitleColor: Color,
    val dividerColor: Color,
    val sidebarBackground: Color?,
    val pageBackground: Color,
    val contentPadding: Dp,
    val sectionSpacing: Dp,
    val profileImageSize: Dp
)

object TemplateDesigns {

    val Classic = TemplateDesign(
        displayName = "Classic",
        accentColor = Primary,
        headerBackground = null,
        headerTextColor = OnBackground,
        sectionTitleColor = OnBackground,
        bodyColor = OnSurfaceVariant,
        subtitleColor = Primary,
        dividerColor = OutlineVariant,
        sidebarBackground = null,
        pageBackground = SurfaceContainerLowest,
        contentPadding = 16.dp,
        sectionSpacing = 16.dp,
        profileImageSize = 80.dp
    )

    val TwoColumn = TemplateDesign(
        displayName = "Two Column",
        accentColor = Primary,
        headerBackground = null,
        headerTextColor = OnBackground,
        sectionTitleColor = OnBackground,
        bodyColor = OnSurfaceVariant,
        subtitleColor = Primary,
        dividerColor = Primary.copy(alpha = 0.5f),
        sidebarBackground = Primary.copy(alpha = 0.08f),
        pageBackground = SurfaceContainerLowest,
        contentPadding = 20.dp,
        sectionSpacing = 16.dp,
        profileImageSize = 120.dp
    )

    val Modern = TemplateDesign(
        displayName = "Modern",
        accentColor = Secondary,
        headerBackground = Secondary,
        headerTextColor = Color.White,
        sectionTitleColor = Secondary,
        bodyColor = OnSurfaceVariant,
        subtitleColor = Secondary,
        dividerColor = OutlineVariant,
        sidebarBackground = null,
        pageBackground = SurfaceContainerLowest,
        contentPadding = 20.dp,
        sectionSpacing = 14.dp,
        profileImageSize = 72.dp
    )

    val Minimal = TemplateDesign(
        displayName = "Minimal",
        accentColor = OnBackground,
        headerBackground = null,
        headerTextColor = OnBackground,
        sectionTitleColor = OnBackground,
        bodyColor = OnSurfaceVariant,
        subtitleColor = OnBackground,
        dividerColor = OutlineVariant,
        sidebarBackground = null,
        pageBackground = SurfaceContainerLowest,
        contentPadding = 24.dp,
        sectionSpacing = 20.dp,
        profileImageSize = 64.dp
    )

    val Professional = TemplateDesign(
        displayName = "Professional",
        accentColor = PrimaryContainer,
        headerBackground = PrimaryContainer,
        headerTextColor = Color.White,
        sectionTitleColor = PrimaryContainer,
        bodyColor = OnSurfaceVariant,
        subtitleColor = SurfaceTint,
        dividerColor = OutlineVariant,
        sidebarBackground = null,
        pageBackground = SurfaceContainerLowest,
        contentPadding = 20.dp,
        sectionSpacing = 14.dp,
        profileImageSize = 88.dp
    )

    fun forTemplate(template: CvTemplate): TemplateDesign = when (template) {
        CvTemplate.CLASSIC -> Classic
        CvTemplate.TWO_COLUMN -> TwoColumn
        CvTemplate.MODERN -> Modern
        CvTemplate.MINIMAL -> Minimal
        CvTemplate.PROFESSIONAL -> Professional
    }
}
