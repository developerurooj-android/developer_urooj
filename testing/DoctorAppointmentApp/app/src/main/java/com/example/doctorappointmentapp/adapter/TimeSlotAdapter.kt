package com.example.doctorappointmentapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.databinding.ItemTimeSlotBinding
import com.example.doctorappointmentapp.model.TimeSlot

class TimeSlotAdapter(
    private val onSlotSelected: (TimeSlot) -> Unit
) : ListAdapter<TimeSlot, TimeSlotAdapter.SlotVH>(DIFF) {

    private var selectedPos = -1

    inner class SlotVH(private val b: ItemTimeSlotBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(slot: TimeSlot, position: Int) {
            b.tvTime.text = slot.time
            val selected  = position == selectedPos
            val available = slot.isAvailable

            when {
                !available -> {
                    b.root.setBackgroundResource(R.drawable.bg_slot_unavailable)
                    b.tvTime.setTextColor(ContextCompat.getColor(b.root.context, R.color.gray_400))
                    b.root.isEnabled = false
                }
                selected -> {
                    b.root.setBackgroundResource(R.drawable.bg_slot_selected)
                    b.tvTime.setTextColor(ContextCompat.getColor(b.root.context, R.color.white))
                    b.root.isEnabled = true
                }
                else -> {
                    b.root.setBackgroundResource(R.drawable.bg_slot_normal)
                    b.tvTime.setTextColor(ContextCompat.getColor(b.root.context, R.color.text_primary))
                    b.root.isEnabled = true
                }
            }
            b.root.setOnClickListener {
                if (!available) return@setOnClickListener
                val prev = selectedPos
                selectedPos = position
                notifyItemChanged(prev)
                notifyItemChanged(selectedPos)
                onSlotSelected(slot)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SlotVH(ItemTimeSlotBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: SlotVH, position: Int) = holder.bind(getItem(position), position)

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<TimeSlot>() {
            override fun areItemsTheSame(a: TimeSlot, b: TimeSlot) = a.time == b.time
            override fun areContentsTheSame(a: TimeSlot, b: TimeSlot) = a == b
        }
    }
}
