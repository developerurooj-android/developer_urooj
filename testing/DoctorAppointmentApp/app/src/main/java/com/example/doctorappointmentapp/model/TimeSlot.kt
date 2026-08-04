package com.example.doctorappointmentapp.model

data class TimeSlot(
    val time: String = "",
    val isAvailable: Boolean = true,
    val isSelected: Boolean = false
)
