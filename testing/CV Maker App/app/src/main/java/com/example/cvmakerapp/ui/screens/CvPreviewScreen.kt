package com.example.cvmaker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cvmakerapp.ui.theme.CvMakerTheme

data class WorkExperienceItem(
    val role: String,
    val company: String,
    val dates: String,
    val location: String,
    val bullets: List<String>
)

private val sampleExperience = listOf(
    WorkExperienceItem(
        role = "Chief Operations Officer",
        company = "Acme Global Tech Solutions",
        dates = "2019 - Present",
        location = "San Francisco, CA",
        bullets = listOf(
            "Orchestrated a 30% reduction in operational costs across 5 international hubs through lean management initiatives.",
            "Directed a cross-functional team of 150+ employees to launch a new SaaS product line, capturing \$50M in year-one revenue.",
            "Implemented enterprise-wide ERP system, reducing reporting latency by 40%."
        )
    ),
    WorkExperienceItem(
        role = "VP of Global Operations",
        company = "Acme Global Tech Solutions",
        dates = "2014 - 2019",
        location = "New York, NY",
        bullets = listOf(
            "Scaled regional operations team from 12 to 60 to support international expansion.",
            "Negotiated vendor contracts saving \$4.2M annually."
        )
    )
)

@Composable
fun CvPreviewScreen(
    name: String = "Eleanor Vance",
    jobTitle: String = "Chief Operations Officer",
    location: String = "New York, NY",
    email: String = "eleanor.vance@example.com",
    phone: String = "+1 (555) 123-4567",
    linkedIn: String = "linkedin.com/in/evance",
    summary: String = "Strategic and results-driven Chief Operations Officer with over 15 years of experience in scaling global operations within the technology sector. Proven track record in streamlining supply chains, implementing cost-saving efficiencies, and leading cross-functional teams to exceed aggressive growth targets. Adept at navigating complex regulatory environments and steering organizations through transformational change.",
    experience: List<WorkExperienceItem> = sampleExperience,
    onShare: () -> Unit = {},
    onExportPdf: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        topBar = { CvPreviewTopBar() },
        bottomBar = { CvPreviewBottomBar(onShare = onShare, onExportPdf = onExportPdf, onEdit = onEdit) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                // "Paper" — the resume document itself, lifted with ambient shadow
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 8.dp, shape = MaterialTheme.shapes.medium, clip = false)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                        .padding(24.dp)
                ) {
                    Text(
                        name.uppercase(),
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        jobTitle,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.height(12.dp))

                    ContactRow(Icons.Outlined.LocationOn, location)
                    ContactRow(Icons.Outlined.Email, email)
                    ContactRow(Icons.Outlined.Phone, phone)
                    ContactRow(Icons.Outlined.Link, linkedIn)

                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(Modifier.height(16.dp))

                    ResumeSectionTitle("EXECUTIVE PROFILE")
                    Text(
                        summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.height(20.dp))
                    ResumeSectionTitle("PROFESSIONAL EXPERIENCE")

                    experience.forEach { item ->
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "${item.dates}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            item.location,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            item.role,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            item.company,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(Modifier.height(8.dp))
                        item.bullets.forEach { bullet ->
                            Row(modifier = Modifier.padding(bottom = 6.dp)) {
                                Text("•  ", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    bullet,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        modifier = Modifier.padding(bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
        Spacer(Modifier.width(6.dp))
        Text(text, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ResumeSectionTitle(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(Modifier.height(8.dp))
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    Spacer(Modifier.height(12.dp))
}

@Composable
private fun CvPreviewTopBar() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(Icons.Outlined.Menu, contentDescription = "Menu", tint = MaterialTheme.colorScheme.onBackground)
            Text(
                "ResumeBuilder",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = "Profile",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}

@Composable
private fun CvPreviewBottomBar(onShare: () -> Unit, onExportPdf: () -> Unit, onEdit: () -> Unit) {
    Surface(color = MaterialTheme.colorScheme.surfaceContainerLowest) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onShare,
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.small,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Icon(Icons.Outlined.Share, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onBackground)
                Spacer(Modifier.width(6.dp))
                Text("Share", color = MaterialTheme.colorScheme.onBackground)
            }
            OutlinedButton(
                onClick = onExportPdf,
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.small,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Icon(Icons.Outlined.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onBackground)
                Spacer(Modifier.width(6.dp))
                Text("Export PDF", color = MaterialTheme.colorScheme.onBackground)
            }
            Button(
                onClick = onEdit,
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Edit Resume")
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1400)
@Composable
private fun CvPreviewScreenPreview() {
    CvMakerTheme {
        CvPreviewScreen()
    }
}
