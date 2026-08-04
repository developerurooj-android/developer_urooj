package com.example.doctorappointmentapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.databinding.ActivityAdminDashboardBinding
import com.example.doctorappointmentapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db   by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupManagementRows()
        loadStats()

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout from Admin Dashboard?")
                .setPositiveButton("Logout") { _, _ ->
                    auth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun setupManagementRows() {
        binding.rowManageDoctors.apply {
            tvTitle.text = "Manage Doctors"
            ivIcon.setImageResource(R.drawable.ic_doctor)
        }
        binding.rowManagePatients.apply {
            tvTitle.text = "Manage Patients"
            ivIcon.setImageResource(R.drawable.ic_person)
        }
        binding.rowVerifyPayments.apply {
            tvTitle.text = "Verify Payments"
            ivIcon.setImageResource(R.drawable.ic_payment_card)
        }
        binding.rowSystemReports.apply {
            tvTitle.text = "System Reports"
            ivIcon.setImageResource(R.drawable.ic_file_pdf)
        }
    }

    private fun loadStats() {
        // Load Doctors Count
        db.collection(Constants.USERS_COLLECTION)
            .whereEqualTo("role", "Doctor")
            .get()
            .addOnSuccessListener { snap ->
                binding.statDoctors.apply {
                    tvValue.text = snap.size().toString()
                    tvLabel.text = "Doctors"
                    ivIcon.setImageResource(R.drawable.ic_doctor)
                }
            }

        // Load Patients Count
        db.collection(Constants.USERS_COLLECTION)
            .whereEqualTo("role", "Patient")
            .get()
            .addOnSuccessListener { snap ->
                binding.statPatients.apply {
                    tvValue.text = snap.size().toString()
                    tvLabel.text = "Patients"
                    ivIcon.setImageResource(R.drawable.ic_person)
                }
            }
            
        // Today's Appointments
        val today = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date())
        db.collection(Constants.APPOINTMENTS_COLLECTION)
            .whereEqualTo("date", today)
            .get()
            .addOnSuccessListener { snap ->
                binding.statTodayAppts.apply {
                    tvValue.text = snap.size().toString()
                    tvLabel.text = "Today Appts"
                    ivIcon.setImageResource(R.drawable.ic_calendar)
                }
            }
            
        // Payments
        db.collection(Constants.PAYMENTS_COLLECTION)
            .get()
            .addOnSuccessListener { snap ->
                binding.statPayments.apply {
                    tvValue.text = snap.size().toString()
                    tvLabel.text = "Transactions"
                    ivIcon.setImageResource(R.drawable.ic_payment_card)
                }
            }
    }
}
