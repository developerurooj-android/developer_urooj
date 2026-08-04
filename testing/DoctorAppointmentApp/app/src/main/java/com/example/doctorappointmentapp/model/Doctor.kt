package com.example.doctorappointmentapp.model

data class Doctor(
    val id: String = "",
    val name: String = "",
    val specialty: String = "",
    val rating: Double = 0.0,
    val reviewsCount: Int = 0,
    val imageUrl: String = "",
    val description: String = "",
    val experience: String = "",
    val patientCount: String = "",
    // Extended fields for Doctor List / Details
    val hospital: String = "",
    val fee: Double = 0.0,
    val languages: List<String> = emptyList(),
    val qualifications: String = "",
    val isOnline: Boolean = false,
    val isVerified: Boolean = true,
    val availableSlots: List<String> = emptyList(),
    val uid: String = ""
)
