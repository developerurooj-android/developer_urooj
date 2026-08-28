package com.example.cvmakerapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cvmakerapp.ui.theme.CvIconSystem

/**
 * Renders a contact item with unified icon styling
 * Used in all CV templates to ensure consistency between preview and PDF
 */
@Composable
fun UnifiedContactIcon(
    contactIcon: CvIconSystem.ContactIcon,
    text: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = contactIcon.vectorIcon,
            contentDescription = contactIcon.label,
            modifier = Modifier.size(16.dp),
            tint = accentColor
        )
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray,
            maxLines = 1
        )
    }
}

/**
 * Contact item for Classic template matching PDF layout
 */
@Composable
fun ClassicContactLine(
    icon: CvIconSystem.ContactIcon,
    text: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon.vectorIcon,
            contentDescription = icon.label,
            modifier = Modifier.size(14.dp),
            tint = accentColor
        )
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Color.DarkGray
        )
    }
}

/**
 * Modern template contact line with icon and text
 */
@Composable
fun ModernContactLine(
    text: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    // Parse icon from text or use unified system
    val (icon, value) = when {
        text.contains("@") -> CvIconSystem.Email to text
        text.matches(Regex("""^[\d\s\-\+\(\)]+$""")) -> CvIconSystem.Phone to text
        text.contains(",") || text.contains("st") || text.contains("rd") || text.contains("th") -> CvIconSystem.Location to text
        text.contains("linkedin") -> CvIconSystem.LinkedIn to text
        text.contains("www") || text.contains("http") -> CvIconSystem.Website to text
        else -> CvIconSystem.Website to text
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon.vectorIcon,
            contentDescription = icon.label,
            modifier = Modifier.size(16.dp),
            tint = accentColor
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Color.DarkGray,
            maxLines = 1
        )
    }
}

/**
 * Get unified icon for a contact type
 */
fun getContactIcon(value: String): CvIconSystem.ContactIcon {
    return when {
        value.contains("@") -> CvIconSystem.Email
        value.matches(Regex("""^[\d\s\-\+\(\)]+$""")) -> CvIconSystem.Phone
        value.contains(",") || value.contains("city") -> CvIconSystem.Location
        value.contains("linkedin") || value.contains("in/") -> CvIconSystem.LinkedIn
        value.contains("www") || value.contains("http") -> CvIconSystem.Website
        else -> CvIconSystem.Website
    }
}
