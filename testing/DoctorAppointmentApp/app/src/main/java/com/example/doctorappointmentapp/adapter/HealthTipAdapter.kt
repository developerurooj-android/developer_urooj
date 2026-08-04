package com.example.doctorappointmentapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentapp.databinding.ItemHealthTipBinding
import com.example.doctorappointmentapp.model.HealthTip

class HealthTipAdapter : ListAdapter<HealthTip, HealthTipAdapter.TipViewHolder>(DIFF) {

    inner class TipViewHolder(val binding: ItemHealthTipBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipViewHolder {
        val binding = ItemHealthTipBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        val tip = getItem(position)
        holder.binding.apply {
            tvTipTitle.text = tip.title
            tvTipDesc.text = tip.description
            if (tip.icon != 0) ivTipIcon.setImageResource(tip.icon)
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<HealthTip>() {
            override fun areItemsTheSame(oldItem: HealthTip, newItem: HealthTip) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: HealthTip, newItem: HealthTip) = oldItem == newItem
        }
    }
}
