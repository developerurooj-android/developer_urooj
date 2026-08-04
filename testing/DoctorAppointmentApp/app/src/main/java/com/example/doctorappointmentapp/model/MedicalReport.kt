package com.example.doctorappointmentapp.model

data class MedicalReport(
    val id: String = "",
    val patientId: String = "",
    val title: String = "",
    val fileUrl: String = "",
    val fileType: String = "pdf",   // "pdf" | "jpg" | "png"
    val fileSize: String = "",
    val uploadedAt: Long = System.currentTimeMillis(),
    val doctorId: String = "",
    val doctorName: String = ""
)
