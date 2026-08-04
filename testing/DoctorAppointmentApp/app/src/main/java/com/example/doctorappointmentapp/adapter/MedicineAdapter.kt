package com.example.doctorappointmentapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentapp.databinding.ItemMedicineBinding
import com.example.doctorappointmentapp.model.Medicine

class MedicineAdapter : ListAdapter<Medicine, MedicineAdapter.MedVH>(DIFF) {

    inner class MedVH(private val b: ItemMedicineBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(m: Medicine) {
            b.tvName.text         = m.name
            b.tvDosage.text       = m.dosage
            b.tvFrequency.text    = m.frequency
            b.tvDuration.text     = m.duration
            b.tvInstructions.text = m.instructions.ifEmpty { "—" }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MedVH(ItemMedicineBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MedVH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Medicine>() {
            override fun areItemsTheSame(a: Medicine, b: Medicine) = a.name == b.name
            override fun areContentsTheSame(a: Medicine, b: Medicine) = a == b
        }
    }
}
