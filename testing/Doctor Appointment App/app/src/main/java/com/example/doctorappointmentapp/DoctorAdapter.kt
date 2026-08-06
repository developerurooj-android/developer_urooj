package com.example.doctorappointmentapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentapp.databinding.ItemDoctorBinding

class DoctorAdapter(
    private val list: List<Doctor>
) : RecyclerView.Adapter<DoctorAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemDoctorBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemDoctorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val doctor = list[position]

        holder.binding.imgDoctor.setImageResource(doctor.image)
        holder.binding.tvDoctorName.text = doctor.name
        holder.binding.tvSpecialization.text = doctor.specialization
    }

    override fun getItemCount() = list.size
}