package com.example.dap.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.dap.data.local.entity.Appointment
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {
    @Insert
    suspend fun bookAppointment(appointment: Appointment)

    @Query("SELECT * FROM appointments WHERE userId = :userId ORDER BY date DESC, time DESC")
    fun getAppointmentsForUser(userId: Int): Flow<List<Appointment>>

    @Query("DELETE FROM appointments WHERE id = :appointmentId")
    suspend fun cancelAppointment(appointmentId: Int)
}
