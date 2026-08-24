package com.example.cvmakerapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StepNavigationButtons(
    currentStep: Int,
    totalSteps: Int = 7,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSave: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        // Show Previous from step 2 onward
        if (currentStep > 1) {
            OutlinedButton(
                onClick = onPrevious,
                modifier = Modifier.weight(1f)
            ) {
                Text("Previous")
            }
        }

        // Show Next until the last step
        if (currentStep < totalSteps) {
            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f)
            ) {
                Text("Next")
            }
        } else {
            // Step 7
            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f)
            ) {
                Text("Save CV")
            }
        }
    }
}