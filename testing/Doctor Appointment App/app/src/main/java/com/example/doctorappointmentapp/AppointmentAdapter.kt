package com.example.doctorappointmentapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctorappointmentapp.databinding.ItemAppointmentBinding
import com.google.firebase.firestore.FirebaseFirestore

class AppointmentAdapter(
    private val appointmentList: ArrayList<Appointment>
) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    inner class AppointmentViewHolder(
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

        // Update Cancel Button State
        if (appointment.status == "Cancelled") {

            holder.binding.btnCancel.isEnabled = false
            holder.binding.btnCancel.text = "Cancelled"

        } else {

            holder.binding.btnCancel.isEnabled = true
            holder.binding.btnCancel.text = "Cancel"
        }

        // Details Button
        holder.binding.btnDetails.setOnClickListener {

            val intent = Intent(
                holder.itemView.context,
                AppointmentDetailsActivity::class.java
            )

            intent.putExtra("appointmentId", appointment.appointmentId)
            intent.putExtra("doctorName", appointment.doctorName)
            intent.putExtra("specialization", appointment.specialization)
            intent.putExtra("date", appointment.date)
            intent.putExtra("time", appointment.time)
            intent.putExtra("status", appointment.status)
            intent.putExtra("fee", appointment.consultationFee)
            intent.putExtra("profileImage", appointment.profileImage)

            holder.itemView.context.startActivity(intent)
        }

        // Cancel Button
        holder.binding.btnCancel.setOnClickListener {

            if (appointment.appointmentId.isEmpty()) {

                Toast.makeText(
                    holder.itemView.context,
                    "Invalid Appointment",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            FirebaseFirestore.getInstance()
                .collection("appointments")
                .document(appointment.appointmentId)
                .update("status", "Cancelled")
                .addOnSuccessListener {

                    appointment.status = "Cancelled"

                    notifyItemChanged(position)

                    Toast.makeText(
                        holder.itemView.context,
                        "Appointment Cancelled Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->

                    Toast.makeText(
                        holder.itemView.context,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }
}