package com.example.cvmakerapp.ui.screens

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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.EducationEntry
import com.example.cvmakerapp.data.ExperienceEntry
import com.example.cvmakerapp.ui.theme.CVMakerAppTheme


// ============================================================
// PREVIEW SCREEN
// ============================================================

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CvPreviewScreen(
    cvData: CvData,
    onBack: () -> Unit = {}
) {

    Scaffold(

        containerColor =
            MaterialTheme.colorScheme.surfaceContainerLow,

        topBar = {

            CvPreviewTopBar(
                onBack = onBack
            )
        }

    ) { innerPadding ->

        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),

            contentPadding = PaddingValues(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)

        ) {

            item {

                // =================================================
                // CV PAPER
                // =================================================

                Column(

                    modifier = Modifier
                        .fillMaxWidth()

                        .shadow(
                            elevation = 8.dp,
                            shape = MaterialTheme.shapes.medium
                        )

                        .clip(
                            MaterialTheme.shapes.medium
                        )

                        .background(
                            MaterialTheme.colorScheme.surfaceContainerLowest
                        )

                        .padding(24.dp)

                ) {

                    // =================================================
                    // HEADER
                    // =================================================

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        // Profile image
                        if (cvData.profileImageUri != null) {

                            GlideImage(

                                model = cvData.profileImageUri,

                                contentDescription =
                                    "Profile photo",

                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape),

                                contentScale =
                                    ContentScale.Crop
                            )

                            Spacer(
                                Modifier.width(16.dp)
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(

                                text = cvData.name.uppercase(),

                                style =
                                    MaterialTheme.typography.headlineLarge
                                        .copy(
                                            fontWeight =
                                                FontWeight.Bold
                                        ),

                                color =
                                    MaterialTheme.colorScheme.onBackground
                            )

                            Spacer(
                                Modifier.height(4.dp)
                            )

                            Text(

                                text = cvData.jobTitle,

                                style =
                                    MaterialTheme.typography.titleLarge,

                                color =
                                    MaterialTheme.colorScheme.primary
                            )
                        }
                    }


                    Spacer(
                        Modifier.height(16.dp)
                    )


                    // =================================================
                    // CONTACT INFORMATION
                    // =================================================

                    ContactRow(
                        icon = Icons.Outlined.LocationOn,
                        text = cvData.location
                    )

                    ContactRow(
                        icon = Icons.Outlined.Email,
                        text = cvData.email
                    )

                    ContactRow(
                        icon = Icons.Outlined.Phone,
                        text = cvData.phone
                    )

                    if (cvData.linkedIn.isNotBlank()) {

                        ContactRow(
                            icon = Icons.Outlined.Link,
                            text = cvData.linkedIn
                        )
                    }

                    if (cvData.website.isNotBlank()) {

                        ContactRow(
                            icon = Icons.Outlined.Link,
                            text = cvData.website
                        )
                    }


                    Spacer(
                        Modifier.height(16.dp)
                    )


                    HorizontalDivider(
                        color =
                            MaterialTheme.colorScheme.outlineVariant
                    )


                    Spacer(
                        Modifier.height(20.dp)
                    )


                    // =================================================
                    // SUMMARY
                    // =================================================

                    if (cvData.summary.isNotBlank()) {

                        ResumeSectionTitle(
                            text = "PROFESSIONAL SUMMARY"
                        )

                        Text(

                            text = cvData.summary,

                            style =
                                MaterialTheme.typography.bodyMedium,

                            color =
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }


                    // =================================================
                    // EXPERIENCE
                    // =================================================

                    if (cvData.experiences.isNotEmpty()) {

                        Spacer(
                            Modifier.height(24.dp)
                        )

                        ResumeSectionTitle(
                            text = "PROFESSIONAL EXPERIENCE"
                        )

                        cvData.experiences.forEach { item ->

                            ExperiencePreviewItem(
                                entry = item
                            )
                        }
                    }


                    // =================================================
                    // EDUCATION
                    // =================================================

                    if (cvData.education.isNotEmpty()) {

                        Spacer(
                            Modifier.height(24.dp)
                        )

                        ResumeSectionTitle(
                            text = "EDUCATION"
                        )

                        cvData.education.forEach { item ->

                            EducationPreviewItem(
                                entry = item
                            )
                        }
                    }


                    // =================================================
                    // SKILLS
                    // =================================================

                    if (cvData.skills.isNotEmpty()) {

                        Spacer(
                            Modifier.height(24.dp)
                        )

                        ResumeSectionTitle(
                            text = "SKILLS"
                        )

                        SkillsPreview(
                            skills = cvData.skills
                        )
                    }
                }
            }
        }
    }
}


// ============================================================
// EXPERIENCE PREVIEW
// ============================================================

@Composable
private fun ExperiencePreviewItem(
    entry: ExperienceEntry
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {

        Text(
            text = entry.role,
            style =
                MaterialTheme.typography.titleMedium
                    .copy(
                        fontWeight =
                            FontWeight.Bold
                    ),
            color =
                MaterialTheme.colorScheme.onBackground
        )

        Spacer(
            Modifier.height(2.dp)
        )

        Text(
            text = entry.company,
            style =
                MaterialTheme.typography.bodyMedium
                    .copy(
                        fontWeight =
                            FontWeight.SemiBold
                    ),
            color =
                MaterialTheme.colorScheme.primary
        )

        Spacer(
            Modifier.height(2.dp)
        )

        Text(
            text = entry.dates,
            style =
                MaterialTheme.typography.labelMedium,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (entry.description.isNotBlank()) {

            Spacer(
                Modifier.height(8.dp)
            )

            Row(
                modifier =
                    Modifier.padding(bottom = 4.dp)
            ) {

                Text(
                    text = "•",
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    Modifier.width(8.dp)
                )

                Text(
                    text = entry.description,
                    style =
                        MaterialTheme.typography.bodyMedium,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


// ============================================================
// EDUCATION PREVIEW
// ============================================================

@Composable
private fun EducationPreviewItem(
    entry: EducationEntry
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {

        Text(
            text = entry.degree,
            style =
                MaterialTheme.typography.titleMedium
                    .copy(
                        fontWeight =
                            FontWeight.Bold
                    ),
            color =
                MaterialTheme.colorScheme.onBackground
        )

        Spacer(
            Modifier.height(2.dp)
        )

        Text(
            text = entry.school,
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                MaterialTheme.colorScheme.primary
        )

        Spacer(
            Modifier.height(2.dp)
        )

        Text(
            text = entry.dates,
            style =
                MaterialTheme.typography.labelMedium,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


// ============================================================
// SKILLS
// ============================================================

@Composable
private fun SkillsPreview(
    skills: List<String>
) {

    Column(
        verticalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {

        skills.chunked(2).forEach { rowSkills ->

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                rowSkills.forEach { skill ->

                    Surface(

                        modifier =
                            Modifier.weight(1f),

                        shape =
                            MaterialTheme.shapes.small,

                        color =
                            MaterialTheme.colorScheme.surfaceContainerHigh
                    ) {

                        Text(

                            text = skill,

                            modifier =
                                Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 8.dp
                                ),

                            style =
                                MaterialTheme.typography.bodySmall,

                            color =
                                MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}


// ============================================================
// CONTACT ROW
// ============================================================

@Composable
private fun ContactRow(

    icon: androidx.compose.ui.graphics.vector.ImageVector,

    text: String

) {

    if (text.isBlank()) return

    Row(

        modifier =
            Modifier.padding(bottom = 6.dp),

        verticalAlignment =
            Alignment.CenterVertically

    ) {

        Icon(

            imageVector = icon,

            contentDescription = null,

            tint =
                MaterialTheme.colorScheme.onSurfaceVariant,

            modifier =
                Modifier.size(15.dp)
        )

        Spacer(
            Modifier.width(7.dp)
        )

        Text(

            text = text,

            style =
                MaterialTheme.typography.labelMedium,

            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


// ============================================================
// SECTION TITLE
// ============================================================

@Composable
private fun ResumeSectionTitle(
    text: String
) {

    Text(

        text = text,

        style =
            MaterialTheme.typography.titleMedium
                .copy(
                    fontWeight =
                        FontWeight.Bold
                ),

        color =
            MaterialTheme.colorScheme.onBackground
    )

    Spacer(
        Modifier.height(7.dp)
    )

    HorizontalDivider(
        color =
            MaterialTheme.colorScheme.outlineVariant
    )

    Spacer(
        Modifier.height(12.dp)
    )
}


// ============================================================
// TOP BAR
// ============================================================

@Composable
private fun CvPreviewTopBar(
    onBack: () -> Unit
) {

    Column {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme
                        .surfaceContainerLowest
                )
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBack
            ) {

                Icon(
                    imageVector =
                        Icons.AutoMirrored.Outlined.ArrowBack,

                    contentDescription =
                        "Back",

                    tint =
                        MaterialTheme.colorScheme
                            .onBackground
                )
            }

            Text(

                text = "CV Preview",

                modifier =
                    Modifier.weight(1f),

                style =
                    MaterialTheme.typography
                        .titleLarge
                        .copy(
                            fontWeight =
                                FontWeight.Bold
                        ),

                color =
                    MaterialTheme.colorScheme
                        .onBackground
            )
        }

        HorizontalDivider(
            color =
                MaterialTheme.colorScheme
                    .outlineVariant
        )
    }
}


// ============================================================
// PREVIEW
// ============================================================

@Preview(
    showBackground = true,
    heightDp = 1400
)
@Composable
private fun CvPreviewScreenPreview() {

    CVMakerAppTheme {

        CvPreviewScreen(
            cvData = CvData(
                name = "Eleanor Vance",
                jobTitle = "Chief Operations Officer",
                location = "New York, NY",
                email = "eleanor.vance@example.com",
                phone = "+1 (555) 123-4567",
                linkedIn = "linkedin.com/in/evance",
                summary = "Strategic and results-driven Chief Operations Officer with over 15 years of experience in scaling global operations within the technology sector.",
                experiences = listOf(
                    ExperienceEntry(
                        company = "Acme Global Tech Solutions",
                        role = "Chief Operations Officer",
                        dates = "2019 - Present",
                        description = "Orchestrated a 30% reduction in operational costs."
                    )
                ),
                education = listOf(
                    EducationEntry(
                        school = "Stanford University",
                        degree = "MBA",
                        dates = "2012"
                    )
                ),
                skills = listOf(
                    "Operations Management",
                    "Leadership",
                    "Strategic Planning"
                )
            )
        )
    }
}
