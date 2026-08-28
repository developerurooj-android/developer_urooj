package com.example.cvmakerapp.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Unified icon system for consistent rendering across UI and PDF
 */
object CvIconSystem {
    
    data class ContactIcon(
        val label: String,
        val emoji: String,
        val vectorIcon: ImageVector
    )
    
    val Email = ContactIcon(
        label = "Email",
        emoji = "✉",
        vectorIcon = Icons.Default.Email
    )
    
    val Phone = ContactIcon(
        label = "Phone",
        emoji = "☎",
        vectorIcon = Icons.Default.Phone
    )
    
    val Location = ContactIcon(
        label = "Location",
        emoji = "📍",
        vectorIcon = Icons.Default.LocationOn
    )
    
    val LinkedIn = ContactIcon(
        label = "LinkedIn",
        emoji = "👤",
        vectorIcon = Icons.Default.Person
    )
    
    val Website = ContactIcon(
        label = "Website",
        emoji = "🌐",
        vectorIcon = Icons.Default.Language
    )
    
    // Ordered contact icons matching data fields
    val contactIcons = listOf(Email, Phone, Location, LinkedIn, Website)
    
    // PDF-specific rendering constants for perfect alignment
    object PdfConstants {
        const val ICON_SIZE = 12f           // Font size for emoji rendering
        const val ICON_BOX_SIZE = 24f       // Size of icon box (for Modern template)
        const val ICON_BOX_RADIUS = 6f      // Corner radius for icon boxes
        const val ICON_TEXT_OFFSET_X = 6f   // X offset within icon box
        const val ICON_TEXT_OFFSET_Y = 17f  // Y offset within icon box (baseline)
        const val TEXT_OFFSET = 18f         // Offset from icon to text
        const val ROW_HEIGHT = 22f          // Height per row in contact grid
        const val ROW_SPACING = 4f          // Space between rows
        const val CONTACT_ITEMS_PER_ROW = 2 // Items per row in contact grid
    }
}
