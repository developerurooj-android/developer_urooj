package com.example.doctorappointmentapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.model.OnboardingItem

class OnboardingViewPagerAdapter(private val onboardingItems: List<OnboardingItem>) :
    RecyclerView.Adapter<OnboardingViewPagerAdapter.OnboardingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.onboarding_item,
            parent,
            false
        )
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(onboardingItems[position])
    }

    override fun getItemCount(): Int {
        Log.d("OnboardingAdapter", "getItemCount: ${onboardingItems.size}")
        return onboardingItems.size
    }

    inner class OnboardingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ivIllustration: ImageView = view.findViewById(R.id.ivIllustration)
        private val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        private val tvDescription: TextView = view.findViewById(R.id.tvDescription)

        fun bind(onboardingItem: OnboardingItem) {
            ivIllustration.setImageResource(onboardingItem.image)
            tvTitle.text = onboardingItem.title
            tvDescription.text = onboardingItem.description
        }
    }
}
