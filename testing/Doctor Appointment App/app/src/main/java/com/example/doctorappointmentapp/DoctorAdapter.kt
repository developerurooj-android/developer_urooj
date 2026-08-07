package com.example.doctorappointmentapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctorappointmentapp.databinding.ItemDoctorBinding

class DoctorAdapter(
    private val list: List<TopDoctor>
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

        Glide.with(holder.itemView.context)
            .load(doctor.profileImage)
            .placeholder(R.drawable.doctor_placeholder)
            .into(holder.binding.imgDoctor)

        holder.binding.tvDoctorName.text = doctor.doctorName
        holder.binding.tvSpecialization.text = doctor.specialization
        holder.binding.tvRating.text = "⭐ ${doctor.rating}"
        holder.binding.tvFee.text = "Rs. ${doctor.consultationFee}"
    }

    override fun getItemCount() = list.size
}