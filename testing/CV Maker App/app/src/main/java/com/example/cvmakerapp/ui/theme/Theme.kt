package com.example.cvmakerapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cvmakerapp.ui.theme.Background
import com.example.cvmakerapp.ui.theme.CvTypography
import com.example.cvmakerapp.ui.theme.ErrorColor
import com.example.cvmakerapp.ui.theme.ErrorContainer
import com.example.cvmakerapp.ui.theme.InverseOnSurface
import com.example.cvmakerapp.ui.theme.InversePrimary
import com.example.cvmakerapp.ui.theme.InverseSurface
import com.example.cvmakerapp.ui.theme.OnBackground
import com.example.cvmakerapp.ui.theme.OnError
import com.example.cvmakerapp.ui.theme.OnErrorContainer
import com.example.cvmakerapp.ui.theme.OnPrimary
import com.example.cvmakerapp.ui.theme.OnPrimaryContainer
import com.example.cvmakerapp.ui.theme.OnSecondary
import com.example.cvmakerapp.ui.theme.OnSecondaryContainer
import com.example.cvmakerapp.ui.theme.OnSurface
import com.example.cvmakerapp.ui.theme.OnSurfaceVariant
import com.example.cvmakerapp.ui.theme.OnTertiary
import com.example.cvmakerapp.ui.theme.OnTertiaryContainer
import com.example.cvmakerapp.ui.theme.Outline
import com.example.cvmakerapp.ui.theme.OutlineVariant
import com.example.cvmakerapp.ui.theme.Primary
import com.example.cvmakerapp.ui.theme.PrimaryContainer
import com.example.cvmakerapp.ui.theme.Secondary
import com.example.cvmakerapp.ui.theme.SecondaryContainer
import com.example.cvmakerapp.ui.theme.Surface
import com.example.cvmakerapp.ui.theme.SurfaceBright
import com.example.cvmakerapp.ui.theme.SurfaceContainer
import com.example.cvmakerapp.ui.theme.SurfaceContainerHigh
import com.example.cvmakerapp.ui.theme.SurfaceContainerHighest
import com.example.cvmakerapp.ui.theme.SurfaceContainerLow
import com.example.cvmakerapp.ui.theme.SurfaceContainerLowest
import com.example.cvmakerapp.ui.theme.SurfaceDim
import com.example.cvmakerapp.ui.theme.SurfaceVariant
import com.example.cvmakerapp.ui.theme.Tertiary
import com.example.cvmakerapp.ui.theme.TertiaryContainer

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
