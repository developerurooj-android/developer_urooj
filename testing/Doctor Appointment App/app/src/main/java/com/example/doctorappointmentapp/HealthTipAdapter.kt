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

        holder.binding.imgHealthTip.setImageResource(tip.image)
        holder.binding.tvHealthTitle.text = tip.title
    }

    override fun getItemCount(): Int {
        return healthTips.size
    }
}