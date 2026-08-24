package com.example.cvmakerapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cvmakerapp.ui.theme.ResponsiveUtils

@Composable
fun CvStepLayout(
    step: Int,
    title: String,
    onBack: () -> Unit,
    onNext: (() -> Unit)? = null,
    nextText: String = "Next",
    content: @Composable ColumnScope.() -> Unit
) {

    Scaffold(

        topBar = {

            Column {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = ResponsiveUtils.getResponsiveHorizontalPadding(),
                            vertical = ResponsiveUtils.getResponsiveSpacing()
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick = onBack
                    ) {

                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back"
                        )
                    }

                    Text(
                        text = title,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "$step / 7",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                HorizontalDivider()
            }
        },

        bottomBar = {

            Surface(
                tonalElevation = 3.dp
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(ResponsiveUtils.getResponsivePadding()),
                    horizontalArrangement =
                        Arrangement.spacedBy(ResponsiveUtils.getResponsiveSpacing())
                ) {

                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier
                            .weight(1f)
                            .height(ResponsiveUtils.getResponsiveButtonHeight())
                    ) {
                        Text("Back")
                    }

                    if (onNext != null) {

                        Button(
                            onClick = onNext,
                            modifier = Modifier
                                .weight(1f)
                                .height(ResponsiveUtils.getResponsiveButtonHeight())
                        ) {
                            Text(nextText)
                        }
                    }
                }
            }
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(ResponsiveUtils.getResponsivePadding()),
            content = content
        )
    }
}