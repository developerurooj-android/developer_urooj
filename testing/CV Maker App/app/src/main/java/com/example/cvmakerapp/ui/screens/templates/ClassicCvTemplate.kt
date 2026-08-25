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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.EducationEntry
import com.example.cvmakerapp.data.ExperienceEntry
import com.example.cvmakerapp.data.TemplateDesigns

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ClassicCvTemplate(
    cvData: CvData,
    modifier: Modifier = Modifier
) {
    val design = TemplateDesigns.Classic

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
                if (!cvData.profileImageUri.isNullOrBlank()) {
                    Surface(
                        modifier = Modifier.size(design.profileImageSize),
                        shape = CircleShape,
                        shadowElevation = 2.dp,
                        color = Color.Transparent
                    ) {
                        GlideImage(
                            model = cvData.profileImageUri,
                            contentDescription = "Profile photo",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(Modifier.width(20.dp))
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = cvData.fullName.uppercase(),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        color = design.headerTextColor
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = cvData.jobTitle,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = design.subtitleColor
                    )
                }
            }

            Spacer(Modifier.height(design.sectionSpacing))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(design.accentColorSoft)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val contactPairs = listOfNotNull(
                    cvData.email.takeIf { it.isNotBlank() }?.let { Icons.Default.Email to it },
                    cvData.phone.takeIf { it.isNotBlank() }?.let { Icons.Default.Phone to it },
                    cvData.location.takeIf { it.isNotBlank() }?.let { Icons.Default.LocationOn to it },
                    cvData.linkedIn.takeIf { it.isNotBlank() }?.let { Icons.Default.Person to it },
                    cvData.website.takeIf { it.isNotBlank() }?.let { Icons.Default.Language to it }
                )

                val half = (contactPairs.size + 1) / 2
                val row1 = contactPairs.take(half)
                val row2 = contactPairs.drop(half)

                if (row1.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        row1.forEach { (icon, text) ->
                            ClassicContactItem(
                                icon = icon,
                                text = text,
                                accentColor = design.accentColor,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (row1.size < row2.size + 0) {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
                if (row2.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        row2.forEach { (icon, text) ->
                            ClassicContactItem(
                                icon = icon,
                                text = text,
                                accentColor = design.accentColor,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        repeat(half - row2.size) {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(design.accentColor)
            )
            Spacer(Modifier.height(4.dp))
        }

        if (cvData.summary.isNotBlank()) {
            item {
                ClassicSectionTitle(
                    text = "PROFESSIONAL SUMMARY",
                    accentColor = design.accentColor,
                    dividerColor = design.dividerColor,
                    titleColor = design.sectionTitleColor
                )

                Text(
                    text = cvData.summary,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 22.sp
                    ),
                    color = design.bodyColor
                )
            }
        }

        if (cvData.experiences.isNotEmpty()) {
            item {
                ClassicSectionTitle(
                    text = "PROFESSIONAL EXPERIENCE",
                    accentColor = design.accentColor,
                    dividerColor = design.dividerColor,
                    titleColor = design.sectionTitleColor
                )
            }

            items(cvData.experiences.size) { index ->
                ClassicExperienceItem(
                    entry = cvData.experiences[index],
                    accentColor = design.accentColor,
                    bodyColor = design.bodyColor,
                    isLast = index == cvData.experiences.size - 1
                )
            }
        }

        if (cvData.education.isNotEmpty()) {
            item {
                ClassicSectionTitle(
                    text = "EDUCATION",
                    accentColor = design.accentColor,
                    dividerColor = design.dividerColor,
                    titleColor = design.sectionTitleColor
                )
            }

            items(cvData.education.size) { index ->
                ClassicEducationItem(
                    entry = cvData.education[index],
                    accentColor = design.accentColor,
                    bodyColor = design.bodyColor,
                    isLast = index == cvData.education.size - 1
                )
            }
        }

        if (cvData.skills.isNotEmpty()) {
            item {
                ClassicSectionTitle(
                    text = "SKILLS",
                    accentColor = design.accentColor,
                    dividerColor = design.dividerColor,
                    titleColor = design.sectionTitleColor
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    cvData.skills.chunked(3).forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            row.forEach { skill ->
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(6.dp),
                                    color = design.accentColorSoft
                                ) {
                                    Text(
                                        text = skill,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Medium
                                        ),
                                        color = design.sectionTitleColor,
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 6.dp
                                        )
                                    )
                                }
                            }
                            val remaining = 3 - row.size
                            repeat(remaining) {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ClassicContactItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
            imageVector = icon,
            contentDescription = null,
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
private fun ClassicSectionTitle(
    text: String,
    accentColor: Color,
    dividerColor: Color,
    titleColor: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(18.dp)
                    .background(accentColor)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.3.sp
                ),
                color = titleColor
            )
        }

        Spacer(Modifier.height(8.dp))

        HorizontalDivider(
            color = dividerColor,
            thickness = 1.dp
        )

        Spacer(Modifier.height(14.dp))
    }
}

@Composable
private fun ClassicExperienceItem(
    entry: ExperienceEntry,
    accentColor: Color,
    bodyColor: Color,
    isLast: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = if (isLast) 0.dp else 14.dp)
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

                Spacer(Modifier.height(2.dp))

                Text(
                    text = entry.company,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = accentColor
                )
            }

            Spacer(Modifier.width(12.dp))

            Text(
                text = entry.dates,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = bodyColor
            )
        }

        if (entry.description.isNotBlank()) {
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.padding(start = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 9.dp)
                        .size(5.dp)
                        .clip(CircleShape)
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
    }
}

@Composable
private fun ClassicEducationItem(
    entry: EducationEntry,
    accentColor: Color,
    bodyColor: Color,
    isLast: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = if (isLast) 0.dp else 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.degree,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TemplateDesigns.Classic.headerTextColor
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = entry.school,
                    style = MaterialTheme.typography.bodyMedium,
                    color = accentColor
                )
            }

            Spacer(Modifier.width(12.dp))

            Text(
                text = entry.dates,
                style = MaterialTheme.typography.labelMedium,
                color = bodyColor
            )
        }
    }
}
