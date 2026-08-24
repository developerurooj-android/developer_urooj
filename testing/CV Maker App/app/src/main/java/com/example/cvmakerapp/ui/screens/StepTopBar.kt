package com.example.cvmakerapp.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepTopBar(
    step: Int,
    onBack: () -> Unit,
    showBack: Boolean = true
) {
    TopAppBar(
        title = {
            Text(
                text = when (step) {
                    0 -> "Personal Information"
                    1 -> "Experience"
                    2 -> "Professional Contact"
                    3 -> "Social Links"
                    else -> "Create CV"
                }
            )
        },
        navigationIcon = {
            if (showBack) {
                IconButton(
                    onClick = onBack
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}