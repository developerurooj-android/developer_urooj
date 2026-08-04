package com.example.doctorappointmentapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.model.Appointment

class RecentPatientAdapter(private val appointments: List<Appointment>) :
    RecyclerView.Adapter<RecentPatientAdapter.PatientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_patient_recent,
            parent,
            false
        )
        return PatientViewHolder(view)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.tvPatientName.text = appointment.patientName
        holder.tvAppointTime.text = "${appointment.time} | ${appointment.date}"
    }

    override fun getItemCount(): Int = appointments.size

    class PatientViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPatientName: TextView = view.findViewById(R.id.tvPatientName)
        val tvAppointTime: TextView = view.findViewById(R.id.tvAppointTime)
    }
}
