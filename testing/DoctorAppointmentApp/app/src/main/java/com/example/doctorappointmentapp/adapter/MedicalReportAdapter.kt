package com.example.doctorappointmentapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.databinding.ItemReportBinding
import com.example.doctorappointmentapp.model.MedicalReport
import java.text.SimpleDateFormat
import java.util.*

class MedicalReportAdapter(
    private val onDelete: (MedicalReport) -> Unit
) : ListAdapter<MedicalReport, MedicalReportAdapter.ReportVH>(DIFF) {

    inner class ReportVH(private val b: ItemReportBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(r: MedicalReport) {
            b.tvTitle.text    = r.title
            b.tvDate.text     = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(r.uploadedAt))
            b.tvFileType.text = r.fileType.uppercase()
            b.ivIcon.setImageResource(
                if (r.fileType == "pdf") R.drawable.ic_file_pdf else R.drawable.ic_image
            )
            b.btnDelete.setOnClickListener { onDelete(r) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ReportVH(ItemReportBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ReportVH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<MedicalReport>() {
            override fun areItemsTheSame(a: MedicalReport, b: MedicalReport) = a.id == b.id
            override fun areContentsTheSame(a: MedicalReport, b: MedicalReport) = a == b
        }
    }
}
