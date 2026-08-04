package com.example.doctorappointmentapp.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.databinding.ItemAppointmentCardBinding
import com.example.doctorappointmentapp.model.Appointment
import com.example.doctorappointmentapp.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

class AppointmentAdapter(
    private val role: String = Constants.ROLE_PATIENT,
    private val onActionClick: (Appointment, String) -> Unit
) : ListAdapter<Appointment, AppointmentAdapter.ApptVH>(DIFF) {

    inner class ApptVH(private val b: ItemAppointmentCardBinding) : RecyclerView.ViewHolder(b.root) {

        fun bind(a: Appointment) {
            val isPatient = role == Constants.ROLE_PATIENT
            
            // Name display based on role
            b.tvDoctorName.text = if (isPatient) {
                if (a.doctorName.startsWith("Dr.")) a.doctorName else "Dr. ${a.doctorName}"
            } else {
                a.patientName
            }
            
            b.tvSpecialty.text  = if (isPatient) a.doctorSpecialty.ifEmpty { "Consultation" } else "Patient"
            b.tvFullDate.text   = a.date
            b.tvTime.text       = a.time
            b.tvFee.text        = if (a.fee > 0) "Rs ${a.fee.toInt()}" else "Free"

            // Date Badge
            val parts = a.date.trim().split(" ")
            b.tvDateDayNumber.text = parts.getOrElse(0) { "-" }
            b.tvDateDayName.text   = parts.getOrElse(1) { "-" }.uppercase().take(3)

            b.tvHospital.text = a.hospitalName.ifEmpty { "Private Clinic" }

            val imageUrl = if (isPatient) a.doctorImageUrl else "" // Patient image could be added to model if available
            Glide.with(b.ivDoctor).load(imageUrl)
                .placeholder(if (isPatient) R.drawable.ic_doctor else R.drawable.ic_user)
                .circleCrop().into(b.ivDoctor)

            bindStatusChip(a.status)
            bindPaymentBadge(a.paymentStatus)
            bindActions(a, isPatient)

            b.root.setOnClickListener { onActionClick(a, "details") }
        }

        private fun bindStatusChip(status: String) {
            val clean = status.trim()
            val pair = when (clean) {
                Constants.STATUS_ACCEPTED -> Quad("#10B981", "#D1FAE5", "#A7F3D0", "Accepted")
                Constants.STATUS_COMPLETED -> Quad("#1565C0", "#E3F2FD", "#90CAF9", "Completed")
                Constants.STATUS_CANCELLED -> Quad("#EF4444", "#FEE2E2", "#FECACA", "Cancelled")
                Constants.STATUS_UPCOMING -> Quad("#3B82F6", "#DBEAFE", "#BFDBFE", "Upcoming")
                else -> Quad("#F59E0B", "#FEF3C7", "#FDE68A", clean)
            }
            val (textColor, bgColor, strokeColor, label) = pair
            b.cvStatus.setCardBackgroundColor(android.graphics.Color.parseColor(bgColor))
            b.cvStatus.strokeColor = android.graphics.Color.parseColor(strokeColor)
            b.tvStatus.text = label
            b.tvStatus.setTextColor(android.graphics.Color.parseColor(textColor))
        }

        private fun bindPaymentBadge(paymentStatus: String) {
            val clean = paymentStatus.trim().lowercase()
            val pair = when {
                clean == "paid" || clean.contains("approve") -> "PAID" to R.color.success_green
                clean == "unpaid" || clean.contains("pending") -> "UNPAID" to R.color.warning_orange
                else -> clean.uppercase() to R.color.text_hint
            }
            b.tvPayStatus.text = pair.first
            b.tvPayStatus.setTextColor(ContextCompat.getColor(b.root.context, pair.second))
        }

        private fun bindActions(a: Appointment, isPatient: Boolean) {
            val status = a.status
            
            // Hide everything first
            b.layoutCallActions.visibility = View.GONE
            b.layoutActions.visibility = View.GONE
            b.btnAction1.visibility = View.VISIBLE
            b.btnAction2.visibility = View.VISIBLE

            if (isPatient) {
                when (status) {
                    Constants.STATUS_UPCOMING, Constants.STATUS_ACCEPTED -> {
                        b.layoutActions.visibility = View.VISIBLE
                        b.btnAction1.text = "Cancel"
                        b.btnAction1.setOnClickListener { onActionClick(a, "cancel") }
                        
                        b.btnAction2.text = "Details"
                        b.btnAction2.setOnClickListener { onActionClick(a, "details") }

                        // Video Call logic
                        if (status == Constants.STATUS_ACCEPTED && isTimeStarted(a.date, a.time)) {
                            b.layoutCallActions.visibility = View.VISIBLE
                            b.btnVideoCall.setOnClickListener { onActionClick(a, "video_call") }
                            b.btnAudioCall.setOnClickListener { onActionClick(a, "audio_call") }
                        }
                    }
                    Constants.STATUS_COMPLETED -> {
                        b.layoutActions.visibility = View.VISIBLE
                        b.btnAction1.text = "Prescription"
                        b.btnAction1.setOnClickListener { onActionClick(a, "prescription") }
                        
                        b.btnAction2.text = "Rate Doctor"
                        b.btnAction2.setOnClickListener { onActionClick(a, "rate") }
                    }
                    Constants.STATUS_CANCELLED -> {
                        b.layoutActions.visibility = View.VISIBLE
                        b.btnAction1.text = "Rebook"
                        b.btnAction1.setOnClickListener { onActionClick(a, "rebook") }
                        b.btnAction2.visibility = View.GONE
                    }
                }
            } else {
                // Doctor Side
                when (status) {
                    Constants.STATUS_UPCOMING -> {
                        b.layoutActions.visibility = View.VISIBLE
                        b.btnAction1.text = "Reject"
                        b.btnAction1.setOnClickListener { onActionClick(a, "reject") }
                        
                        b.btnAction2.text = "Accept"
                        b.btnAction2.setOnClickListener { onActionClick(a, "accept") }
                    }
                    Constants.STATUS_ACCEPTED -> {
                        b.layoutActions.visibility = View.VISIBLE
                        b.btnAction1.text = "Cancel"
                        b.btnAction1.setOnClickListener { onActionClick(a, "cancel") }
                        
                        b.btnAction2.text = "Complete"
                        b.btnAction2.setOnClickListener { onActionClick(a, "complete") }
                        
                        if (isTimeStarted(a.date, a.time)) {
                            b.layoutCallActions.visibility = View.VISIBLE
                            b.btnVideoCall.setOnClickListener { onActionClick(a, "video_call") }
                            b.btnAudioCall.setOnClickListener { onActionClick(a, "audio_call") }
                        }
                    }
                    Constants.STATUS_COMPLETED -> {
                        b.layoutActions.visibility = View.VISIBLE
                        b.btnAction1.text = "View Prescription"
                        b.btnAction1.setOnClickListener { onActionClick(a, "prescription") }
                        b.btnAction2.visibility = View.GONE
                    }
                    Constants.STATUS_CANCELLED -> {
                        b.layoutActions.visibility = View.GONE
                    }
                }
            }
        }

        private fun isTimeStarted(dateStr: String, timeStr: String): Boolean {
            return try {
                // Example: "15 Oct 2026", "10:00 AM - 10:30 AM"
                val sdf = SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault())
                val startTimeStr = timeStr.split("-")[0].trim()
                val appointmentDateTime = sdf.parse("$dateStr $startTimeStr")
                val now = Calendar.getInstance().time
                appointmentDateTime != null && now.after(appointmentDateTime)
            } catch (e: Exception) {
                true // Fallback to enabled if parsing fails
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ApptVH(ItemAppointmentCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ApptVH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Appointment>() {
            override fun areItemsTheSame(a: Appointment, b: Appointment) = a.id == b.id
            override fun areContentsTheSame(a: Appointment, b: Appointment) = a == b
        }
    }

    private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
}
