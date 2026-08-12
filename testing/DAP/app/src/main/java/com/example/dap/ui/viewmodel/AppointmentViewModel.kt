package com.example.dap.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dap.data.local.AppDatabase
import com.example.dap.data.local.entity.Appointment
import com.example.dap.data.model.Doctor
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppointmentViewModel(application: Application) : AndroidViewModel(application) {
    private val appointmentDao = AppDatabase.getDatabase(application).appointmentDao()

    // For simplicity, we use a fixed userId = 1
    val appointments: StateFlow<List<Appointment>> = appointmentDao.getAppointmentsForUser(1)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun bookAppointment(doctor: Doctor, date: String, time: String) {
        viewModelScope.launch {
            val appointment = Appointment(
                userId = 1,
                doctorId = doctor.id,
                doctorName = doctor.name,
                doctorSpecialty = doctor.specialty,
                date = date,
                time = time
            )
            appointmentDao.bookAppointment(appointment)
        }
    }

    fun cancelAppointment(appointmentId: Int) {
        viewModelScope.launch {
            appointmentDao.cancelAppointment(appointmentId)
        }
    }
}
