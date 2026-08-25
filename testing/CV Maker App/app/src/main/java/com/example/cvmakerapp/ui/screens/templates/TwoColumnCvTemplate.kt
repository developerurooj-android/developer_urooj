package com.example.cvmakerapp.ui.screens.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TwoColumnCvTemplate(
    cvData: CvData,
    modifier: Modifier = Modifier
) {
    val design = TemplateDesigns.TwoColumn

    Row(
        modifier = modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .weight(0.32f)
                .fillMaxHeight(),
            color = design.sidebarBackground ?: Color(0xFFF5F5F5)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(design.contentPadding - 4.dp),
                verticalArrangement = Arrangement.spacedBy(design.sectionSpacing),
                contentPadding = PaddingValues(bottom = design.contentPadding)
            ) {
                if (!cvData.profileImageUri.isNullOrBlank()) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                modifier = Modifier.size(design.profileImageSize),
                                shape = CircleShape,
                                shadowElevation = 2.dp,
                                color = Color.White
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
                        }
                    }
                }

                item {
                    TwoColumnSidebarSectionTitle(
                        text = "CONTACT",
                        accentColor = design.accentColor,
                        dividerColor = design.dividerColor
                    )

                    Column(modifier = Modifier.fillMaxWidth()) {
                        val contacts = listOfNotNull(
                            cvData.email.takeIf { it.isNotBlank() }?.let { Icons.Default.Email to it },
                            cvData.phone.takeIf { it.isNotBlank() }?.let { Icons.Default.Phone to it },
                            cvData.location.takeIf { it.isNotBlank() }?.let { Icons.Default.LocationOn to it },
                            cvData.linkedIn.takeIf { it.isNotBlank() }?.let { Icons.Default.Person to it },
                            cvData.website.takeIf { it.isNotBlank() }?.let { Icons.Default.Language to it }
                        )
                        contacts.forEach { (icon, text) ->
                            TwoColumnContactItem(
                                icon = icon,
                                text = text,
                                accentColor = design.accentColor,
                                bodyColor = design.bodyColor
                            )
                        }
                    }
                }

                if (cvData.skills.isNotEmpty()) {
                    item {
                        TwoColumnSidebarSectionTitle(
                            text = "SKILLS",
                            accentColor = design.accentColor,
                            dividerColor = design.dividerColor
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            cvData.skills.forEach { skill ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(5.dp)
                                            .clip(CircleShape)
                                            .background(design.accentColor)
                                    )
                                    Text(
                                        text = skill,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Medium
                                        ),
                                        color = design.sidebarTextColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(0.68f)
                .fillMaxHeight()
                .background(design.pageBackground),
            contentPadding = PaddingValues(design.contentPadding),
            verticalArrangement = Arrangement.spacedBy(design.sectionSpacing)
        ) {
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
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
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = design.accentColor.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            if (cvData.summary.isNotBlank()) {
                item {
                    TwoColumnMainSectionTitle(
                        text = "PROFESSIONAL SUMMARY",
                        accentColor = design.accentColor,
                        dividerColor = design.dividerColor
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
                    TwoColumnMainSectionTitle(
                        text = "PROFESSIONAL EXPERIENCE",
                        accentColor = design.accentColor,
                        dividerColor = design.dividerColor
                    )
                }

                items(cvData.experiences.size) { index ->
                    TwoColumnExperienceItem(
                        entry = cvData.experiences[index],
                        accentColor = design.accentColor,
                        bodyColor = design.bodyColor,
                        isLast = index == cvData.experiences.size - 1
                    )
                }
            }

            if (cvData.education.isNotEmpty()) {
                item {
                    TwoColumnMainSectionTitle(
                        text = "EDUCATION",
                        accentColor = design.accentColor,
                        dividerColor = design.dividerColor
                    )
                }

                items(cvData.education.size) { index ->
                    TwoColumnEducationItem(
                        entry = cvData.education[index],
                        accentColor = design.accentColor,
                        bodyColor = design.bodyColor,
                        isLast = index == cvData.education.size - 1
                    )
                }
            }

            item {
                Spacer(Modifier.height(design.contentPadding))
            }
        }
    }
}

@Composable
private fun TwoColumnSidebarSectionTitle(
    text: String,
    accentColor: Color,
    dividerColor: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            ),
            color = accentColor
        )

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .width(40.dp)
                .height(2.dp)
                .background(accentColor)
                .clip(RoundedCornerShape(2.dp))
        )

        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun TwoColumnMainSectionTitle(
    text: String,
    accentColor: Color,
    dividerColor: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(accentColor, accentColor.copy(alpha = 0.5f))
                        )
                    )
            )
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.2.sp
                ),
                color = accentColor
            )
        }

        Spacer(Modifier.height(10.dp))

        HorizontalDivider(
            color = dividerColor,
            thickness = 1.dp
        )

        Spacer(Modifier.height(14.dp))
    }
}

@Composable
private fun TwoColumnContactItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    accentColor: Color,
    bodyColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(accentColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(13.dp),
                tint = accentColor
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = bodyColor,
            maxLines = 2
        )
    }
}

@Composable
private fun TwoColumnExperienceItem(
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
        Text(
            text = entry.role,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = bodyColor.copy(alpha = 0.9f)
        )

        Spacer(Modifier.height(3.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entry.company,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = accentColor
            )
            Text(
                text = entry.dates,
                style = MaterialTheme.typography.labelMedium,
                color = bodyColor
            )
        }

        if (entry.description.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(start = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 9.dp)
                        .size(4.dp)
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
private fun TwoColumnEducationItem(
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
        Text(
            text = entry.degree,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = bodyColor.copy(alpha = 0.9f)
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
