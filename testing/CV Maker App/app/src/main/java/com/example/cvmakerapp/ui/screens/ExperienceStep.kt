package com.example.cvmakerapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cvmakerapp.data.ExperienceEntry
import com.example.cvmakerapp.ui.theme.ResponsiveUtils

@Composable
fun ExperienceStep(
    experiences: List<ExperienceEntry>,
    onExperiencesChange: (List<ExperienceEntry>) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(ResponsiveUtils.getResponsivePadding()),
        verticalArrangement = Arrangement.spacedBy(ResponsiveUtils.getResponsiveSpacing())
    ) {

        item {

            Text(
                text = "Step 5 of 7",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier.height(ResponsiveUtils.getResponsiveSpacing())
            )

            Text(
                text = "Experience",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(ResponsiveUtils.getResponsiveSpacing())
            )

            Text(
                text = "Add your previous work experience.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(ResponsiveUtils.getResponsivePadding())
            )
        }

        itemsIndexed(experiences) { index, experience ->

            ExperienceEntryCard(
                entry = experience,

                onChange = { updatedEntry ->

                    onExperiencesChange(
                        experiences.toMutableList().apply {
                            set(index, updatedEntry)
                        }
                    )
                },

                onDelete = {

                    if (experiences.size > 1) {
                        onExperiencesChange(
                            experiences.toMutableList().apply {
                                removeAt(index)
                            }
                        )
                    }
                }
            )
        }

        item {

            Button(
                onClick = {
                    onExperiencesChange(
                        experiences + ExperienceEntry()
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("+ Add Experience")
            }

            Spacer(
                modifier = Modifier.height(ResponsiveUtils.getResponsivePadding())
            )

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
                    onClick = onNext,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Next →")
                }
            }
        }
    }
}


@Composable
private fun ExperienceEntryCard(
    entry: ExperienceEntry,
    onChange: (ExperienceEntry) -> Unit,
    onDelete: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Experience Entry",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (entry.company.isNotBlank() ||
                entry.role.isNotBlank()
            ) {

                IconButton(
                    onClick = onDelete
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete experience",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        OutlinedTextField(
            value = entry.company,
            onValueChange = {
                onChange(
                    entry.copy(company = it)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Company")
            },
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(ResponsiveUtils.getResponsiveSpacing())
        )

        OutlinedTextField(
            value = entry.role,
            onValueChange = {
                onChange(
                    entry.copy(role = it)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Role")
            },
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(ResponsiveUtils.getResponsiveSpacing())
        )

        OutlinedTextField(
            value = entry.dates,
            onValueChange = {
                onChange(
                    entry.copy(dates = it)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Dates")
            },
            placeholder = {
                Text("2022 - 2025")
            },
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(ResponsiveUtils.getResponsiveSpacing())
        )

        OutlinedTextField(
            value = entry.description,
            onValueChange = {
                onChange(
                    entry.copy(description = it)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(minOf(120.dp, ResponsiveUtils.getScreenHeightDp() * 0.2f)),
            label = {
                Text("Description")
            },
            minLines = 4
        )
    }
}