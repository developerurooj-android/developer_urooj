package com.example.cvmakerapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cvmakerapp.ui.theme.ResponsiveUtils

@Composable
fun SkillsStep(
    skills: List<String>,
    onSkillsChange: (List<String>) -> Unit,
    onBack: () -> Unit,
    onPreview: () -> Unit
) {

    var newSkill by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(ResponsiveUtils.getResponsivePadding())
    ) {

        // ============================================
        // TITLE
        // ============================================

        Text(
            text = "Step 7 of 7",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.height(ResponsiveUtils.getResponsiveSpacing())
        )

        Text(
            text = "Skills",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(ResponsiveUtils.getResponsiveSpacing())
        )

        Text(
            text = "Add skills that represent your professional abilities.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(ResponsiveUtils.getResponsivePadding())
        )

        // ============================================
        // ADD SKILL
        // ============================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ResponsiveUtils.getResponsiveSpacing())
        ) {

            OutlinedTextField(
                value = newSkill,
                onValueChange = {
                    newSkill = it
                },
                modifier = Modifier.weight(1f),
                label = {
                    Text("Skill")
                },
                placeholder = {
                    Text("e.g. Kotlin")
                },
                singleLine = true
            )

            Button(
                onClick = {
                    val skill = newSkill.trim()

                    if (
                        skill.isNotEmpty() &&
                        !skills.contains(skill)
                    ) {

                        onSkillsChange(
                            skills + skill
                        )

                        newSkill = ""
                    }
                }
            ) {
                Text("Add")
            }
        }

        Spacer(
            modifier = Modifier.height(ResponsiveUtils.getResponsivePadding())
        )

        // ============================================
        // SKILL CHIPS
        // ============================================

        if (skills.isEmpty()) {

            Text(
                text = "No skills added yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        } else {

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ResponsiveUtils.getResponsiveSpacing()),
                verticalArrangement = Arrangement.spacedBy(ResponsiveUtils.getResponsiveSpacing())
            ) {

                skills.forEach { skill ->

                    SkillChip(
                        skill = skill,
                        onRemove = {

                            onSkillsChange(
                                skills.filterNot {
                                    it == skill
                                }
                            )
                        }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        // ============================================
        // NAVIGATION
        // ============================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ResponsiveUtils.getResponsiveSpacing())
        ) {

            TextButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Back")
            }

            Button(
                onClick = onPreview,
                modifier = Modifier.weight(1f)
            ) {
                Text("Preview CV")
            }
        }
    }
}


@Composable
private fun SkillChip(
    skill: String,
    onRemove: () -> Unit
) {

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {

        Row(
            modifier = Modifier.padding(
                start = 12.dp,
                end = 4.dp,
                top = 6.dp,
                bottom = 6.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = skill,
                style = MaterialTheme.typography.bodyMedium
            )

            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(28.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove $skill",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}