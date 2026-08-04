package com.example.doctorappointmentapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.databinding.ItemReviewBinding
import com.example.doctorappointmentapp.model.Review
import java.text.SimpleDateFormat
import java.util.*

class ReviewAdapter : ListAdapter<Review, ReviewAdapter.ReviewVH>(DIFF) {

    inner class ReviewVH(private val b: ItemReviewBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(r: Review) {
            b.tvPatientName.text = r.patientName.ifEmpty { "Anonymous" }
            b.tvComment.text     = r.comment.ifEmpty { "—" }
            b.tvDate.text        = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(r.createdAt))
            b.ratingBar.rating   = r.rating
            Glide.with(b.ivPatient).load(r.patientImageUrl)
                .placeholder(R.drawable.ic_user).circleCrop().into(b.ivPatient)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ReviewVH(ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ReviewVH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(a: Review, b: Review) = a.id == b.id
            override fun areContentsTheSame(a: Review, b: Review) = a == b
        }
    }
}
