package com.example.doctorappointmentapp.model

import com.example.doctorappointmentapp.utils.Constants

data class Appointment(
    val id: String = "",
    val patientId: String = "",
    val doctorId: String = "",
    val patientName: String = "",
    val doctorName: String = "",
    val doctorImageUrl: String = "",
    val doctorSpecialty: String = "",
    val hospitalName: String = "",
    val date: String = "",
    val time: String = "",
    val status: String = Constants.STATUS_UPCOMING,
    val type: String = "Consultation",
    val fee: Double = 0.0,
    val paymentStatus: String = Constants.PAYMENT_UNPAID,
    val paymentId: String = "",
    val prescriptionId: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
