package com.example.cvmakerapp.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cvmakerapp.ui.theme.AccentIndigo
import com.example.cvmakerapp.ui.theme.AccentSlate
import com.example.cvmakerapp.ui.theme.AccentTeal
import com.example.cvmakerapp.ui.theme.CvPageCream
import com.example.cvmakerapp.ui.theme.CvPageSoftBlue
import com.example.cvmakerapp.ui.theme.CvPageWhite
import com.example.cvmakerapp.ui.theme.OnBackground
import com.example.cvmakerapp.ui.theme.OnSurfaceVariant
import com.example.cvmakerapp.ui.theme.OutlineVariant
import com.example.cvmakerapp.ui.theme.Primary
import com.example.cvmakerapp.ui.theme.Secondary
import com.example.cvmakerapp.ui.theme.Tertiary

data class TemplateDesign(
    val displayName: String,
    val accentColor: Color,
    val accentColorSoft: Color,
    val headerBackground: Color?,
    val headerTextColor: Color,
    val sectionTitleColor: Color,
    val bodyColor: Color,
    val subtitleColor: Color,
    val dividerColor: Color,
    val sidebarBackground: Color?,
    val sidebarTextColor: Color,
    val pageBackground: Color,
    val contentPadding: Dp,
    val sectionSpacing: Dp,
    val profileImageSize: Dp,
    val cardCornerRadius: Dp,
    val sectionCardBackground: Color? = null
)

object TemplateDesigns {

    val Classic = TemplateDesign(
        displayName = "Classic",
        accentColor = Primary,
        accentColorSoft = Primary.copy(alpha = 0.08f),
        headerBackground = null,
        headerTextColor = OnBackground,
        sectionTitleColor = OnBackground,
        bodyColor = OnSurfaceVariant,
        subtitleColor = Primary,
        dividerColor = Primary.copy(alpha = 0.25f),
        sidebarBackground = null,
        sidebarTextColor = OnBackground,
        pageBackground = CvPageWhite,
        contentPadding = 20.dp,
        sectionSpacing = 18.dp,
        profileImageSize = 88.dp,
        cardCornerRadius = 0.dp
    )

    val TwoColumn = TemplateDesign(
        displayName = "Two Column",
        accentColor = AccentIndigo,
        accentColorSoft = AccentIndigo.copy(alpha = 0.10f),
        headerBackground = null,
        headerTextColor = OnBackground,
        sectionTitleColor = OnBackground,
        bodyColor = OnSurfaceVariant,
        subtitleColor = AccentIndigo,
        dividerColor = AccentIndigo.copy(alpha = 0.35f),
        sidebarBackground = AccentIndigo.copy(alpha = 0.08f),
        sidebarTextColor = OnBackground,
        pageBackground = CvPageWhite,
        contentPadding = 22.dp,
        sectionSpacing = 16.dp,
        profileImageSize = 128.dp,
        cardCornerRadius = 10.dp
    )

    val Modern = TemplateDesign(
        displayName = "Modern",
        accentColor = AccentTeal,
        accentColorSoft = AccentTeal.copy(alpha = 0.10f),
        headerBackground = AccentTeal,
        headerTextColor = Color.White,
        sectionTitleColor = AccentTeal,
        bodyColor = OnSurfaceVariant,
        subtitleColor = AccentTeal,
        dividerColor = AccentTeal.copy(alpha = 0.20f),
        sidebarBackground = null,
        sidebarTextColor = OnBackground,
        pageBackground = CvPageSoftBlue,
        contentPadding = 22.dp,
        sectionSpacing = 14.dp,
        profileImageSize = 76.dp,
        cardCornerRadius = 14.dp,
        sectionCardBackground = Color.White
    )

    val Minimal = TemplateDesign(
        displayName = "Minimal",
        accentColor = AccentSlate,
        accentColorSoft = AccentSlate.copy(alpha = 0.06f),
        headerBackground = null,
        headerTextColor = OnBackground,
        sectionTitleColor = AccentSlate,
        bodyColor = OnSurfaceVariant,
        subtitleColor = OnBackground,
        dividerColor = OutlineVariant,
        sidebarBackground = null,
        sidebarTextColor = OnBackground,
        pageBackground = CvPageCream,
        contentPadding = 28.dp,
        sectionSpacing = 22.dp,
        profileImageSize = 68.dp,
        cardCornerRadius = 4.dp
    )

    val Professional = TemplateDesign(
        displayName = "Professional",
        accentColor = Primary,
        accentColorSoft = Primary.copy(alpha = 0.10f),
        headerBackground = null,
        headerTextColor = OnBackground,
        sectionTitleColor = Primary,
        bodyColor = OnSurfaceVariant,
        subtitleColor = Secondary,
        dividerColor = OutlineVariant,
        sidebarBackground = null,
        sidebarTextColor = OnBackground,
        pageBackground = CvPageWhite,
        contentPadding = 22.dp,
        sectionSpacing = 16.dp,
        profileImageSize = 92.dp,
        cardCornerRadius = 8.dp
    )

    fun forTemplate(template: CvTemplate): TemplateDesign = when (template) {
        CvTemplate.CLASSIC -> Classic
        CvTemplate.TWO_COLUMN -> TwoColumn
        CvTemplate.MODERN -> Modern
        CvTemplate.MINIMAL -> Minimal
        CvTemplate.PROFESSIONAL -> Professional
    }
}
