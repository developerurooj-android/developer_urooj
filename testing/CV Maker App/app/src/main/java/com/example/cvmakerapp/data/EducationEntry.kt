package com.example.cvmakerapp.data

import kotlinx.serialization.Serializable

@Serializable
data class EducationEntry(
    val school: String = "",
    val degree: String = "",
    val dates: String = ""
)