package com.example.cvmakerapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
fun ProfessionalDetailsStep(
    jobTitle: String,
    email: String,
    phone: String,
    location: String,
    onJobTitleChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onSave: () -> Unit = {}
) {

    Scaffold(
        topBar = {
            StepTopBar(
                step = 2,
                onBack = onBack
            )
        },

        bottomBar = {
            StepNavigationButtons(
                currentStep = 2,
                onPrevious = onBack,
                onNext = onNext,
                onSave = onSave
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(ResponsiveUtils.getResponsivePadding()),
            verticalArrangement = Arrangement.spacedBy(ResponsiveUtils.getResponsiveSpacing())
        ) {

            item {

                Text(
                    text = "Tell us about your professional contact details",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    Modifier.height(ResponsiveUtils.getResponsivePadding())
                )

                OutlinedTextField(
                    value = jobTitle,
                    onValueChange = onJobTitleChange,
                    label = {
                        Text("Job Title")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(
                    Modifier.height(ResponsiveUtils.getResponsiveSpacing())
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = {
                        Text("Email")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(
                    Modifier.height(ResponsiveUtils.getResponsiveSpacing())
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = onPhoneChange,
                    label = {
                        Text("Phone")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(
                    Modifier.height(ResponsiveUtils.getResponsiveSpacing())
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = onLocationChange,
                    label = {
                        Text("Location")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }
    }
}

