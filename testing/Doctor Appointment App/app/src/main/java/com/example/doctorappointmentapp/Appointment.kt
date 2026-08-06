package com.example.doctorappointmentapp

data class Appointment(

    val appointmentId: String = "",

    val patientId: String = "",

    val doctorId: String = "",

    val patientName: String = "",

    val doctorName: String = "",

    val specialization: String = "",

    val date: String = "",

    val time: String = "",

    var status: String = "",

    val symptoms: String = "",

    val notes: String = "",

    val consultationFee: Double = 0.0,

    val profileImage: String = ""

)