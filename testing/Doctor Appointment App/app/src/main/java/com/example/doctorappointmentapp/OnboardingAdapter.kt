package com.example.doctorappointmentapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentapp.databinding.ItemOnboardingBinding

class OnboardingAdapter(
    private val onboardingItems: List<OnboardingItem>
) : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    class OnboardingViewHolder(
        val binding: ItemOnboardingBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OnboardingViewHolder {

        val binding = ItemOnboardingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return OnboardingViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: OnboardingViewHolder,
        position: Int
    ) {

        val item = onboardingItems[position]

        holder.binding.imgOnboarding.setImageResource(item.image)
        holder.binding.tvTitle.text = item.title
        holder.binding.tvDescription.text = item.description
    }

    override fun getItemCount(): Int {
        return onboardingItems.size
    }
}