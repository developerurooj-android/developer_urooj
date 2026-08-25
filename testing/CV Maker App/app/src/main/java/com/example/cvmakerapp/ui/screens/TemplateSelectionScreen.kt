package com.example.cvmakerapp.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cvmakerapp.data.CvTemplate
import com.example.cvmakerapp.data.TemplateDesigns
import com.example.cvmakerapp.ui.screens.templates.CvTemplateRenderer
import com.example.cvmakerapp.ui.screens.templates.TemplatePreviewData
import com.example.cvmakerapp.ui.theme.CardShadowElevated
import com.example.cvmakerapp.ui.theme.CardShadowSoft
import com.example.cvmakerapp.ui.theme.ResponsiveUtils

private data class TemplateOption(
    val template: CvTemplate,
    val title: String,
    val description: String,
    val badge: String
)

private val templateOptions = listOf(
    TemplateOption(
        template = CvTemplate.CLASSIC,
        title = "Classic",
        description = "Timeless single-column layout with clear hierarchy and elegant section dividers.",
        badge = "Popular"
    ),
    TemplateOption(
        template = CvTemplate.TWO_COLUMN,
        title = "Two Column",
        description = "Structured sidebar with contact & skills; spacious main content area.",
        badge = "Balanced"
    ),
    TemplateOption(
        template = CvTemplate.MODERN,
        title = "Modern",
        description = "Bold accent header, card-based sections, and contemporary visual flow.",
        badge = "Creative"
    ),
    TemplateOption(
        template = CvTemplate.MINIMAL,
        title = "Minimal",
        description = "Generous whitespace, refined typography, and understated elegance.",
        badge = "Clean"
    ),
    TemplateOption(
        template = CvTemplate.PROFESSIONAL,
        title = "Professional",
        description = "Corporate-grade structure with accent markers and polished sections.",
        badge = "Executive"
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
        containerColor = MaterialTheme.colorScheme.background,
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
                    .padding(
                        horizontal = ResponsiveUtils.getResponsiveHorizontalPadding(),
                        vertical = ResponsiveUtils.getResponsivePadding()
                    )
            ) {
                Text(
                    text = "Choose a CV Template",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Text(
                    text = "Pick a professional design that matches your style and industry",
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
                contentPadding = PaddingValues(bottom = ResponsiveUtils.getResponsivePadding())
            ) {
                items(templateOptions) { option ->
                    val design = TemplateDesigns.forTemplate(option.template)
                    TemplateCard(
                        title = option.title,
                        description = option.description,
                        badge = option.badge,
                        accentColor = design.accentColor,
                        isSelected = selectedTemplate == option.template,
                        onSelect = {
                            selectedTemplate = option.template
                        }
                    ) {
                        ScaledTemplatePreview(template = option.template)
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shadowElevation = 4.dp,
                tonalElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = ResponsiveUtils.getResponsiveHorizontalPadding(),
                            vertical = ResponsiveUtils.getResponsivePadding()
                        ),
                    horizontalArrangement = Arrangement.spacedBy(ResponsiveUtils.getResponsiveSpacing())
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier
                            .weight(1f)
                            .height(ResponsiveUtils.getResponsiveButtonHeight()),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    ) {
                        Text(
                            "Back",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    }

                    Button(
                        onClick = {
                            onTemplateSelected(selectedTemplate)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(ResponsiveUtils.getResponsiveButtonHeight()),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 0.dp,
                            hoveredElevation = 4.dp
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            "Continue",
                            fontWeight = FontWeight.SemiBold
                        )
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
    val design = TemplateDesigns.forTemplate(template)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .shadow(
                elevation = 2.dp,
                spotColor = CardShadowSoft,
                ambientColor = CardShadowSoft,
                shape = RoundedCornerShape(10.dp)
            )
            .background(design.pageBackground)
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
                .align(Alignment.TopEnd)
                .padding(8.dp),
            shape = RoundedCornerShape(6.dp),
            color = design.accentColor.copy(alpha = 0.92f),
            shadowElevation = 1.dp
        ) {
            Text(
                text = design.displayName,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.White
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
                .padding(
                    horizontal = ResponsiveUtils.getResponsiveHorizontalPadding() - 4.dp,
                    vertical = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    tonalElevation = 1.dp
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

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
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 0.5.dp
        )
    }
}

@Composable
private fun TemplateCard(
    title: String,
    description: String,
    badge: String,
    accentColor: androidx.compose.ui.graphics.Color,
    isSelected: Boolean,
    onSelect: () -> Unit,
    preview: @Composable () -> Unit
) {
    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 2.dp else 1.dp,
        animationSpec = tween(200), label = "borderWidth"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) accentColor else MaterialTheme.colorScheme.outlineVariant,
        animationSpec = tween(200), label = "borderColor"
    )
    val shadowElevation by animateDpAsState(
        targetValue = if (isSelected) 8.dp else 2.dp,
        animationSpec = tween(200), label = "shadow"
    )
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.surfaceContainerLowest
        else MaterialTheme.colorScheme.surfaceContainerLowest,
        animationSpec = tween(200), label = "container"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = shadowElevation,
                spotColor = if (isSelected) CardShadowElevated else CardShadowSoft,
                ambientColor = if (isSelected) CardShadowElevated else CardShadowSoft,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onSelect),
        color = containerColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(accentColor)
                                )

                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = accentColor.copy(alpha = 0.10f)
                                ) {
                                    Text(
                                        text = badge,
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 3.dp
                                        ),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = accentColor
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        if (isSelected) {
                            Surface(
                                modifier = Modifier.size(28.dp),
                                shape = RoundedCornerShape(50),
                                color = accentColor,
                                shadowElevation = 2.dp
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = androidx.compose.ui.graphics.Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    ) {
                        preview()
                    }
                }
            }
        }
    }
}


