package com.example.cvmakerapp.ui.screens.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.EducationEntry
import com.example.cvmakerapp.data.ExperienceEntry


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TwoColumnCvTemplate(
    cvData: CvData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxSize()
    ) {
        // Left Column - Sidebar
        Surface(
            modifier = Modifier
                .weight(0.35f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Profile Photo
                item {
                    if (!cvData.profileImageUri.isNullOrBlank()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            GlideImage(
                                model = cvData.profileImageUri,
                                contentDescription = "Profile photo",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }

                // Contact Information
                item {
                    TwoColumnSectionTitle(text = "CONTACT")

                    if (cvData.email.isNotBlank()) {
                        TwoColumnContactItem(
                            icon = Icons.Default.Email,
                            iconDescription = "Email",
                            text = cvData.email
                        )
                    }

                    if (cvData.phone.isNotBlank()) {
                        TwoColumnContactItem(
                            icon = Icons.Default.Phone,
                            iconDescription = "Phone",
                            text = cvData.phone
                        )
                    }

                    if (cvData.location.isNotBlank()) {
                        TwoColumnContactItem(
                            icon = Icons.Default.LocationOn,
                            iconDescription = "Location",
                            text = cvData.location
                        )
                    }

                    if (cvData.linkedIn.isNotBlank()) {
                        TwoColumnContactItem(
                            icon = Icons.Default.Person,
                            iconDescription = "LinkedIn",
                            text = cvData.linkedIn
                        )
                    }

                    if (cvData.website.isNotBlank()) {
                        TwoColumnContactItem(
                            icon = Icons.Default.Language,
                            iconDescription = "Website",
                            text = cvData.website
                        )
                    }
                }

                // Skills
                if (cvData.skills.isNotEmpty()) {
                    item {
                        TwoColumnSectionTitle(text = "SKILLS")

                        cvData.skills.forEach { skill ->
                            Text(
                                text = "• $skill",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // Right Column - Main Content
        LazyColumn(
            modifier = Modifier
                .weight(0.65f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Section
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = cvData.fullName.uppercase(),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = cvData.jobTitle,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.height(12.dp))

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        thickness = 2.dp
                    )

                    Spacer(Modifier.height(12.dp))
                }
            }

            // Professional Summary
            if (cvData.summary.isNotBlank()) {
                item {
                    TwoColumnMainSectionTitle(text = "PROFESSIONAL SUMMARY")

                    Text(
                        text = cvData.summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.height(8.dp))
                }
            }

            // Experience
            if (cvData.experiences.isNotEmpty()) {
                item {
                    TwoColumnMainSectionTitle(text = "PROFESSIONAL EXPERIENCE")
                }

                items(cvData.experiences.size) { index ->
                    TwoColumnExperienceItem(entry = cvData.experiences[index])
                }
            }

            // Education
            if (cvData.education.isNotEmpty()) {
                item {
                    TwoColumnMainSectionTitle(text = "EDUCATION")
                }

                items(cvData.education.size) { index ->
                    TwoColumnEducationItem(entry = cvData.education[index])
                }
            }

            item {
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}


@Composable
private fun TwoColumnSectionTitle(text: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(8.dp))

        HorizontalDivider(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
            thickness = 1.dp
        )

        Spacer(Modifier.height(10.dp))
    }
}


@Composable
private fun TwoColumnMainSectionTitle(text: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(8.dp))

        HorizontalDivider(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            thickness = 1.5.dp
        )

        Spacer(Modifier.height(12.dp))
    }
}


@Composable
private fun TwoColumnContactItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconDescription: String,
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconDescription,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Composable
private fun TwoColumnExperienceItem(entry: ExperienceEntry) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = entry.role,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = entry.company,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = entry.dates,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (entry.description.isNotBlank()) {
            Spacer(Modifier.height(8.dp))

            Row(modifier = Modifier.padding(bottom = 4.dp)) {
                Text(
                    text = "•",
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = entry.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Composable
private fun TwoColumnEducationItem(entry: EducationEntry) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Text(
            text = entry.degree,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = entry.school,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = entry.dates,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
