package com.example.doctorappointmentapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.databinding.ActivityAppointmentDetailsBinding
import com.example.doctorappointmentapp.model.Appointment
import com.example.doctorappointmentapp.utils.Constants
import com.example.doctorappointmentapp.viewmodel.AppointmentViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AppointmentDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentDetailsBinding
    private val viewModel: AppointmentViewModel by viewModels()
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db   by lazy { FirebaseFirestore.getInstance() }
    
    private var appointment: Appointment? = null
    private var userRole: String = Constants.ROLE_PATIENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appointmentId = intent.getStringExtra(Constants.EXTRA_APPOINTMENT_ID)
        if (appointmentId == null) {
            finish()
            return
        }

        setupToolbar()
        fetchRoleAndLoad(appointmentId)
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun fetchRoleAndLoad(id: String) {
        val uid = auth.currentUser?.uid ?: return
        db.collection(Constants.USERS_COLLECTION).document(uid).get()
            .addOnSuccessListener { doc ->
                userRole = doc.getString("role") ?: Constants.ROLE_PATIENT
                loadAppointment(id)
            }
    }

    private fun loadAppointment(id: String) {
        db.collection(Constants.APPOINTMENTS_COLLECTION)
            .document(id)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                appointment = snapshot?.toObject(Appointment::class.java)?.copy(id = snapshot.id)
                appointment?.let { bindData(it) }
            }
    }

    private fun bindData(a: Appointment) {
        val isPatient = userRole == Constants.ROLE_PATIENT
        
        binding.tvDoctorName.text = if (isPatient) {
            if (a.doctorName.startsWith("Dr.")) a.doctorName else "Dr. ${a.doctorName}"
        } else {
            a.patientName
        }
        
        binding.tvSpecialty.text = if (isPatient) a.doctorSpecialty else "Patient"
        binding.tvHospital.text = a.hospitalName.ifEmpty { "Private Clinic" }
        binding.tvDate.text = a.date
        binding.tvTime.text = a.time
        binding.tvStatus.text = a.status
        binding.tvFee.text = "Rs ${a.fee.toInt()}"
        binding.tvPaymentStatus.text = a.paymentStatus
        binding.tvNotes.text = a.notes.ifEmpty { "No extra notes provided." }

        val imageUrl = if (isPatient) a.doctorImageUrl else ""
        Glide.with(this).load(imageUrl)
            .placeholder(if (isPatient) R.drawable.ic_doctor else R.drawable.ic_user)
            .circleCrop().into(binding.ivDoctor)

        // Setup Buttons based on status and role
        setupActions(a, isPatient)
    }

    private fun setupActions(a: Appointment, isPatient: Boolean) {
        val status = a.status
        binding.btnMainAction.visibility = View.GONE
        binding.btnSecondaryAction.visibility = View.GONE

        if (isPatient) {
            when (status) {
                Constants.STATUS_UPCOMING, Constants.STATUS_ACCEPTED -> {
                    binding.btnSecondaryAction.visibility = View.VISIBLE
                    binding.btnSecondaryAction.text = "Cancel Appointment"
                    binding.btnSecondaryAction.setOnClickListener { showCancelDialog(a.id) }
                    
                    if (status == Constants.STATUS_ACCEPTED) {
                        binding.btnMainAction.visibility = View.VISIBLE
                        binding.btnMainAction.text = "Join Video Call"
                        binding.btnMainAction.setOnClickListener { joinCall(a) }
                    } else if (a.paymentStatus == Constants.PAYMENT_UNPAID) {
                        binding.btnMainAction.visibility = View.VISIBLE
                        binding.btnMainAction.text = "Pay Now"
                        binding.btnMainAction.setOnClickListener {
                            startActivity(Intent(this, PaymentActivity::class.java).putExtra(Constants.EXTRA_APPOINTMENT_ID, a.id))
                        }
                    }
                }
                Constants.STATUS_COMPLETED -> {
                    binding.btnMainAction.visibility = View.VISIBLE
                    binding.btnMainAction.text = "View Prescription"
                    binding.btnMainAction.setOnClickListener { viewPrescription(a) }
                    
                    binding.btnSecondaryAction.visibility = View.VISIBLE
                    binding.btnSecondaryAction.text = "Rate Doctor"
                    binding.btnSecondaryAction.setOnClickListener { rateDoctor(a) }
                }
                Constants.STATUS_CANCELLED -> {
                    binding.btnMainAction.visibility = View.VISIBLE
                    binding.btnMainAction.text = "Book Again"
                    binding.btnMainAction.setOnClickListener {
                        startActivity(Intent(this, DoctorDetailsActivity::class.java).putExtra(Constants.EXTRA_DOCTOR_ID, a.doctorId))
                    }
                }
            }
        } else {
            // Doctor role actions
            when (status) {
                Constants.STATUS_UPCOMING -> {
                    binding.btnMainAction.visibility = View.VISIBLE
                    binding.btnMainAction.text = "Accept"
                    binding.btnMainAction.setOnClickListener { viewModel.acceptAppointment(a.id) }
                    
                    binding.btnSecondaryAction.visibility = View.VISIBLE
                    binding.btnSecondaryAction.text = "Reject"
                    binding.btnSecondaryAction.setOnClickListener { viewModel.rejectAppointment(a.id) }
                }
                Constants.STATUS_ACCEPTED -> {
                    binding.btnMainAction.visibility = View.VISIBLE
                    binding.btnMainAction.text = "Mark Completed"
                    binding.btnMainAction.setOnClickListener { viewModel.completeAppointment(a.id) }
                    
                    binding.btnSecondaryAction.visibility = View.VISIBLE
                    binding.btnSecondaryAction.text = "Cancel"
                    binding.btnSecondaryAction.setOnClickListener { viewModel.cancelAppointment(a.id) }
                }
                Constants.STATUS_COMPLETED -> {
                    binding.btnMainAction.visibility = View.VISIBLE
                    binding.btnMainAction.text = "Write Prescription"
                    binding.btnMainAction.setOnClickListener { /* Logic for doctor to write prescription */ }
                }
            }
        }
    }

    private fun joinCall(a: Appointment) {
        startActivity(Intent(this, VideoCallActivity::class.java).apply {
            putExtra(Constants.EXTRA_RECEIVER_NAME, a.doctorName)
            putExtra(Constants.EXTRA_RECEIVER_IMAGE, a.doctorImageUrl)
        })
    }

    private fun viewPrescription(a: Appointment) {
        if (a.prescriptionId.isNotEmpty()) {
            startActivity(Intent(this, PrescriptionActivity::class.java).putExtra(Constants.EXTRA_PRESCRIPTION_ID, a.prescriptionId))
        } else {
            Snackbar.make(binding.root, "Prescription not available", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun rateDoctor(a: Appointment) {
        startActivity(Intent(this, RateDoctorActivity::class.java).apply {
            putExtra(Constants.EXTRA_DOCTOR_ID, a.doctorId)
            putExtra(Constants.EXTRA_APPOINTMENT_ID, a.id)
            putExtra(Constants.EXTRA_DOCTOR_NAME, a.doctorName)
        })
    }

    private fun showCancelDialog(id: String) {
        AlertDialog.Builder(this)
            .setTitle("Cancel Appointment")
            .setMessage("Are you sure you want to cancel this appointment?")
            .setPositiveButton("Yes, Cancel") { _, _ ->
                viewModel.cancelAppointment(id)
            }
            .setNegativeButton("No", null)
            .show()
    }
}
