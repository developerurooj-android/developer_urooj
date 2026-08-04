package com.example.doctorappointmentapp.model

data class Prescription(
    val id: String = "",
    val appointmentId: String = "",
    val patientId: String = "",
    val doctorId: String = "",
    val doctorName: String = "",
    val patientName: String = "",
    val diagnosis: String = "",
    val medicines: List<Medicine> = emptyList(),
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
