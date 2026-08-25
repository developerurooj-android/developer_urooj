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
import androidx.compose.ui.draw.shadow
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
import com.example.cvmakerapp.ui.theme.CardShadowSoft

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
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    design.headerBackground ?: design.accentColor,
                                    design.accentColor.copy(alpha = 0.7f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp)
                        .padding(horizontal = design.contentPadding),
                    verticalArrangement = Arrangement.spacedBy(design.sectionSpacing)
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 6.dp,
                                spotColor = CardShadowSoft,
                                ambientColor = CardShadowSoft,
                                shape = RoundedCornerShape(design.cardCornerRadius)
                            ),
                        shape = RoundedCornerShape(design.cardCornerRadius),
                        color = design.sectionCardBackground ?: Color.White,
                        tonalElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!cvData.profileImageUri.isNullOrBlank()) {
                                Surface(
                                    modifier = Modifier.size(design.profileImageSize),
                                    shape = CircleShape,
                                    shadowElevation = 3.dp,
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
                                Spacer(Modifier.width(18.dp))
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = cvData.fullName.ifBlank { "Your Name" },
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = TemplateDesigns.Classic.headerTextColor
                                )

                                if (cvData.jobTitle.isNotBlank()) {
                                    Spacer(Modifier.height(6.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(design.accentColor)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            text = cvData.jobTitle,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            color = design.accentColor
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 2.dp,
                                spotColor = CardShadowSoft,
                                ambientColor = CardShadowSoft,
                                shape = RoundedCornerShape(design.cardCornerRadius)
                            ),
                        shape = RoundedCornerShape(design.cardCornerRadius),
                        color = Color.White,
                        tonalElevation = 1.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val contacts = listOfNotNull(
                                cvData.email.takeIf { it.isNotBlank() },
                                cvData.phone.takeIf { it.isNotBlank() },
                                cvData.location.takeIf { it.isNotBlank() },
                                cvData.linkedIn.takeIf { it.isNotBlank() },
                                cvData.website.takeIf { it.isNotBlank() }
                            )
                            contacts.chunked(2).forEach { row ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    row.forEach { contact ->
                                        ModernContactLine(
                                            text = contact,
                                            accentColor = design.accentColor,
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
            }
        }

        if (cvData.summary.isNotBlank()) {
            item {
                ModernSectionCard(
                    title = "Summary",
                    accentColor = design.accentColor,
                    cornerRadius = design.cardCornerRadius,
                    cardBg = design.sectionCardBackground ?: Color.White,
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
                ModernSectionCard(
                    title = "Experience",
                    accentColor = design.accentColor,
                    cornerRadius = design.cardCornerRadius,
                    cardBg = design.sectionCardBackground ?: Color.White,
                    softColor = design.accentColorSoft
                ) {
                    cvData.experiences.forEachIndexed { idx, entry ->
                        ModernExperienceItem(
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
                ModernSectionCard(
                    title = "Education",
                    accentColor = design.accentColor,
                    cornerRadius = design.cardCornerRadius,
                    cardBg = design.sectionCardBackground ?: Color.White,
                    softColor = design.accentColorSoft
                ) {
                    cvData.education.forEachIndexed { idx, entry ->
                        ModernEducationItem(
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
                ModernSectionCard(
                    title = "Skills",
                    accentColor = design.accentColor,
                    cornerRadius = design.cardCornerRadius,
                    cardBg = design.sectionCardBackground ?: Color.White,
                    softColor = design.accentColorSoft
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        cvData.skills.chunked(2).forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                row.forEach { skill ->
                                    Surface(
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp),
                                        color = design.accentColorSoft
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(
                                                horizontal = 12.dp,
                                                vertical = 7.dp
                                            ),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .clip(CircleShape)
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
private fun ModernContactLine(
    text: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val iconAndDesc = when {
        text.contains("@") -> Icons.Outlined.Email to "Email"
        text.contains("+") || text.matches(Regex("^[0-9\\s\\-\\+\\(\\)]+$")) -> Icons.Rounded.Phone to "Phone"
        text.contains("linkedin", ignoreCase = true) ->
            Icons.Outlined.Person to "LinkedIn"
        text.contains("http", ignoreCase = true) -> Icons.Outlined.Language to "Website"
        else -> Icons.Rounded.LocationOn to "Location"
    }
    val icon = iconAndDesc.first

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(accentColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(15.dp),
                tint = accentColor
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = TemplateDesigns.Classic.headerTextColor,
            maxLines = 1
        )
    }
}

@Composable
private fun ModernSectionCard(
    title: String,
    accentColor: Color,
    cornerRadius: androidx.compose.ui.unit.Dp,
    cardBg: Color,
    softColor: Color,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = TemplateDesigns.Modern.contentPadding)
            .padding(bottom = TemplateDesigns.Modern.sectionSpacing)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(bottom = 10.dp, start = 2.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = softColor
            ) {
                Box(
                    modifier = Modifier
                        .width(5.dp)
                        .height(22.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(accentColor)
                )
            }
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.3.sp
                ),
                color = accentColor
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 3.dp,
                    spotColor = CardShadowSoft,
                    ambientColor = CardShadowSoft,
                    shape = RoundedCornerShape(cornerRadius)
                ),
            shape = RoundedCornerShape(cornerRadius),
            color = cardBg,
            tonalElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun ModernExperienceItem(
    entry: ExperienceEntry,
    accentColor: Color,
    bodyColor: Color,
    isLast: Boolean
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
                Text(
                    text = entry.company,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = accentColor
                )
            }
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = accentColor.copy(alpha = 0.08f)
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
            Text(
                text = entry.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 21.sp
                ),
                color = bodyColor
            )
        }
        if (!isLast) {
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(
                color = accentColor.copy(alpha = 0.10f),
                thickness = 1.dp
            )
        }
    }
}

@Composable
private fun ModernEducationItem(
    entry: EducationEntry,
    accentColor: Color,
    bodyColor: Color,
    isLast: Boolean
) {
    Column(
        modifier = Modifier.padding(bottom = if (isLast) 0.dp else 12.dp)
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
                Text(
                    text = entry.school,
                    style = MaterialTheme.typography.bodyMedium,
                    color = accentColor
                )
            }
            Text(
                text = entry.dates,
                style = MaterialTheme.typography.labelMedium,
                color = bodyColor
            )
        }
        if (!isLast) {
            Spacer(Modifier.height(10.dp))
            HorizontalDivider(
                color = accentColor.copy(alpha = 0.10f),
                thickness = 1.dp
            )
        }
    }
}
