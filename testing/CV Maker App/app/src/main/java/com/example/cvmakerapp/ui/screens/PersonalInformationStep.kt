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
fun PersonalInformationStep(
    firstName: String,
    lastName: String,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit = {}
) {

    Scaffold(

        bottomBar = {
            StepNavigationButtons(
                currentStep = 1,
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
                text = "Let's start with your name",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(ResponsiveUtils.getResponsivePadding())
            )

            OutlinedTextField(
                value = firstName,
                onValueChange = onFirstNameChange,
                label = {
                    Text("First Name")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(
                modifier = Modifier.height(ResponsiveUtils.getResponsiveSpacing())
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = onLastNameChange,
                label = {
                    Text("Last Name")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}