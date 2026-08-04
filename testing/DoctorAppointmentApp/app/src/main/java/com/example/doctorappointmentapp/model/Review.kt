package com.example.doctorappointmentapp.model

data class Review(
    val id: String = "",
    val doctorId: String = "",
    val patientId: String = "",
    val patientName: String = "",
    val patientImageUrl: String = "",
    val rating: Float = 0f,
    val comment: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
