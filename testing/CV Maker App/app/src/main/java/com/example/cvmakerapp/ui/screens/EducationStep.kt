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
import com.example.cvmakerapp.data.EducationEntry
import com.example.cvmakerapp.ui.theme.ResponsiveUtils

@Composable
fun EducationStep(
    education: List<EducationEntry>,
    onEducationChange: (List<EducationEntry>) -> Unit,
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
                text = "Step 6 of 7",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier.height(ResponsiveUtils.getResponsiveSpacing())
            )

            Text(
                text = "Education",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(ResponsiveUtils.getResponsiveSpacing())
            )

            Text(
                text = "Add your educational background.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        itemsIndexed(education) { index, entry ->

            EducationEntryCard(
                entry = entry,

                onChange = { updatedEntry ->
                    onEducationChange(
                        education.toMutableList().apply {
                            set(index, updatedEntry)
                        }
                    )
                },

                onDelete = {

                    if (education.size > 1) {
                        onEducationChange(
                            education.toMutableList().apply {
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
                    onEducationChange(
                        education + EducationEntry()
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("+ Add Education")
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
private fun EducationEntryCard(
    entry: EducationEntry,
    onChange: (EducationEntry) -> Unit,
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
                text = "Education Entry",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (entry.school.isNotBlank() ||
                entry.degree.isNotBlank()
            ) {

                IconButton(
                    onClick = onDelete
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete education",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        OutlinedTextField(
            value = entry.school,
            onValueChange = {
                onChange(
                    entry.copy(school = it)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("University / School")
            },
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(ResponsiveUtils.getResponsiveSpacing())
        )

        OutlinedTextField(
            value = entry.degree,
            onValueChange = {
                onChange(
                    entry.copy(degree = it)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Degree")
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
                Text("2018 - 2022")
            },
            singleLine = true
        )
    }
}