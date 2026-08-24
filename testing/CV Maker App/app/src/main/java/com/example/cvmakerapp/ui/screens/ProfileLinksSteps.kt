package com.example.cvmakerapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cvmakerapp.ui.theme.ResponsiveUtils

@Composable
fun ProfileLinksStep(
    profileImageUri: String?,
    linkedIn: String,
    website: String,
    onImageSelected: (String?) -> Unit,
    onLinkedInChange: (String) -> Unit,
    onWebsiteChange: (String) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onSave: () -> Unit = {}
) {

    Scaffold(
        topBar = {
            StepTopBar(
                step = 3,
                onBack = onBack
            )
        },

        bottomBar = {
            StepNavigationButtons(
                currentStep = 3,
                onPrevious = onBack,
                onNext = onNext,
                onSave = onSave
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(ResponsiveUtils.getResponsivePadding())
        ) {

            Text(
                text = "Add your profile and online presence",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                Modifier.height(ResponsiveUtils.getResponsivePadding())
            )

            PhotoUploadBox(
                selectedImageUri = profileImageUri,
                onImageSelected = onImageSelected
            )

            Spacer(
                Modifier.height(ResponsiveUtils.getResponsivePadding())
            )

            OutlinedTextField(
                value = linkedIn,
                onValueChange = onLinkedInChange,
                label = {
                    Text("LinkedIn URL")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(
                Modifier.height(ResponsiveUtils.getResponsiveSpacing())
            )

            OutlinedTextField(
                value = website,
                onValueChange = onWebsiteChange,
                label = {
                    Text("Website URL")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}