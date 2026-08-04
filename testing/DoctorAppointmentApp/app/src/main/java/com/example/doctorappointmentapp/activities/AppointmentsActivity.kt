package com.example.doctorappointmentapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.adapter.AppointmentAdapter
import com.example.doctorappointmentapp.databinding.ActivityAppointmentsBinding
import com.example.doctorappointmentapp.model.Appointment
import com.example.doctorappointmentapp.utils.Constants
import com.example.doctorappointmentapp.viewmodel.AppointmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AppointmentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentsBinding
    private val viewModel: AppointmentViewModel by viewModels()
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }

    private var userRole: String = Constants.ROLE_PATIENT
    private var allAppts: List<Appointment> = emptyList()
    private var activeTab = 0

    private lateinit var upcomingAdapter: AppointmentAdapter
    private lateinit var completedAdapter: AppointmentAdapter
    private lateinit var cancelledAdapter: AppointmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        
        fetchUserRoleAndStart()
    }

    private fun fetchUserRoleAndStart() {
        val uid = auth.currentUser?.uid ?: return
        binding.progressBar.visibility = View.VISIBLE
        db.collection(Constants.USERS_COLLECTION).document(uid).get()
            .addOnSuccessListener { doc ->
                userRole = doc.getString("role") ?: Constants.ROLE_PATIENT
                setupAdapters()
                setupTabSelection()
                setupObservers()
                
                if (userRole == Constants.ROLE_DOCTOR) {
                    viewModel.startListeningDoctor(uid)
                } else {
                    viewModel.startListeningPatient(uid)
                }
            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
            }
    }

    private fun setupAdapters() {
        val handler: (Appointment, String) -> Unit = { appt, action -> handleAction(appt, action) }
        upcomingAdapter  = AppointmentAdapter(userRole, handler)
        completedAdapter = AppointmentAdapter(userRole, handler)
        cancelledAdapter = AppointmentAdapter(userRole, handler)

        binding.rvUpcoming.layoutManager  = LinearLayoutManager(this)
        binding.rvCompleted.layoutManager = LinearLayoutManager(this)
        binding.rvCancelled.layoutManager = LinearLayoutManager(this)
        
        binding.rvUpcoming.adapter  = upcomingAdapter
        binding.rvCompleted.adapter = completedAdapter
        binding.rvCancelled.adapter = cancelledAdapter
    }

    private fun setupTabSelection() {
        showTab(0)
        binding.tabUpcoming.setOnClickListener  { showTab(0) }
        binding.tabCompleted.setOnClickListener { showTab(1) }
        binding.tabCancelled.setOnClickListener { showTab(2) }
    }

    private fun showTab(index: Int) {
        activeTab = index
        binding.rvUpcoming.visibility  = if (index == 0) View.VISIBLE else View.GONE
        binding.rvCompleted.visibility = if (index == 1) View.VISIBLE else View.GONE
        binding.rvCancelled.visibility = if (index == 2) View.VISIBLE else View.GONE

        // UI for tabs
        updateTabUI(index)
        updateEmptyState()
    }

    private fun updateTabUI(index: Int) {
        val activeBg = R.drawable.bg_slot_selected
        val inactiveBg = 0
        
        binding.tabUpcoming.setBackgroundResource( if (index == 0) activeBg else inactiveBg)
        binding.tabCompleted.setBackgroundResource(if (index == 1) activeBg else inactiveBg)
        binding.tabCancelled.setBackgroundResource(if (index == 2) activeBg else inactiveBg)

        val activeColor = getColor(R.color.white)
        val inactiveColor = getColor(R.color.text_secondary)

        // Find text views inside linear layout tabs
        fun setTabText(tab: View, isActive: Boolean) {
            val tv = (tab as? android.view.ViewGroup)?.getChildAt(0) as? TextView
            tv?.setTextColor(if (isActive) activeColor else inactiveColor)
            tv?.typeface = if (isActive) android.graphics.Typeface.DEFAULT_BOLD else android.graphics.Typeface.DEFAULT
        }

        setTabText(binding.tabUpcoming, index == 0)
        setTabText(binding.tabCompleted, index == 1)
        setTabText(binding.tabCancelled, index == 2)
        
        val subtitle = when(index) {
            0 -> getString(R.string.appt_upcoming_subtitle)
            1 -> "View your past medical consultations"
            else -> "Cancelled or missed appointments"
        }
        binding.tvTabSubtitle.text = subtitle
    }

    private fun setupObservers() {
        viewModel.appointments.observe(this) { list ->
            binding.progressBar.visibility = View.GONE
            allAppts = list
            
            val upcoming = list.filter { it.status == Constants.STATUS_UPCOMING || it.status == Constants.STATUS_ACCEPTED }
            val completed = list.filter { it.status == Constants.STATUS_COMPLETED }
            val cancelled = list.filter { it.status == Constants.STATUS_CANCELLED }

            upcomingAdapter.submitList(upcoming)
            completedAdapter.submitList(completed)
            cancelledAdapter.submitList(cancelled)

            // Update Counts
            binding.tvUpcomingCount.text = upcoming.size.toString()
            binding.tvCompletedCount.text = completed.size.toString()
            binding.tvCancelledCount.text = cancelled.size.toString()
            
            val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
            val todayCount = upcoming.count { it.date == today }
            binding.tvTodayCount.text = "$todayCount Today"

            updateEmptyState()
        }

        viewModel.loading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun updateEmptyState() {
        val relevant = when (activeTab) {
            0    -> allAppts.filter { it.status == Constants.STATUS_UPCOMING || it.status == Constants.STATUS_ACCEPTED }
            1    -> allAppts.filter { it.status == Constants.STATUS_COMPLETED }
            else -> allAppts.filter { it.status == Constants.STATUS_CANCELLED }
        }
        binding.layoutEmpty.visibility = if (relevant.isEmpty()) View.VISIBLE else View.GONE
        binding.btnEmptyBook.visibility = if (activeTab == 0 && userRole == Constants.ROLE_PATIENT) View.VISIBLE else View.GONE
        
        if (relevant.isEmpty()) {
            binding.tvEmptyTitle.text = when(activeTab) {
                0 -> "No Upcoming Appointments"
                1 -> "No Completed Appointments"
                else -> "No Cancelled Appointments"
            }
        }
    }

    private fun handleAction(appointment: Appointment, action: String) {
        when (action) {
            "cancel" -> showConfirmationDialog("Cancel Appointment", "Are you sure you want to cancel this appointment?") {
                viewModel.cancelAppointment(appointment.id)
            }
            "accept" -> viewModel.acceptAppointment(appointment.id)
            "reject" -> showConfirmationDialog("Reject Appointment", "Are you sure you want to reject this appointment?") {
                viewModel.rejectAppointment(appointment.id)
            }
            "complete" -> showConfirmationDialog("Complete Appointment", "Mark this appointment as completed?") {
                viewModel.completeAppointment(appointment.id)
            }
            "details" -> startActivity(Intent(this, AppointmentDetailsActivity::class.java).putExtra(Constants.EXTRA_APPOINTMENT_ID, appointment.id))
            "pay" -> startActivity(Intent(this, PaymentActivity::class.java).putExtra(Constants.EXTRA_APPOINTMENT_ID, appointment.id))
            "prescription" -> {
                if (appointment.prescriptionId.isNotEmpty()) {
                    startActivity(Intent(this, PrescriptionActivity::class.java).putExtra(Constants.EXTRA_PRESCRIPTION_ID, appointment.prescriptionId))
                }
            }
            "rate" -> startActivity(Intent(this, RateDoctorActivity::class.java).apply {
                putExtra(Constants.EXTRA_DOCTOR_ID, appointment.doctorId)
                putExtra(Constants.EXTRA_APPOINTMENT_ID, appointment.id)
                putExtra(Constants.EXTRA_DOCTOR_NAME, appointment.doctorName)
            })
            "rebook" -> startActivity(Intent(this, DoctorDetailsActivity::class.java).putExtra(Constants.EXTRA_DOCTOR_ID, appointment.doctorId))
            "video_call" -> startActivity(Intent(this, VideoCallActivity::class.java).apply {
                putExtra(Constants.EXTRA_RECEIVER_NAME, appointment.doctorName)
                putExtra(Constants.EXTRA_RECEIVER_IMAGE, appointment.doctorImageUrl)
            })
            "audio_call" -> startActivity(Intent(this, AudioCallActivity::class.java).apply {
                putExtra(Constants.EXTRA_RECEIVER_NAME, appointment.doctorName)
                putExtra(Constants.EXTRA_RECEIVER_IMAGE, appointment.doctorImageUrl)
            })
        }
    }

    private fun showConfirmationDialog(title: String, message: String, onConfirm: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Yes", { _, _ -> onConfirm() })
            .setNegativeButton("No", null)
            .show()
    }

    override fun onDestroy() {
        viewModel.stopListening()
        super.onDestroy()
    }
}
