package com.example.cvmakerapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cvmakerapp.ui.theme.ResponsiveUtils

@Composable
fun ProfessionalSummaryStep(
    summary: String,
    onSummaryChange: (String) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(ResponsiveUtils.getResponsivePadding()),

        verticalArrangement = Arrangement.Top
    ) {

        // ============================================
        // STEP TITLE
        // ============================================

        Text(
            text = "Step 4 of 7",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.height(ResponsiveUtils.getResponsiveSpacing())
        )

        Text(
            text = "Professional Summary",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(ResponsiveUtils.getResponsiveSpacing())
        )

        Text(
            text = "Write a short summary about your professional background, experience and career goals.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(ResponsiveUtils.getResponsivePadding())
        )

        // ============================================
        // SUMMARY FIELD
        // ============================================

        OutlinedTextField(
            value = summary,

            onValueChange = onSummaryChange,

            modifier = Modifier
                .fillMaxWidth()
                .height(minOf(180.dp, ResponsiveUtils.getScreenHeightDp() * 0.25f)),

            placeholder = {
                Text(
                    "Example: Experienced software developer with 5+ years of experience..."
                )
            },

            label = {
                Text("Professional Summary")
            },

            minLines = 6,

            maxLines = 8,

            shape = MaterialTheme.shapes.medium
        )

        Spacer(
            modifier = Modifier.height(ResponsiveUtils.getResponsiveSpacing())
        )

        Text(
            text = "${summary.length} characters",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        // ============================================
        // NAVIGATION BUTTONS
        // ============================================

        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ResponsiveUtils.getResponsiveSpacing())
        ) {

            TextButton(
                onClick = onBack,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    vertical = ResponsiveUtils.getResponsiveSpacing()
                )
            ) {
                Text("Back")
            }

            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    vertical = ResponsiveUtils.getResponsiveSpacing()
                )
            ) {
                Text("Next →")
            }
        }
    }
}