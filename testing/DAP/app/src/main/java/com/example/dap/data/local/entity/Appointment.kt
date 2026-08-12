package com.example.dap.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val doctorId: Int,
    val doctorName: String,
    val doctorSpecialty: String,
    val date: String,
    val time: String,
    val status: String = "Upcoming" // Upcoming, Completed, Cancelled
)
