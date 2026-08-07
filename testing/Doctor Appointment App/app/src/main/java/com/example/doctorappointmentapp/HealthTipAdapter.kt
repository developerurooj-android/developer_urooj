package com.example.doctorappointmentapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentapp.databinding.ItemHealthTipBinding

class HealthTipAdapter(
    private val healthTips: List<HealthTip>
) : RecyclerView.Adapter<HealthTipAdapter.HealthTipViewHolder>() {

    inner class HealthTipViewHolder(
        val binding: ItemHealthTipBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthTipViewHolder {

        val binding = ItemHealthTipBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return HealthTipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HealthTipViewHolder, position: Int) {

        val tip = healthTips[position]

        val imageRes = when (tip.image) {
            "water" -> R.drawable.ic_water
            "exercise" -> R.drawable.ic_exercise
            "balanced_diet" -> R.drawable.ic_balanced_diet
            else -> R.drawable.ic_placeholder
        }

        holder.binding.imgHealthTip.setImageResource(imageRes)
        holder.binding.imgHealthTip.setImageResource(imageRes)
        holder.binding.tvHealthTitle.text = tip.title
    }

    override fun getItemCount(): Int {
        return healthTips.size
    }
}