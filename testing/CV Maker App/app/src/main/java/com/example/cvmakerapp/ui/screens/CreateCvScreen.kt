package com.example.cvmakerapp.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.CvRepository
import com.example.cvmakerapp.data.EducationEntry
import com.example.cvmakerapp.data.ExperienceEntry
import com.example.cvmakerapp.ui.theme.CVMakerAppTheme

// ============================================================
// MAIN CREATE CV SCREEN
// ============================================================

@Composable
fun CreateCvScreen(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {},
    onPreview: () -> Unit = {}
) {
    // ========================================================
    // STATE
    // ========================================================

    var fullName by remember { mutableStateOf("") }
    var jobTitle by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var linkedIn by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    var experiences by remember {
        mutableStateOf(listOf(ExperienceEntry()))
    }

    var education by remember {
        mutableStateOf(listOf(EducationEntry()))
    }

    var skills by remember {
        mutableStateOf(listOf<String>())
    }

    var newSkill by remember { mutableStateOf("") }

    // Helper to get current CV data
    val getCurrentCvData = {
        CvData(
            name = fullName,
            jobTitle = jobTitle,
            email = email,
            phone = phone,
            location = location,
            linkedIn = linkedIn,
            website = website,
            summary = summary,
            profileImageUri = profileImageUri,
            experiences = experiences.filter { it.company.isNotBlank() || it.role.isNotBlank() },
            education = education.filter { it.school.isNotBlank() || it.degree.isNotBlank() },
            skills = skills
        )
    }

    // ========================================================
    // SCAFFOLD
    // ========================================================

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CreateCvTopBar(
                onBack = onBack,
                onSave = {
                    CvRepository.saveCv(getCurrentCvData())
                    onSave()
                }
            )
        },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.background) {
                Button(
                    onClick = {
                        CvRepository.previewCv = getCurrentCvData()
                        onPreview()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .height(54.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "Preview CV",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // PERSONAL INFORMATION
            item {
                FormSectionCard {
                    SectionHeading("Personal Information")

                    PhotoUploadBox(
                        selectedImageUri = profileImageUri,
                        onImageSelected = { profileImageUri = it }
                    )

                    Spacer(Modifier.height(16.dp))

                    LabeledOutlinedField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        placeholder = "Full Name"
                    )

                    LabeledOutlinedField(
                        value = jobTitle,
                        onValueChange = { jobTitle = it },
                        placeholder = "Professional Title"
                    )

                    LabeledOutlinedField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "Email"
                    )

                    LabeledOutlinedField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = "Phone"
                    )

                    LabeledOutlinedField(
                        value = location,
                        onValueChange = { location = it },
                        placeholder = "Location (City, Country)"
                    )

                    LabeledOutlinedField(
                        value = linkedIn,
                        onValueChange = { linkedIn = it },
                        placeholder = "LinkedIn URL"
                    )

                    LabeledOutlinedField(
                        value = website,
                        onValueChange = { website = it },
                        placeholder = "Website URL"
                    )
                }
            }

            // SUMMARY
            item {
                FormSectionCard {
                    SectionHeading("Professional Summary")
                    OutlinedTextField(
                        value = summary,
                        onValueChange = { summary = it },
                        placeholder = { Text("Brief overview of your career and goals...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = MaterialTheme.shapes.small,
                        colors = cvFieldColors()
                    )
                }
            }

            // EXPERIENCE
            item {
                FormSectionCard {
                    SectionHeadingWithAction(
                        title = "Experience",
                        actionLabel = "Add Experience",
                        onAction = { experiences = experiences + ExperienceEntry() }
                    )

                    experiences.forEachIndexed { index, experience ->
                        ExperienceCard(
                            entry = experience,
                            onChange = { updatedExperience ->
                                experiences = experiences.toMutableList().also {
                                    it[index] = updatedExperience
                                }
                            },
                            onDelete = {
                                if (experiences.size > 1) {
                                    experiences = experiences.toMutableList().also {
                                        it.removeAt(index)
                                    }
                                }
                            }
                        )

                        if (index != experiences.lastIndex) {
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }
            }

            // EDUCATION
            item {
                FormSectionCard {
                    SectionHeadingWithAction(
                        title = "Education",
                        actionLabel = "Add Education",
                        onAction = { education = education + EducationEntry() }
                    )

                    education.forEachIndexed { index, edu ->
                        EducationCard(
                            entry = edu,
                            onChange = { updatedEducation ->
                                education = education.toMutableList().also {
                                    it[index] = updatedEducation
                                }
                            },
                            onDelete = {
                                if (education.size > 1) {
                                    education = education.toMutableList().also {
                                        it.removeAt(index)
                                    }
                                }
                            }
                        )

                        if (index != education.lastIndex) {
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }
            }

            // SKILLS
            item {
                FormSectionCard {
                    SectionHeading("Skills")

                    SkillChips(
                        skills = skills,
                        onRemove = { skill ->
                            skills = skills.filterNot { it == skill }
                        }
                    )

                    Spacer(Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = newSkill,
                            onValueChange = { newSkill = it },
                            placeholder = { Text("Add a skill") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = MaterialTheme.shapes.small,
                            colors = cvFieldColors()
                        )

                        Spacer(Modifier.width(8.dp))

                        Button(
                            onClick = {
                                val skill = newSkill.trim()
                                if (skill.isNotEmpty() && !skills.contains(skill)) {
                                    skills = skills + skill
                                    newSkill = ""
                                }
                            },
                            modifier = Modifier.height(56.dp)
                        ) {
                            Icon(Icons.Outlined.Add, contentDescription = "Add skill")
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(20.dp)) }
        }
    }
}

@Composable
private fun CreateCvTopBar(
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                }

                Text(
                    text = "Create CV",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Button(
                    onClick = onSave,
                    shape = MaterialTheme.shapes.small,
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Text("Save")
                }
            }
            HorizontalDivider()
        }
    }
}

@Composable
private fun FormSectionCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.large
            )
            .padding(16.dp),
        content = content
    )
}

@Composable
private fun SectionHeading(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(Modifier.height(12.dp))
    HorizontalDivider()
    Spacer(Modifier.height(16.dp))
}

@Composable
private fun SectionHeadingWithAction(
    title: String,
    actionLabel: String,
    onAction: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        TextButton(onClick = onAction) {
            Text("+ $actionLabel")
        }
    }
    HorizontalDivider()
    Spacer(Modifier.height(16.dp))
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PhotoUploadBox(
    selectedImageUri: Uri?,
    onImageSelected: (Uri?) -> Unit
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> onImageSelected(uri) }

    Column {
        Text(
            text = "Profile Photo",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = CircleShape
                )
                .clickable {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                GlideImage(
                    model = selectedImageUri,
                    contentDescription = "Profile photo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Outlined.CameraAlt,
                        contentDescription = "Upload photo",
                        modifier = Modifier.size(28.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Upload",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Tap to select a profile photo",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LabeledOutlinedField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        singleLine = true,
        shape = MaterialTheme.shapes.small,
        colors = cvFieldColors()
    )
}

@Composable
private fun ExperienceCard(
    entry: ExperienceEntry,
    onChange: (ExperienceEntry) -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                MaterialTheme.shapes.medium
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Experience Entry",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Delete experience",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        MiniLabeledField(
            label = "Company",
            value = entry.company,
            onValueChange = { onChange(entry.copy(company = it)) }
        )
        MiniLabeledField(
            label = "Role",
            value = entry.role,
            onValueChange = { onChange(entry.copy(role = it)) }
        )
        MiniLabeledField(
            label = "Dates",
            value = entry.dates,
            onValueChange = { onChange(entry.copy(dates = it)) }
        )
        MiniLabeledField(
            label = "Description",
            value = entry.description,
            minLines = 4,
            onValueChange = { onChange(entry.copy(description = it)) }
        )
    }
}

@Composable
private fun EducationCard(
    entry: EducationEntry,
    onChange: (EducationEntry) -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                MaterialTheme.shapes.medium
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Education Entry",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Delete education",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        MiniLabeledField(
            label = "School / University",
            value = entry.school,
            onValueChange = { onChange(entry.copy(school = it)) }
        )
        MiniLabeledField(
            label = "Degree",
            value = entry.degree,
            onValueChange = { onChange(entry.copy(degree = it)) }
        )
        MiniLabeledField(
            label = "Dates",
            value = entry.dates,
            onValueChange = { onChange(entry.copy(dates = it)) }
        )
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
        Spacer(Modifier.height(10.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
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
private fun SkillChips(
    skills: List<String>,
    onRemove: (String) -> Unit
) {
    if (skills.isEmpty()) {
        Text(
            text = "No skills added yet.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        return
    }

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        skills.forEach { skill ->
            Row(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = skill)
                Spacer(Modifier.width(6.dp))
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Remove $skill",
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onRemove(skill) }
                )
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

@Preview(showBackground = true, showSystemUi = true, heightDp = 1400)
@Composable
private fun CreateCvScreenPreview() {
    CVMakerAppTheme {
        CreateCvScreen()
    }
}
