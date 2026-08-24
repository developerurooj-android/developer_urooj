package com.example.cvmakerapp.ui.screens.templates

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.CvTemplate

@Composable
fun CvTemplateRenderer(
    cvData: CvData,
    modifier: Modifier = Modifier
) {
    when (cvData.template) {
        CvTemplate.CLASSIC -> ClassicCvTemplate(cvData = cvData, modifier = modifier)
        CvTemplate.TWO_COLUMN -> TwoColumnCvTemplate(cvData = cvData, modifier = modifier)
        CvTemplate.MODERN -> ModernCvTemplate(cvData = cvData, modifier = modifier)
        CvTemplate.MINIMAL -> MinimalCvTemplate(cvData = cvData, modifier = modifier)
        CvTemplate.PROFESSIONAL -> ProfessionalCvTemplate(cvData = cvData, modifier = modifier)
    }
}
