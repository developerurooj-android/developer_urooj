package com.example.cvmakerapp.ui.screens.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.EducationEntry
import com.example.cvmakerapp.data.ExperienceEntry
import com.example.cvmakerapp.data.TemplateDesigns

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ModernCvTemplate(
    cvData: CvData,
    modifier: Modifier = Modifier
) {
    val design = TemplateDesigns.Modern

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(design.pageBackground),
        contentPadding = PaddingValues(bottom = design.contentPadding)
    ) {
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = design.headerBackground ?: design.accentColor
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(design.contentPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!cvData.profileImageUri.isNullOrBlank()) {
                        GlideImage(
                            model = cvData.profileImageUri,
                            contentDescription = "Profile photo",
                            modifier = Modifier
                                .size(design.profileImageSize)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.width(16.dp))
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = cvData.fullName.ifBlank { "Your Name" },
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = design.headerTextColor
                        )

                        if (cvData.jobTitle.isNotBlank()) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = cvData.jobTitle,
                                style = MaterialTheme.typography.titleMedium,
                                color = design.headerTextColor.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(design.contentPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ModernContactLine(text = cvData.email)
                ModernContactLine(text = cvData.phone)
                ModernContactLine(text = cvData.location)
                ModernContactLine(text = cvData.linkedIn)
                ModernContactLine(text = cvData.website)
            }
        }

        if (cvData.summary.isNotBlank()) {
            item {
                ModernSectionCard(title = "Summary", accentColor = design.accentColor) {
                    Text(
                        text = cvData.summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = design.bodyColor
                    )
                }
            }
        }

        if (cvData.experiences.isNotEmpty()) {
            item {
                ModernSectionCard(title = "Experience", accentColor = design.accentColor) {
                    cvData.experiences.forEach { entry ->
                        ModernExperienceItem(entry = entry, accentColor = design.accentColor)
                    }
                }
            }
        }

        if (cvData.education.isNotEmpty()) {
            item {
                ModernSectionCard(title = "Education", accentColor = design.accentColor) {
                    cvData.education.forEach { entry ->
                        ModernEducationItem(entry = entry, accentColor = design.accentColor)
                    }
                }
            }
        }

        if (cvData.skills.isNotEmpty()) {
            item {
                ModernSectionCard(title = "Skills", accentColor = design.accentColor) {
                    Text(
                        text = cvData.skills.joinToString("  •  "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = design.bodyColor
                    )
                }
            }
        }
    }
}

@Composable
private fun ModernContactLine(text: String) {
    if (text.isBlank()) return
    
    val iconAndDesc = when {
        text.contains("@") -> Icons.Outlined.Email to "Email"
        text.contains("+") || text.matches(Regex("^[0-9\\s\\-\\+\\(\\)]+$")) -> Icons.Rounded.Phone to "Phone"
        text.contains("linkedin", ignoreCase = true) ->
            Icons.Outlined.Person to "LinkedIn"
        text.contains("http", ignoreCase = true) -> Icons.Outlined.Language to "Website"
        else -> Icons.Rounded.LocationOn to "Location"
    }
    val icon = iconAndDesc.first
    val description = iconAndDesc.second
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ModernSectionCard(
    title: String,
    accentColor: Color,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = TemplateDesigns.Modern.contentPadding)
            .padding(bottom = TemplateDesigns.Modern.sectionSpacing),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = accentColor
            )
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = accentColor.copy(alpha = 0.3f))
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun ModernExperienceItem(entry: ExperienceEntry, accentColor: Color) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(
            text = entry.role,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = entry.company,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = accentColor
        )
        Text(
            text = entry.dates,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (entry.description.isNotBlank()) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = entry.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ModernEducationItem(entry: EducationEntry, accentColor: Color) {
    Column(modifier = Modifier.padding(bottom = 10.dp)) {
        Text(
            text = entry.degree,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = entry.school,
            style = MaterialTheme.typography.bodyMedium,
            color = accentColor
        )
        Text(
            text = entry.dates,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
