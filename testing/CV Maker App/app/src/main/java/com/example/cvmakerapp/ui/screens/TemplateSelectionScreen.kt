package com.example.cvmakerapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cvmakerapp.data.CvTemplate
import com.example.cvmakerapp.data.TemplateDesigns
import com.example.cvmakerapp.ui.screens.templates.CvTemplateRenderer
import com.example.cvmakerapp.ui.screens.templates.TemplatePreviewData
import com.example.cvmakerapp.ui.theme.ResponsiveUtils

private data class TemplateOption(
    val template: CvTemplate,
    val title: String,
    val description: String
)

private val templateOptions = listOf(
    TemplateOption(
        template = CvTemplate.CLASSIC,
        title = "Classic",
        description = "Clean traditional single-column layout with clear section dividers."
    ),
    TemplateOption(
        template = CvTemplate.TWO_COLUMN,
        title = "Two Column",
        description = "Sidebar with contact and skills; main content on the right."
    ),
    TemplateOption(
        template = CvTemplate.MODERN,
        title = "Modern",
        description = "Bold header with accent color and card-style sections."
    ),
    TemplateOption(
        template = CvTemplate.MINIMAL,
        title = "Minimal",
        description = "Simple, readable layout with subtle dividers and white space."
    ),
    TemplateOption(
        template = CvTemplate.PROFESSIONAL,
        title = "Professional",
        description = "Corporate-style layout with structured sections and accent bar."
    )
)

@Composable
fun TemplateSelectionScreen(
    onTemplateSelected: (CvTemplate) -> Unit,
    onBack: () -> Unit
) {
    var selectedTemplate by remember {
        mutableStateOf(CvTemplate.CLASSIC)
    }

    Scaffold(
        topBar = {
            TemplateSelectionTopBar(onBack = onBack)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Choose a CV Template",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Select a professional template to build your CV",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(ResponsiveUtils.getTemplateGridColumns()),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = ResponsiveUtils.getResponsiveHorizontalPadding())
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(ResponsiveUtils.getResponsiveSpacing()),
                verticalArrangement = Arrangement.spacedBy(ResponsiveUtils.getResponsiveSpacing()),
                contentPadding = PaddingValues(bottom = ResponsiveUtils.getResponsiveSpacing())
            ) {
                items(templateOptions) { option ->
                    TemplateCard(
                        title = option.title,
                        description = option.description,
                        isSelected = selectedTemplate == option.template,
                        onSelect = {
                            selectedTemplate = option.template
                        }
                    ) {
                        ScaledTemplatePreview(template = option.template)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ResponsiveUtils.getResponsiveHorizontalPadding()),
                horizontalArrangement = Arrangement.spacedBy(ResponsiveUtils.getResponsiveSpacing())
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .weight(1f)
                        .height(ResponsiveUtils.getResponsiveButtonHeight())
                ) {
                    Text("Back")
                }

                Button(
                    onClick = {
                        onTemplateSelected(selectedTemplate)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(ResponsiveUtils.getResponsiveButtonHeight())
                ) {
                    Text("Continue")
                }
            }
        }
    }
}

@Composable
private fun ScaledTemplatePreview(template: CvTemplate) {
    val previewData = remember(template) {
        TemplatePreviewData.sample.copy(template = template)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(720.dp)
                .graphicsLayer {
                    scaleX = 0.28f
                    scaleY = 0.28f
                    transformOrigin = TransformOrigin(0f, 0f)
                }
        ) {
            CvTemplateRenderer(
                cvData = previewData,
                modifier = Modifier.fillMaxSize()
            )
        }

        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp),
            shape = RoundedCornerShape(4.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.9f)
        ) {
            Text(
                text = TemplateDesigns.forTemplate(template).displayName.uppercase(),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun TemplateSelectionTopBar(
    onBack: () -> Unit
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
                text = "CV Templates",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Composable
private fun TemplateCard(
    title: String,
    description: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    preview: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                },
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onSelect),
        color = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = description,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                if (isSelected) {
                    Surface(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(50)),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = MaterialTheme.colorScheme.surface
            ) {
                preview()
            }
        }
    }
}
