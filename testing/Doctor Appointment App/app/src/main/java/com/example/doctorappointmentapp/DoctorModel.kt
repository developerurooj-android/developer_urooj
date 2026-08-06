package com.example.doctorappointmentapp

data class DoctorModel(
    var id: String = "",
    var name: String = "",
    var speciality: String = "",
    var experience: String = "",
    var rating: Double = 0.0,
    var image: String = ""
)