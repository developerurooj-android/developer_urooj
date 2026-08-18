package com.example.cvmakerapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cvmaker.ui.theme.Background
import com.example.cvmaker.ui.theme.CvTypography
import com.example.cvmaker.ui.theme.ErrorColor
import com.example.cvmaker.ui.theme.ErrorContainer
import com.example.cvmaker.ui.theme.InverseOnSurface
import com.example.cvmaker.ui.theme.InversePrimary
import com.example.cvmaker.ui.theme.InverseSurface
import com.example.cvmaker.ui.theme.OnBackground
import com.example.cvmaker.ui.theme.OnError
import com.example.cvmaker.ui.theme.OnErrorContainer
import com.example.cvmaker.ui.theme.OnPrimary
import com.example.cvmaker.ui.theme.OnPrimaryContainer
import com.example.cvmaker.ui.theme.OnSecondary
import com.example.cvmaker.ui.theme.OnSecondaryContainer
import com.example.cvmaker.ui.theme.OnSurface
import com.example.cvmaker.ui.theme.OnSurfaceVariant
import com.example.cvmaker.ui.theme.OnTertiary
import com.example.cvmaker.ui.theme.OnTertiaryContainer
import com.example.cvmaker.ui.theme.Outline
import com.example.cvmaker.ui.theme.OutlineVariant
import com.example.cvmaker.ui.theme.Primary
import com.example.cvmaker.ui.theme.PrimaryContainer
import com.example.cvmaker.ui.theme.Secondary
import com.example.cvmaker.ui.theme.SecondaryContainer
import com.example.cvmaker.ui.theme.Surface
import com.example.cvmaker.ui.theme.SurfaceBright
import com.example.cvmaker.ui.theme.SurfaceContainer
import com.example.cvmaker.ui.theme.SurfaceContainerHigh
import com.example.cvmaker.ui.theme.SurfaceContainerHighest
import com.example.cvmaker.ui.theme.SurfaceContainerLow
import com.example.cvmaker.ui.theme.SurfaceContainerLowest
import com.example.cvmaker.ui.theme.SurfaceDim
import com.example.cvmaker.ui.theme.SurfaceVariant
import com.example.cvmaker.ui.theme.Tertiary
import com.example.cvmaker.ui.theme.TertiaryContainer

// rounded: sm 2px, DEFAULT 4px, md 6px, lg 8px, xl 12px, full pill
val CvShapes = Shapes(
    extraSmall = RoundedCornerShape(2.dp),
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(6.dp),
    large = RoundedCornerShape(8.dp),
    extraLarge = RoundedCornerShape(12.dp)
)

private val CvColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    inversePrimary = InversePrimary,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    error = ErrorColor,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    outlineVariant = OutlineVariant,
    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface,
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,
    surfaceDim = SurfaceDim,
    surfaceBright = SurfaceBright,
    scrim = Color.Black
)

@Composable
fun CVMakerAppTheme(
    content: @Composable () -> Unit
)  {
    MaterialTheme(
        colorScheme = CvColorScheme,
        typography = CvTypography,
        shapes = CvShapes,
        content = content
    )
}
