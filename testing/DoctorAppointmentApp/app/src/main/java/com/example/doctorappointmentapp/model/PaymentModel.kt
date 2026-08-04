package com.example.doctorappointmentapp.model

data class PaymentModel(
    val id: String = "",
    val appointmentId: String = "",
    val patientId: String = "",
    val doctorId: String = "",
    val amount: Double = 0.0,
    val method: String = "",          // "easypaisa" | "jazzcash" | "debit" | "credit"
    val screenshotUrl: String = "",
    val transactionId: String = "",
    val status: String = "Pending",   // Pending | UnderVerification | Approved | Rejected
    val createdAt: Long = System.currentTimeMillis()
)
