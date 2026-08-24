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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Person
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


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ClassicCvTemplate(
    cvData: CvData,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Header Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Image
                if (!cvData.profileImageUri.isNullOrBlank()) {
                    GlideImage(
                        model = cvData.profileImageUri,
                        contentDescription = "Profile photo",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(Modifier.width(16.dp))
                }

                // Name and Job Title
                Column(modifier = Modifier.weight(1f)) {
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
                }
            }

            Spacer(Modifier.height(16.dp))

            // Contact Information
            if (cvData.email.isNotBlank()) {
                ContactRow(
                    icon = Icons.Default.Email,
                    iconDescription = "Email",
                    text = cvData.email
                )
            }

            if (cvData.phone.isNotBlank()) {
                ContactRow(
                    icon = Icons.Default.Phone,
                    iconDescription = "Phone",
                    text = cvData.phone
                )
            }

            if (cvData.location.isNotBlank()) {
                ContactRow(
                    icon = Icons.Default.LocationOn,
                    iconDescription = "Location",
                    text = cvData.location
                )
            }

            if (cvData.linkedIn.isNotBlank()) {
                ContactRow(
                    icon = Icons.Default.Person,
                    iconDescription = "LinkedIn",
                    text = cvData.linkedIn
                )
            }

            if (cvData.website.isNotBlank()) {
                ContactRow(
                    icon = Icons.Default.Language,
                    iconDescription = "Website",
                    text = cvData.website
                )
            }

            Spacer(Modifier.height(16.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(Modifier.height(16.dp))
        }

        // Professional Summary
        if (cvData.summary.isNotBlank()) {
            item {
                SectionTitle(text = "PROFESSIONAL SUMMARY")

                Text(
                    text = cvData.summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(16.dp))
            }
        }

        // Experience
        if (cvData.experiences.isNotEmpty()) {
            item {
                SectionTitle(text = "PROFESSIONAL EXPERIENCE")
            }

            items(cvData.experiences.size) { index ->
                ExperienceItem(entry = cvData.experiences[index])
            }

            item {
                Spacer(Modifier.height(16.dp))
            }
        }

        // Education
        if (cvData.education.isNotEmpty()) {
            item {
                SectionTitle(text = "EDUCATION")
            }

            items(cvData.education.size) { index ->
                EducationItem(entry = cvData.education[index])
            }

            item {
                Spacer(Modifier.height(16.dp))
            }
        }

        // Skills
        if (cvData.skills.isNotEmpty()) {
            item {
                SectionTitle(text = "SKILLS")

                Text(
                    text = cvData.skills.joinToString(" • "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}


@Composable
private fun ContactRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconDescription: String,
    text: String
) {
    if (text.isBlank()) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconDescription,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Composable
private fun SectionTitle(text: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(7.dp))

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Spacer(Modifier.height(12.dp))
    }
}


@Composable
private fun ExperienceItem(entry: ExperienceEntry) {
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
private fun EducationItem(entry: EducationEntry) {
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
