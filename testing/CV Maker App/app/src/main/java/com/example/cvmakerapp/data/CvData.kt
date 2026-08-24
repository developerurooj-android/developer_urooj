package com.example.cvmakerapp.data

import kotlinx.serialization.Serializable

@Serializable
enum class CvTemplate {
    CLASSIC,
    TWO_COLUMN,
    MODERN,
    MINIMAL,
    PROFESSIONAL
}

@Serializable
data class CvData(
    val id: Long = 0L,

    val firstName: String = "",
    val lastName: String = "",

    val jobTitle: String = "",
    val location: String = "",
    val email: String = "",
    val phone: String = "",

    val profileImageUri: String? = null,

    val linkedIn: String = "",
    val website: String = "",

    val summary: String = "",

    val experiences: List<ExperienceEntry> = emptyList(),

    val education: List<EducationEntry> = emptyList(),

    val skills: List<String> = emptyList(),
    
    val template: CvTemplate = CvTemplate.CLASSIC
) {
    val fullName: String
        get() = "$firstName $lastName".trim()
}