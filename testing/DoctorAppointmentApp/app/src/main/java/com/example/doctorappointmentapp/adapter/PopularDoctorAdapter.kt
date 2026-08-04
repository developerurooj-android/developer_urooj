package com.example.doctorappointmentapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.databinding.ItemDoctorPopularBinding
import com.example.doctorappointmentapp.model.Doctor

class PopularDoctorAdapter(
    private val onClick: (Doctor) -> Unit
) : ListAdapter<Doctor, PopularDoctorAdapter.DoctorViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val binding = ItemDoctorPopularBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DoctorViewHolder(private val binding: ItemDoctorPopularBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(doctor: Doctor) {
            binding.apply {
                tvDoctorName.text = "Dr. ${doctor.name}"
                tvSpecialty.text  = doctor.specialty
                tvRating.text     = doctor.rating.toString()
                tvRatingBadge.text = doctor.rating.toString()
                tvHospital.text   = doctor.hospital.ifEmpty { "Private Clinic" }
                ivVerified.visibility = if (doctor.isVerified) View.VISIBLE else View.GONE
                viewOnlineDot.visibility = if (doctor.isOnline) View.VISIBLE else View.GONE
                
                Glide.with(ivDoctor)
                    .load(doctor.imageUrl)
                    .placeholder(R.drawable.ic_doctor)
                    .centerCrop()
                    .into(ivDoctor)
                
                root.setOnClickListener { onClick(doctor) }
            }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Doctor>() {
            override fun areItemsTheSame(oldItem: Doctor, newItem: Doctor) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Doctor, newItem: Doctor) = oldItem == newItem
        }
    }
}
