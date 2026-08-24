package com.example.cvmakerapp.ui.screens.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
fun ProfessionalCvTemplate(
    cvData: CvData,
    modifier: Modifier = Modifier
) {
    val design = TemplateDesigns.Professional

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(design.pageBackground),
        contentPadding = PaddingValues(bottom = design.contentPadding)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(design.headerBackground ?: design.accentColor)
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(design.contentPadding),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = cvData.fullName.ifBlank { "Your Name" }.uppercase(),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = design.sectionTitleColor
                    )

                    if (cvData.jobTitle.isNotBlank()) {
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = cvData.jobTitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = design.subtitleColor
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    ProfessionalContactBlock(cvData = cvData)
                }

                if (!cvData.profileImageUri.isNullOrBlank()) {
                    Spacer(Modifier.width(16.dp))
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

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = design.contentPadding),
                color = design.dividerColor
            )
        }

        if (cvData.summary.isNotBlank()) {
            item {
                ProfessionalSection(title = "Professional Summary", accentColor = design.accentColor) {
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
                ProfessionalSection(title = "Professional Experience", accentColor = design.accentColor) {
                    cvData.experiences.forEach { entry ->
                        ProfessionalExperienceItem(entry = entry, accentColor = design.accentColor)
                    }
                }
            }
        }

        if (cvData.education.isNotEmpty()) {
            item {
                ProfessionalSection(title = "Education", accentColor = design.accentColor) {
                    cvData.education.forEach { entry ->
                        ProfessionalEducationItem(entry = entry)
                    }
                }
            }
        }

        if (cvData.skills.isNotEmpty()) {
            item {
                ProfessionalSection(title = "Core Skills", accentColor = design.accentColor) {
                    cvData.skills.forEach { skill ->
                        Row(
                            modifier = Modifier.padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 4.dp, height = 4.dp)
                                    .background(design.accentColor)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = skill,
                                style = MaterialTheme.typography.bodyMedium,
                                color = design.bodyColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfessionalContactBlock(cvData: CvData) {
    val lines = listOfNotNull(
        cvData.email.takeIf { it.isNotBlank() },
        cvData.phone.takeIf { it.isNotBlank() },
        cvData.location.takeIf { it.isNotBlank() },
        cvData.linkedIn.takeIf { it.isNotBlank() },
        cvData.website.takeIf { it.isNotBlank() }
    )

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        lines.forEach { line ->
            ProfessionalContactItem(text = line)
        }
    }
}

@Composable
private fun ProfessionalContactItem(text: String) {
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
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            modifier = Modifier.size(14.dp),
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
private fun ProfessionalSection(
    title: String,
    accentColor: Color,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = TemplateDesigns.Professional.contentPadding)
            .padding(bottom = TemplateDesigns.Professional.sectionSpacing)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(18.dp)
                    .background(accentColor)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = accentColor
            )
        }

        Spacer(Modifier.height(12.dp))
        content()
    }
}

@Composable
private fun ProfessionalExperienceItem(entry: ExperienceEntry, accentColor: Color) {
    Column(modifier = Modifier.padding(bottom = 14.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = entry.role,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = entry.dates,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = entry.company,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = accentColor
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
private fun ProfessionalEducationItem(entry: EducationEntry) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(
            text = entry.degree,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = entry.school,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = entry.dates,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
