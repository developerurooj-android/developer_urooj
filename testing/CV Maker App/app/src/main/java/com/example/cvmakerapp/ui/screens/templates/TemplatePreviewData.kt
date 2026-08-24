package com.example.cvmakerapp.ui.screens.templates

import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.EducationEntry
import com.example.cvmakerapp.data.ExperienceEntry

object TemplatePreviewData {

    val sample: CvData = CvData(
        firstName = "Alex",
        lastName = "Johnson",
        jobTitle = "Product Designer",
        location = "San Francisco, CA",
        email = "alex@email.com",
        phone = "+1 555 0100",
        linkedIn = "linkedin.com/in/alex",
        website = "alexjohnson.com",
        summary = "Creative professional with experience delivering user-centered digital products.",
        experiences = listOf(
            ExperienceEntry(
                role = "Senior Designer",
                company = "Tech Corp",
                dates = "2021 - Present",
                description = "Led design systems and product workflows."
            ),
            ExperienceEntry(
                role = "UI Designer",
                company = "Studio X",
                dates = "2018 - 2021",
                description = "Designed mobile and web interfaces."
            )
        ),
        education = listOf(
            EducationEntry(
                degree = "B.A. Design",
                school = "State University",
                dates = "2014 - 2018"
            )
        ),
        skills = listOf("Figma", "UX Research", "Prototyping", "Design Systems")
    )
}
