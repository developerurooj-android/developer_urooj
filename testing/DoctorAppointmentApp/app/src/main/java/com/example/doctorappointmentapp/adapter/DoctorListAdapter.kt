package com.example.doctorappointmentapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.databinding.ItemDoctorCardBinding
import com.example.doctorappointmentapp.model.Doctor

class DoctorListAdapter(
    private val onDoctorClick: (Doctor) -> Unit,
    private val onFavouriteClick: (Doctor, Boolean) -> Unit
) : ListAdapter<Doctor, DoctorListAdapter.DoctorVH>(DIFF) {

    private val favourites = mutableSetOf<String>()

    fun setFavourites(ids: Set<String>) {
        favourites.clear()
        favourites.addAll(ids)
        notifyDataSetChanged()
    }

    inner class DoctorVH(private val b: ItemDoctorCardBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(doctor: Doctor) {
            b.tvDoctorName.text    = "Dr. ${doctor.name}"
            b.tvSpecialty.text     = doctor.specialty
            b.tvHospital.text      = doctor.hospital.ifEmpty { "Private Clinic" }
            b.tvExperience.text    = if (doctor.experience.isNotEmpty()) "${doctor.experience} exp" else "—"
            b.tvFee.text           = if (doctor.fee > 0) "Rs ${doctor.fee.toInt()}" else "Free"
            b.tvRating.text        = doctor.rating.toString()
            b.tvReviews.text       = "(${doctor.reviewsCount})"
            b.tvOnlineStatus.text  = if (doctor.isOnline) "Online" else "Offline"
            b.tvOnlineStatus.setBackgroundResource(
                if (doctor.isOnline) R.drawable.bg_tag_available else R.drawable.bg_tag_offline
            )
            b.ivVerified.visibility = if (doctor.isVerified) android.view.View.VISIBLE else android.view.View.GONE
            val isFav = doctor.id in favourites
            b.btnFavourite.setImageResource(
                if (isFav) R.drawable.ic_favourite_filled else R.drawable.ic_favourite
            )
            Glide.with(b.ivDoctor).load(doctor.imageUrl)
                .placeholder(R.drawable.ic_doctor).circleCrop().into(b.ivDoctor)
            b.root.setOnClickListener { onDoctorClick(doctor) }
            b.btnFavourite.setOnClickListener {
                val nowFav = doctor.id !in favourites
                if (nowFav) favourites.add(doctor.id) else favourites.remove(doctor.id)
                b.btnFavourite.setImageResource(
                    if (nowFav) R.drawable.ic_favourite_filled else R.drawable.ic_favourite
                )
                onFavouriteClick(doctor, nowFav)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DoctorVH(ItemDoctorCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: DoctorVH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Doctor>() {
            override fun areItemsTheSame(a: Doctor, b: Doctor) = a.id == b.id
            override fun areContentsTheSame(a: Doctor, b: Doctor) = a == b
        }
    }
}
