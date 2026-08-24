package com.example.cvmakerapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.ui.screens.templates.CvTemplateRenderer
import com.example.cvmakerapp.utils.PdfExporter

@Composable
fun CvPreviewScreen(
    cvData: CvData,
    onBack: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    val context = LocalContext.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        topBar = {
            CvPreviewTopBar(
                onBack = onBack,
                onExport = {
                    try {
                        val success = PdfExporter.exportCv(
                            context = context,
                            cvData = cvData,
                            screenshotBitmap = null
                        )

                        if (success) {
                            Toast.makeText(
                                context,
                                "CV downloaded to Downloads",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Unable to download CV",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (_: Exception) {
                        Toast.makeText(
                            context,
                            "Unable to download CV",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                onSave = {
                    onSave()
                    Toast.makeText(
                        context,
                        "CV saved successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    ) { innerPadding ->
        CvTemplateRenderer(
            cvData = cvData,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun CvPreviewTopBar(
    onBack: () -> Unit,
    onExport: () -> Unit,
    onSave: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = "CV Preview",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            IconButton(onClick = onSave) {
                Icon(
                    imageVector = Icons.Outlined.Save,
                    contentDescription = "Save CV",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onExport) {
                Icon(
                    imageVector = Icons.Outlined.PictureAsPdf,
                    contentDescription = "Download CV as PDF",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}
