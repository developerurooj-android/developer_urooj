package com.example.doctorappointmentapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctorappointmentapp.databinding.ItemAppointmentBinding

class AppointmentAdapter(
    private var appointmentList: List<Appointment>,
    private val onEdit: (Appointment) -> Unit,
    private val onDelete: (Appointment) -> Unit,
    private val onStatusChange: (Appointment, String) -> Unit
): RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    class AppointmentViewHolder(
        val binding: ItemAppointmentBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppointmentViewHolder {

        val binding = ItemAppointmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return AppointmentViewHolder(binding)
    }

    fun updateList(newList: List<Appointment>) {
        this.appointmentList = newList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: AppointmentViewHolder,
        position: Int
    ) {

        val appointment = appointmentList[position]

        // Set Appointment Data
        holder.binding.tvDoctorName.text = appointment.doctorName
        holder.binding.tvSpecialization.text = appointment.specialization
        holder.binding.tvDate.text = appointment.date
        holder.binding.tvTime.text = appointment.time
        holder.binding.tvStatus.text = appointment.status
        holder.binding.tvFee.text = "Rs. ${appointment.consultationFee}"

        // Load Doctor Image
        Glide.with(holder.itemView.context)
            .load(appointment.profileImage)
            .placeholder(R.drawable.doctor_placeholder)
            .error(R.drawable.doctor_placeholder)
            .into(holder.binding.imgDoctor)

        // Status view styling
        when (appointment.status) {
            "Upcoming" -> {
                holder.binding.tvStatus.setBackgroundResource(R.drawable.bg_circle_blue)
                holder.binding.tvStatus.setTextColor(android.graphics.Color.parseColor("#2D7FF9"))
            }
            "Completed" -> {
                holder.binding.tvStatus.setBackgroundResource(R.drawable.bg_circle_green)
                holder.binding.tvStatus.setTextColor(android.graphics.Color.parseColor("#10B981"))
            }
            "Cancelled" -> {
                holder.binding.tvStatus.setBackgroundResource(R.drawable.bg_circle_white) // Assuming a neutral bg
                holder.binding.tvStatus.setTextColor(android.graphics.Color.parseColor("#EF4444"))
            }
        }

        // Update Cancel Button State
        if (appointment.status == "Cancelled") {
            holder.binding.btnCancel.isEnabled = false
            holder.binding.btnCancel.text = "Cancelled"
        } else {
            holder.binding.btnCancel.isEnabled = true
            holder.binding.btnCancel.text = "Cancel"
        }

        // Action Buttons
        holder.binding.btnDetails.setOnClickListener {
            val intent = Intent(holder.itemView.context, AppointmentDetailsActivity::class.java).apply {
                putExtra("appointmentId", appointment.appointmentId)
                putExtra("doctorName", appointment.doctorName)
                putExtra("specialization", appointment.specialization)
                putExtra("date", appointment.date)
                putExtra("time", appointment.time)
                putExtra("status", appointment.status)
                putExtra("fee", appointment.consultationFee)
                putExtra("profileImage", appointment.profileImage)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.binding.btnEdit.setOnClickListener {
            onEdit(appointment)
        }

        holder.binding.btnCancel.setOnClickListener {
            onStatusChange(appointment, "Cancelled")
        }

        // Professional touch: long click to delete
        holder.itemView.setOnLongClickListener {
            onDelete(appointment)
            true
        }
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }
}
