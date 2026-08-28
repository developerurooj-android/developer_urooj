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
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.EducationEntry
import com.example.cvmakerapp.data.ExperienceEntry
import com.example.cvmakerapp.data.TemplateDesigns
import com.example.cvmakerapp.ui.theme.CvIconSystem

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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = cvData.fullName.ifBlank { "Your Name" },
                        style = MaterialTheme.typography.displaySmall?.copy(
                            fontWeight = FontWeight.Light
                        ) ?: MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Light,
                            fontSize = 36.sp
                        ),
                        color = design.headerTextColor,
                        letterSpacing = (-0.5).sp
                    )

                    if (cvData.jobTitle.isNotBlank()) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = cvData.jobTitle,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Light
                            ),
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

            Spacer(Modifier.height(design.sectionSpacing - 4.dp))

            val contactParts = listOfNotNull(
                cvData.email.takeIf { it.isNotBlank() },
                cvData.phone.takeIf { it.isNotBlank() },
                cvData.location.takeIf { it.isNotBlank() },
                cvData.linkedIn.takeIf { it.isNotBlank() },
                cvData.website.takeIf { it.isNotBlank() }
            )

            if (contactParts.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    contactParts.chunked(3).forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            row.forEach { contact ->
                                MinimalContactItem(
                                    text = contact,
                                    bodyColor = design.bodyColor,
                                    accentColor = design.accentColor
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(design.sectionSpacing - 4.dp))
            HorizontalDivider(
                color = design.dividerColor,
                thickness = 0.5.dp
            )
        }

        if (cvData.summary.isNotBlank()) {
            item {
                MinimalSection(title = "Summary", accentColor = design.accentColor) {
                    Text(
                        text = cvData.summary,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Light,
                            lineHeight = 23.sp
                        ),
                        color = design.bodyColor
                    )
                }
            }
        }

        if (cvData.experiences.isNotEmpty()) {
            item {
                MinimalSection(title = "Experience", accentColor = design.accentColor) {
                    cvData.experiences.forEachIndexed { idx, entry ->
                        MinimalExperienceItem(
                            entry = entry,
                            accentColor = design.accentColor,
                            bodyColor = design.bodyColor,
                            isLast = idx == cvData.experiences.size - 1
                        )
                    }
                }
            }
        }

        if (cvData.education.isNotEmpty()) {
            item {
                MinimalSection(title = "Education", accentColor = design.accentColor) {
                    cvData.education.forEachIndexed { idx, entry ->
                        MinimalEducationItem(
                            entry = entry,
                            bodyColor = design.bodyColor,
                            isLast = idx == cvData.education.size - 1
                        )
                    }
                }
            }
        }

        if (cvData.skills.isNotEmpty()) {
            item {
                MinimalSection(title = "Skills", accentColor = design.accentColor) {
                    Text(
                        text = cvData.skills.joinToString("  ·  "),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Light
                        ),
                        color = design.bodyColor,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun MinimalSection(
    title: String,
    accentColor: Color,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp
            ),
            color = accentColor
        )

        Spacer(Modifier.height(12.dp))
        content()
        Spacer(Modifier.height(10.dp))
        HorizontalDivider(
            color = TemplateDesigns.Minimal.dividerColor,
            thickness = 0.5.dp
        )
    }
}

@Composable
private fun MinimalExperienceItem(
    entry: ExperienceEntry,
    accentColor: Color,
    bodyColor: Color,
    isLast: Boolean
) {
    Column(
        modifier = Modifier.padding(bottom = if (isLast) 0.dp else 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.role,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = TemplateDesigns.Classic.headerTextColor
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = entry.company,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Light
                    ),
                    color = accentColor
                )
            }
            Text(
                text = entry.dates,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Light
                ),
                color = bodyColor
            )
        }
        if (entry.description.isNotBlank()) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = entry.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    lineHeight = 22.sp
                ),
                color = bodyColor
            )
        }
    }
}

@Composable
private fun MinimalEducationItem(
    entry: EducationEntry,
    bodyColor: Color,
    isLast: Boolean
) {
    Column(
        modifier = Modifier.padding(bottom = if (isLast) 0.dp else 14.dp)
    ) {
        Text(
            text = entry.degree,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = TemplateDesigns.Classic.headerTextColor
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entry.school,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Light
                ),
                color = bodyColor,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = entry.dates,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Light
                ),
                color = bodyColor
            )
        }
    }
}

@Composable
private fun MinimalContactItem(
    text: String,
    bodyColor: Color,
    accentColor: Color
) {
    val contactIcon = when {
        text.contains("@") -> CvIconSystem.Email
        text.contains("+") || text.matches(Regex("^[0-9\\s+\\-()]+$")) -> CvIconSystem.Phone
        text.contains("linkedin", ignoreCase = true) -> CvIconSystem.LinkedIn
        text.contains("http", ignoreCase = true) -> CvIconSystem.Website
        else -> CvIconSystem.Location
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = contactIcon.vectorIcon,
            contentDescription = contactIcon.label,
            modifier = Modifier.size(13.dp),
            tint = accentColor
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Light
            ),
            color = bodyColor,
            maxLines = 1
        )
    }
}
