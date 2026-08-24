package com.example.cvmakerapp.data

import kotlinx.serialization.Serializable

@Serializable
data class ExperienceEntry(
    val company: String = "",
    val role: String = "",
    val dates: String = "",
    val description: String = ""
)