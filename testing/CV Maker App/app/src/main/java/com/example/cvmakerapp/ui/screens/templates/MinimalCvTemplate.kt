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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun MinimalCvTemplate(
    cvData: CvData,
    modifier: Modifier = Modifier
) {
    val design = TemplateDesigns.Minimal

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(design.pageBackground),
        contentPadding = PaddingValues(design.contentPadding),
        verticalArrangement = Arrangement.spacedBy(design.sectionSpacing)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = cvData.fullName.ifBlank { "Your Name" },
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Normal
                        ),
                        color = design.headerTextColor
                    )

                    if (cvData.jobTitle.isNotBlank()) {
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = cvData.jobTitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = design.bodyColor
                        )
                    }
                }

                if (!cvData.profileImageUri.isNullOrBlank()) {
                    GlideImage(
                        model = cvData.profileImageUri,
                        contentDescription = "Profile photo",
                        modifier = Modifier
                            .size(design.profileImageSize)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            val contactParts = listOfNotNull(
                cvData.email.takeIf { it.isNotBlank() },
                cvData.phone.takeIf { it.isNotBlank() },
                cvData.location.takeIf { it.isNotBlank() },
                cvData.linkedIn.takeIf { it.isNotBlank() },
                cvData.website.takeIf { it.isNotBlank() }
            )

            if (contactParts.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    contactParts.forEach { contact ->
                        MinimalContactItem(text = contact)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = design.dividerColor, thickness = 0.5.dp)
        }

        if (cvData.summary.isNotBlank()) {
            item {
                MinimalSection(title = "Summary") {
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
                MinimalSection(title = "Experience") {
                    cvData.experiences.forEach { entry ->
                        MinimalExperienceItem(entry = entry)
                    }
                }
            }
        }

        if (cvData.education.isNotEmpty()) {
            item {
                MinimalSection(title = "Education") {
                    cvData.education.forEach { entry ->
                        MinimalEducationItem(entry = entry)
                    }
                }
            }
        }

        if (cvData.skills.isNotEmpty()) {
            item {
                MinimalSection(title = "Skills") {
                    Text(
                        text = cvData.skills.joinToString(", "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = design.bodyColor
                    )
                }
            }
        }
    }
}

@Composable
private fun MinimalSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title.lowercase().replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Medium,
                letterSpacing = MaterialTheme.typography.labelLarge.letterSpacing
            ),
            color = TemplateDesigns.Minimal.sectionTitleColor
        )

        Spacer(Modifier.height(10.dp))
        content()
        Spacer(Modifier.height(8.dp))
        HorizontalDivider(color = TemplateDesigns.Minimal.dividerColor, thickness = 0.5.dp)
    }
}

@Composable
private fun MinimalExperienceItem(entry: ExperienceEntry) {
    Column(modifier = Modifier.padding(bottom = 14.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = entry.role,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = entry.dates,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = entry.company,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (entry.description.isNotBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = entry.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun MinimalEducationItem(entry: EducationEntry) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(
            text = entry.degree,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onBackground
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = entry.school,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = entry.dates,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun MinimalContactItem(text: String) {
    val iconAndDesc = when {
        text.contains("@") -> Icons.Outlined.Email to "Email"
        text.contains("+") || text.matches(Regex("^[0-9\\s+\\-()]+$")) -> Icons.Rounded.Phone to "Phone"
        text.contains("linkedin", ignoreCase = true) ->
            Icons.Outlined.Person to "LinkedIn"
        text.contains("http", ignoreCase = true) -> Icons.Outlined.Language to "Website"
        else -> Icons.Rounded.LocationOn to "Location"
    }
    val icon = iconAndDesc.first
    val description = iconAndDesc.second

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            modifier = Modifier.size(14.dp),
            tint = TemplateDesigns.Minimal.bodyColor
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = TemplateDesigns.Minimal.bodyColor,
            maxLines = 1
        )
    }
}
