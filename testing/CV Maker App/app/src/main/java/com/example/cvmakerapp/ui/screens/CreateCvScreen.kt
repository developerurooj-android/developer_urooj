package com.example.cvmakerapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cvmakerapp.ui.theme.CVMakerAppTheme
import com.example.cvmakerapp.ui.theme.CvMakerAppTheme

data class ExperienceEntry(
    val company: String = "",
    val role: String = "",
    val dates: String = "",
    val description: String = ""
)

data class EducationEntry(
    val school: String = "",
    val degree: String = "",
    val dates: String = ""
)

@Composable
fun CreateCvScreen(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {},
    onPreview: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var linkedIn by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }

    var experiences by remember {
        mutableStateOf(listOf(ExperienceEntry("Acme Corp", "Senior Designer", "Jan 2020 - Present", "Led design system overhaul...")))
    }
    var education by remember { mutableStateOf(listOf(EducationEntry())) }
    var skills by remember { mutableStateOf(listOf("UI Design", "Figma")) }
    var newSkill by remember { mutableStateOf("") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { CreateCvTopBar(onBack = onBack, onSave = onSave) },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.background) {
                Button(
                    onClick = onPreview,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(52.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        "Preview CV",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                FormSectionCard {
                    SectionHeading("Personal Information")
                    PhotoUploadBox()
                    Spacer(Modifier.height(16.dp))
                    LabeledOutlinedField(fullName, { fullName = it }, "Full Name")
                    LabeledOutlinedField(title, { title = it }, "Professional Title")
                    LabeledOutlinedField(email, { email = it }, "Email")
                    LabeledOutlinedField(phone, { phone = it }, "Phone")
                    LabeledOutlinedField(location, { location = it }, "Location (City, Country)")
                    LabeledOutlinedField(linkedIn, { linkedIn = it }, "LinkedIn URL")
                    LabeledOutlinedField(website, { website = it }, "Website URL", isLast = true)
                }
            }

            item {
                FormSectionCard {
                    SectionHeading("Professional Summary")
                    OutlinedTextField(
                        value = summary,
                        onValueChange = { summary = it },
                        placeholder = { Text("Brief overview of your career and goals...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(96.dp),
                        shape = MaterialTheme.shapes.small,
                        colors = cvFieldColors()
                    )
                }
            }

            item {
                FormSectionCard {
                    SectionHeadingWithAction("Experience", "+ Add") {
                        experiences = experiences + ExperienceEntry()
                    }
                    experiences.forEachIndexed { index, exp ->
                        ExperienceCard(
                            entry = exp,
                            onChange = { updated ->
                                experiences = experiences.toMutableList().also { it[index] = updated }
                            }
                        )
                        if (index != experiences.lastIndex) Spacer(Modifier.height(12.dp))
                    }
                }
            }

            item {
                FormSectionCard {
                    SectionHeadingWithAction("Education", "+ Add") {
                        education = education + EducationEntry()
                    }
                    education.forEachIndexed { index, edu ->
                        EducationFields(
                            entry = edu,
                            onChange = { updated ->
                                education = education.toMutableList().also { it[index] = updated }
                            }
                        )
                        if (index != education.lastIndex) Spacer(Modifier.height(12.dp))
                    }
                }
            }

            item {
                FormSectionCard(isLast = true) {
                    SectionHeading("Skills")
                    FlowChips(
                        skills = skills,
                        onRemove = { skill -> skills = skills.filterNot { it == skill } }
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = newSkill,
                            onValueChange = { newSkill = it },
                            placeholder = { Text("Add a skill (press Enter)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = MaterialTheme.shapes.small,
                            colors = cvFieldColors()
                        )
                        Spacer(Modifier.width(8.dp))
                        OutlinedButton(
                            onClick = {
                                if (newSkill.isNotBlank()) {
                                    skills = skills + newSkill.trim()
                                    newSkill = ""
                                }
                            },
                            shape = MaterialTheme.shapes.small,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Text("Add", color = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateCvTopBar(onBack: () -> Unit, onSave: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
            }
            Text(
                "Create CV",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickableNoRipple(onSave)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Save", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.SemiBold)
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}

@Composable
private fun FormSectionCard(isLast: Boolean = false, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.large)
            .padding(16.dp),
        content = content
    )
}

@Composable
private fun SectionHeading(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(bottom = 12.dp)
    )
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.padding(bottom = 16.dp))
}

@Composable
private fun SectionHeadingWithAction(text: String, actionLabel: String, onAction: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = actionLabel,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.clickableNoRipple(onAction)
        )
    }
    Spacer(Modifier.height(12.dp))
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.padding(bottom = 16.dp))
}

@Composable
private fun PhotoUploadBox() {
    Box(
        modifier = Modifier
            .size(88.dp)
            .clip(MaterialTheme.shapes.medium)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Outlined.CameraAlt,
                contentDescription = "Upload photo",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Upload Photo",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun LabeledOutlinedField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isLast: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        singleLine = true,
        shape = MaterialTheme.shapes.small,
        colors = cvFieldColors()
    )
}

@Composable
private fun ExperienceCard(entry: ExperienceEntry, onChange: (ExperienceEntry) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.medium)
            .padding(12.dp)
    ) {
        MiniLabeledField("Company", entry.company) { onChange(entry.copy(company = it)) }
        Spacer(Modifier.height(10.dp))
        MiniLabeledField("Role", entry.role) { onChange(entry.copy(role = it)) }
        Spacer(Modifier.height(10.dp))
        MiniLabeledField("Dates", entry.dates) { onChange(entry.copy(dates = it)) }
        Spacer(Modifier.height(10.dp))
        MiniLabeledField("Description", entry.description, minLines = 3) { onChange(entry.copy(description = it)) }
    }
}

@Composable
private fun EducationFields(entry: EducationEntry, onChange: (EducationEntry) -> Unit) {
    Column {
        LabeledOutlinedField(entry.school, { onChange(entry.copy(school = it)) }, "School / University")
        LabeledOutlinedField(entry.degree, { onChange(entry.copy(degree = it)) }, "Degree")
        LabeledOutlinedField(entry.dates, { onChange(entry.copy(dates = it)) }, "Dates")
    }
}

@Composable
private fun MiniLabeledField(
    label: String,
    value: String,
    minLines: Int = 1,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            minLines = minLines,
            shape = MaterialTheme.shapes.small,
            colors = cvFieldColors()
        )
    }
}

@Composable
private fun FlowChips(skills: List<String>, onRemove: (String) -> Unit) {
    // Simple wrap using Row groups of up to 3 for a lightweight flow effect.
    val rows = skills.chunked(3)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { skill ->
                    Row(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(skill, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
                        Spacer(Modifier.width(6.dp))
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Remove $skill",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .size(14.dp)
                                .clickableNoRipple { onRemove(skill) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun cvFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    cursorColor = MaterialTheme.colorScheme.primary
)

@Preview(showBackground = true, heightDp = 1400)
@Composable
private fun CreateCvScreenPreview() {
    CVMakerAppTheme {
        createCvScreen()
    }
}
