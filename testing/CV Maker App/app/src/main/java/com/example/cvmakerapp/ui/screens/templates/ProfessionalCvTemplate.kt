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
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
                    .height(6.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                design.accentColor,
                                design.accentColor.copy(alpha = 0.6f),
                                design.subtitleColor
                            )
                        )
                    )
            )
        }

        item {
            Column(modifier = Modifier.padding(horizontal = design.contentPadding)) {
                Spacer(Modifier.height(design.contentPadding))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = cvData.fullName.ifBlank { "Your Name" }.uppercase(),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            ),
                            color = design.sectionTitleColor
                        )

                        if (cvData.jobTitle.isNotBlank()) {
                            Spacer(Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = design.accentColor
                                ) {
                                    Box(Modifier.size(width = 4.dp, height = 18.dp))
                                }
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    text = cvData.jobTitle,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = design.subtitleColor
                                )
                            }
                        }

                        Spacer(Modifier.height(18.dp))

                        ProfessionalContactBlock(cvData = cvData, accentColor = design.accentColor)
                    }

                    if (!cvData.profileImageUri.isNullOrBlank()) {
                        Surface(
                            modifier = Modifier.size(design.profileImageSize),
                            shape = CircleShape,
                            shadowElevation = 3.dp,
                            color = Color.Transparent,
                            border = androidx.compose.foundation.BorderStroke(
                                width = 3.dp,
                                color = design.accentColorSoft
                            )
                        ) {
                            GlideImage(
                                model = cvData.profileImageUri,
                                contentDescription = "Profile photo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(2.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Spacer(Modifier.height(design.contentPadding - 2.dp))
                HorizontalDivider(color = design.dividerColor, thickness = 0.8.dp)
                Spacer(Modifier.height(design.sectionSpacing))
            }
        }

        if (cvData.summary.isNotBlank()) {
            item {
                ProfessionalSection(
                    title = "Professional Summary",
                    accentColor = design.accentColor,
                    softColor = design.accentColorSoft
                ) {
                    Text(
                        text = cvData.summary,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 22.sp
                        ),
                        color = design.bodyColor
                    )
                }
            }
        }

        if (cvData.experiences.isNotEmpty()) {
            item {
                ProfessionalSection(
                    title = "Professional Experience",
                    accentColor = design.accentColor,
                    softColor = design.accentColorSoft
                ) {
                    cvData.experiences.forEachIndexed { idx, entry ->
                        ProfessionalExperienceItem(
                            entry = entry,
                            accentColor = design.accentColor,
                            bodyColor = design.bodyColor,
                            isLast = idx == cvData.experiences.size - 1,
                            softColor = design.accentColorSoft
                        )
                    }
                }
            }
        }

        if (cvData.education.isNotEmpty()) {
            item {
                ProfessionalSection(
                    title = "Education",
                    accentColor = design.accentColor,
                    softColor = design.accentColorSoft
                ) {
                    cvData.education.forEachIndexed { idx, entry ->
                        ProfessionalEducationItem(
                            entry = entry,
                            accentColor = design.accentColor,
                            bodyColor = design.bodyColor,
                            isLast = idx == cvData.education.size - 1
                        )
                    }
                }
            }
        }

        if (cvData.skills.isNotEmpty()) {
            item {
                ProfessionalSection(
                    title = "Core Skills",
                    accentColor = design.accentColor,
                    softColor = design.accentColorSoft
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        cvData.skills.chunked(2).forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                row.forEach { skill ->
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(5.dp, 14.dp)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(design.accentColor)
                                        )
                                        Text(
                                            text = skill,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Medium
                                            ),
                                            color = TemplateDesigns.Classic.headerTextColor
                                        )
                                    }
                                }
                                val rem = 2 - row.size
                                repeat(rem) { Spacer(Modifier.weight(1f)) }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(design.contentPadding - design.sectionSpacing))
        }
    }
}

@Composable
private fun ProfessionalContactBlock(cvData: CvData, accentColor: Color) {
    val lines = listOfNotNull(
        cvData.email.takeIf { it.isNotBlank() },
        cvData.phone.takeIf { it.isNotBlank() },
        cvData.location.takeIf { it.isNotBlank() },
        cvData.linkedIn.takeIf { it.isNotBlank() },
        cvData.website.takeIf { it.isNotBlank() }
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = accentColor.copy(alpha = 0.05f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            lines.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    row.forEach { line ->
                        ProfessionalContactItem(
                            text = line,
                            accentColor = accentColor,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    val rem = 2 - row.size
                    repeat(rem) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
    }
}

@Composable
private fun ProfessionalContactItem(
    text: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val contactIcon = when {
        text.contains("@") -> CvIconSystem.Email
        text.contains("+") || text.matches(Regex("^[0-9\\s\\-\\+\\(\\)]+$")) -> CvIconSystem.Phone
        text.contains("linkedin", ignoreCase = true) -> CvIconSystem.LinkedIn
        text.contains("http", ignoreCase = true) -> CvIconSystem.Website
        else -> CvIconSystem.Location
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = contactIcon.vectorIcon,
            contentDescription = contactIcon.label,
            modifier = Modifier.size(14.dp),
            tint = accentColor
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = TemplateDesigns.Classic.headerTextColor,
            maxLines = 1
        )
    }
}

@Composable
private fun ProfessionalSection(
    title: String,
    accentColor: Color,
    softColor: Color,
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
                    .width(5.dp)
                    .height(22.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(accentColor, accentColor.copy(alpha = 0.6f))
                        )
                    )
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.4.sp
                ),
                color = accentColor
            )
        }

        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier.padding(start = 17.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(44.dp)
                    .height(2.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(softColor)
            )
        }
        Spacer(Modifier.height(14.dp))
        content()
    }
}

@Composable
private fun ProfessionalExperienceItem(
    entry: ExperienceEntry,
    accentColor: Color,
    bodyColor: Color,
    isLast: Boolean,
    softColor: Color
) {
    Column(
        modifier = Modifier.padding(bottom = if (isLast) 0.dp else 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.role,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TemplateDesigns.Classic.headerTextColor
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = entry.company,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = accentColor
                )
            }
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = softColor
            ) {
                Text(
                    text = entry.dates,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = accentColor,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
        if (entry.description.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.padding(start = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .size(4.dp)
                        .clip(RoundedCornerShape(50))
                        .background(accentColor)
                )
                Text(
                    text = entry.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 21.sp
                    ),
                    color = bodyColor,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        if (!isLast) {
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(
                color = accentColor.copy(alpha = 0.08f),
                thickness = 1.dp
            )
        }
    }
}

@Composable
private fun ProfessionalEducationItem(
    entry: EducationEntry,
    accentColor: Color,
    bodyColor: Color,
    isLast: Boolean
) {
    Column(
        modifier = Modifier.padding(bottom = if (isLast) 0.dp else 12.dp)
    ) {
        Text(
            text = entry.degree,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = TemplateDesigns.Classic.headerTextColor
        )
        Spacer(Modifier.height(3.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entry.school,
                style = MaterialTheme.typography.bodyMedium,
                color = accentColor
            )
            Text(
                text = entry.dates,
                style = MaterialTheme.typography.labelMedium,
                color = bodyColor
            )
        }
    }
}
