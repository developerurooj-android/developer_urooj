package com.example.doctorappointmentapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.adapter.AppointmentAdapter
import com.example.doctorappointmentapp.databinding.ActivityDoctorHomeBinding
import com.example.doctorappointmentapp.model.Appointment
import com.example.doctorappointmentapp.utils.Constants
import com.example.doctorappointmentapp.viewmodel.AppointmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class DoctorHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorHomeBinding
    private val viewModel: AppointmentViewModel by viewModels()
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db   by lazy { FirebaseFirestore.getInstance() }
    private lateinit var appointmentAdapter: AppointmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        loadDoctorProfile()
        setupObservers()
        setupBottomNavigation()
        handleWindowInsets()

        // Start real-time listener for today's appointments
        auth.currentUser?.uid?.let { viewModel.startListeningDoctor(it) }
    }

    private fun handleWindowInsets() {
        androidx.core.view.WindowCompat.setDecorFitsSystemWindows(window, false)
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            val params = binding.bottomNavCard.layoutParams as android.view.ViewGroup.MarginLayoutParams
            params.bottomMargin = systemBars.bottom + resources.getDimensionPixelSize(R.dimen.bottom_nav_margin)
            binding.bottomNavCard.layoutParams = params
            insets
        }
    }

    override fun onDestroy() {
        viewModel.stopListening()
        super.onDestroy()
    }

    private fun setupAdapter() {
        appointmentAdapter = AppointmentAdapter { appt, action ->
            when (action) {
                "chat" -> startActivity(
                    Intent(this, ChatActivity::class.java)
                        .putExtra(Constants.EXTRA_RECEIVER_ID, appt.patientId)
                        .putExtra(Constants.EXTRA_RECEIVER_NAME, appt.patientName)
                )
                "prescription" -> if (appt.prescriptionId.isNotEmpty()) {
                    startActivity(
                        Intent(this, PrescriptionActivity::class.java)
                            .putExtra(Constants.EXTRA_PRESCRIPTION_ID, appt.prescriptionId)
                    )
                }
            }
        }
        binding.rvRecentPatients.layoutManager = LinearLayoutManager(this)
        binding.rvRecentPatients.adapter = appointmentAdapter
        binding.rvRecentPatients.isNestedScrollingEnabled = false
    }

    private fun setupObservers() {
        viewModel.appointments.observe(this) { list ->
            // Stats update in real-time
            val today = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
                .format(java.util.Date())
            val todayAppts    = list.filter { it.date == today }
            val pendingCount  = list.count { it.status == Constants.STATUS_PENDING }
            val completedCount = list.count { it.status == Constants.STATUS_COMPLETED }
            val totalPatients = list.map { it.patientId }.distinct().size

            binding.statAppointments.apply {
                tvValue.text = todayAppts.size.toString()
                tvLabel.text = "Today"
                ivIcon.setImageResource(R.drawable.ic_calendar)
            }
            binding.statPatients.apply {
                tvValue.text = totalPatients.toString()
                tvLabel.text = "Patients"
                ivIcon.setImageResource(R.drawable.ic_person)
            }
            binding.statCompleted.apply {
                tvValue.text = completedCount.toString()
                tvLabel.text = "Completed"
                ivIcon.setImageResource(R.drawable.ic_check_circle)
            }
            binding.statPending.apply {
                tvValue.text = pendingCount.toString()
                tvLabel.text = "Pending"
                ivIcon.setImageResource(R.drawable.ic_clock)
            }

            appointmentAdapter.submitList(
                list.filter { it.status == Constants.STATUS_PENDING || it.status == Constants.STATUS_CONFIRMED }
            )
        }

        viewModel.loading.observe(this) { loading ->
            // Could show/hide a shimmer if desired
        }
    }

    private fun loadDoctorProfile() {
        val uid = auth.currentUser?.uid ?: return
        db.collection(Constants.USERS_COLLECTION).document(uid)
            .addSnapshotListener { doc, _ ->
                if (doc == null) return@addSnapshotListener
                val name = doc.getString("name") ?: "Doctor"
                val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                val greeting = when {
                    hour < 12 -> "Good Morning"
                    hour < 17 -> "Good Afternoon"
                    else      -> "Good Evening"
                }
                binding.tvGreetingLabel.text = "$greeting,"
                binding.tvGreeting.text = "Dr. $name"
                val imageUrl = doc.getString("imageUrl") ?: ""
                if (imageUrl.isNotEmpty()) {
                    Glide.with(this).load(imageUrl)
                        .placeholder(R.drawable.ic_doctor)
                        .circleCrop()
                        .into(binding.ivDoctorProfile)
                }
            }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_home
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home         -> true
                R.id.nav_appointments -> {
                    startActivity(Intent(this, AppointmentsActivity::class.java))
                    true
                }
                R.id.nav_messages     -> true  // Chat list — future screen
                R.id.nav_profile      -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
