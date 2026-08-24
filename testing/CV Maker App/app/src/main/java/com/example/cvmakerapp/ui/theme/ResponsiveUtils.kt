package com.example.cvmakerapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Responsive sizing utilities for mobile, tablet, and large screens
 */
object ResponsiveUtils {

    /**
     * Get current screen width
     */
    @Composable
    fun getScreenWidthDp(): Dp {
        return LocalConfiguration.current.screenWidthDp.dp
    }

    /**
     * Get current screen height
     */
    @Composable
    fun getScreenHeightDp(): Dp {
        return LocalConfiguration.current.screenHeightDp.dp
    }

    /**
     * Determine screen size class
     * - Small: < 400dp (phones)
     * - Medium: 400-600dp (larger phones)
     * - Large: >= 600dp (tablets)
     */
    @Composable
    fun getScreenSizeClass(): ScreenSizeClass {
        val screenWidth = getScreenWidthDp()
        return when {
            screenWidth < 400.dp -> ScreenSizeClass.SMALL
            screenWidth < 600.dp -> ScreenSizeClass.MEDIUM
            else -> ScreenSizeClass.LARGE
        }
    }

    /**
     * Get responsive padding based on screen size
     */
    @Composable
    fun getResponsivePadding(): Dp {
        return when (getScreenSizeClass()) {
            ScreenSizeClass.SMALL -> 12.dp
            ScreenSizeClass.MEDIUM -> 16.dp
            ScreenSizeClass.LARGE -> 24.dp
        }
    }

    /**
     * Get responsive spacing based on screen size
     */
    @Composable
    fun getResponsiveSpacing(): Dp {
        return when (getScreenSizeClass()) {
            ScreenSizeClass.SMALL -> 8.dp
            ScreenSizeClass.MEDIUM -> 12.dp
            ScreenSizeClass.LARGE -> 16.dp
        }
    }

    /**
     * Get responsive content max width (for tablets)
     */
    @Composable
    fun getMaxContentWidth(): Dp {
        return when (getScreenSizeClass()) {
            ScreenSizeClass.SMALL -> 10000.dp // Unlimited for phones
            ScreenSizeClass.MEDIUM -> 10000.dp // Unlimited for medium phones
            ScreenSizeClass.LARGE -> 800.dp // Cap at 800dp for tablets
        }
    }

    /**
     * Get responsive grid columns for template selection
     */
    @Composable
    fun getTemplateGridColumns(): Int {
        return when (getScreenSizeClass()) {
            ScreenSizeClass.SMALL -> 1 // 1 column on small phones
            ScreenSizeClass.MEDIUM -> 2 // 2 columns on medium phones
            ScreenSizeClass.LARGE -> 3 // 3 columns on tablets
        }
    }

    /**
     * Get responsive button height
     */
    @Composable
    fun getResponsiveButtonHeight(): Dp {
        return when (getScreenSizeClass()) {
            ScreenSizeClass.SMALL -> 40.dp
            ScreenSizeClass.MEDIUM -> 44.dp
            ScreenSizeClass.LARGE -> 48.dp
        }
    }

    /**
     * Get responsive image height (for profile photo upload)
     */
    @Composable
    fun getResponsiveImageHeight(): Dp {
        val screenHeight = getScreenHeightDp()
        return when (getScreenSizeClass()) {
            ScreenSizeClass.SMALL -> minOf(200.dp, screenHeight * 0.3f) // 30% of screen or 200dp
            ScreenSizeClass.MEDIUM -> minOf(260.dp, screenHeight * 0.35f) // 35% of screen or 260dp
            ScreenSizeClass.LARGE -> 300.dp // Fixed 300dp on tablets
        }
    }

    /**
     * Get responsive headline text size modifier
     * For screens < 400dp, use smaller sizes
     */
    @Composable
    fun shouldUseCompactTextSizes(): Boolean {
        return getScreenSizeClass() == ScreenSizeClass.SMALL
    }

    /**
     * Get horizontal padding that scales with screen
     * Ensures buttons/text fields have proper margins on all screens
     */
    @Composable
    fun getResponsiveHorizontalPadding(): Dp {
        return when (getScreenSizeClass()) {
            ScreenSizeClass.SMALL -> 12.dp
            ScreenSizeClass.MEDIUM -> 16.dp
            ScreenSizeClass.LARGE -> 20.dp
        }
    }
}

enum class ScreenSizeClass {
    SMALL,   // < 400dp width (small phones like Pixel 3a)
    MEDIUM,  // 400-600dp width (most Android phones)
    LARGE    // >= 600dp width (tablets and foldables)
}
