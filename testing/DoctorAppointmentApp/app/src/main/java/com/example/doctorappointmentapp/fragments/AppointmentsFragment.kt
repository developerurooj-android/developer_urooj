package com.example.doctorappointmentapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.activities.*
import com.example.doctorappointmentapp.adapter.AppointmentAdapter
import com.example.doctorappointmentapp.databinding.FragmentAppointmentsBinding
import com.example.doctorappointmentapp.model.Appointment
import com.example.doctorappointmentapp.utils.Constants
import com.example.doctorappointmentapp.viewmodel.AppointmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AppointmentsFragment : Fragment() {

    private var _binding: FragmentAppointmentsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: AppointmentViewModel by viewModels()
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db   by lazy { FirebaseFirestore.getInstance() }

    private lateinit var adapter: AppointmentAdapter
    private var allAppointments: List<Appointment> = emptyList()
    private var activeTab = 0
    private var userRole: String = Constants.ROLE_PATIENT

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            binding.appBarLayout.setPadding(0, systemBars.top, 0, 0)
            insets
        }
        
        fetchRoleAndStart()
    }

    private fun fetchRoleAndStart() {
        val uid = auth.currentUser?.uid ?: return
        binding.progressBar.visibility = View.VISIBLE
        db.collection(Constants.USERS_COLLECTION).document(uid).get()
            .addOnSuccessListener { doc ->
                if (_binding == null) return@addOnSuccessListener
                userRole = doc.getString("role") ?: Constants.ROLE_PATIENT
                setupRecyclerView()
                setupTabSelection()
                setupObservers()
                
                if (userRole == Constants.ROLE_DOCTOR) {
                    viewModel.startListeningDoctor(uid)
                } else {
                    viewModel.startListeningPatient(uid)
                }
            }
            .addOnFailureListener {
                if (_binding == null) return@addOnFailureListener
                binding.progressBar.visibility = View.GONE
            }
    }

    override fun onDestroyView() {
        viewModel.stopListening()
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        adapter = AppointmentAdapter(userRole) { appt, action -> handleAction(appt, action) }
        binding.rvAppointments.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAppointments.adapter = adapter
    }

    private fun setupTabSelection() {
        showTab(0)
        binding.tabUpcoming.setOnClickListener  { showTab(0) }
        binding.tabCompleted.setOnClickListener { showTab(1) }
        binding.tabCancelled.setOnClickListener { showTab(2) }
    }

    private fun showTab(index: Int) {
        activeTab = index
        
        binding.tabUpcoming.setBackgroundResource( if (index == 0) R.drawable.tab_indicator_active else R.drawable.tab_indicator_inactive)
        binding.tabCompleted.setBackgroundResource(if (index == 1) R.drawable.tab_indicator_active else R.drawable.tab_indicator_inactive)
        binding.tabCancelled.setBackgroundResource(if (index == 2) R.drawable.tab_indicator_active else R.drawable.tab_indicator_inactive)

        val activeColor   = requireContext().getColor(R.color.md_theme_light_primary)
        val inactiveColor = requireContext().getColor(R.color.text_secondary)
        
        binding.tabUpcoming.apply {
            setTextColor(if (index == 0) activeColor else inactiveColor)
            typeface = if (index == 0) android.graphics.Typeface.create("sans-serif-medium", android.graphics.Typeface.NORMAL)
                       else android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.NORMAL)
        }
        binding.tabCompleted.apply {
            setTextColor(if (index == 1) activeColor else inactiveColor)
            typeface = if (index == 1) android.graphics.Typeface.create("sans-serif-medium", android.graphics.Typeface.NORMAL)
                       else android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.NORMAL)
        }
        binding.tabCancelled.apply {
            setTextColor(if (index == 2) activeColor else inactiveColor)
            typeface = if (index == 2) android.graphics.Typeface.create("sans-serif-medium", android.graphics.Typeface.NORMAL)
                       else android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.NORMAL)
        }
        
        updateFilteredList()
    }

    private fun setupObservers() {
        viewModel.appointments.observe(viewLifecycleOwner) { list ->
            binding.progressBar.visibility = View.GONE
            allAppointments = list
            updateFilteredList()
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun updateFilteredList() {
        val filtered = when (activeTab) {
            0 -> allAppointments.filter { it.status == Constants.STATUS_UPCOMING || it.status == Constants.STATUS_ACCEPTED }
            1 -> allAppointments.filter { it.status == Constants.STATUS_COMPLETED }
            else -> allAppointments.filter { it.status == Constants.STATUS_CANCELLED }
        }
        adapter.submitList(filtered)
        binding.layoutEmpty.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
        binding.rvAppointments.visibility = if (filtered.isNotEmpty()) View.VISIBLE else View.GONE
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
            "details" -> startActivity(Intent(requireContext(), AppointmentDetailsActivity::class.java).putExtra(Constants.EXTRA_APPOINTMENT_ID, appointment.id))
            "pay" -> startActivity(Intent(requireContext(), PaymentActivity::class.java).putExtra(Constants.EXTRA_APPOINTMENT_ID, appointment.id))
            "prescription" -> {
                if (appointment.prescriptionId.isNotEmpty()) {
                    startActivity(Intent(requireContext(), PrescriptionActivity::class.java).putExtra(Constants.EXTRA_PRESCRIPTION_ID, appointment.prescriptionId))
                }
            }
            "rate" -> startActivity(Intent(requireContext(), RateDoctorActivity::class.java).apply {
                putExtra(Constants.EXTRA_DOCTOR_ID, appointment.doctorId)
                putExtra(Constants.EXTRA_APPOINTMENT_ID, appointment.id)
                putExtra(Constants.EXTRA_DOCTOR_NAME, appointment.doctorName)
            })
            "rebook" -> startActivity(Intent(requireContext(), DoctorDetailsActivity::class.java).putExtra(Constants.EXTRA_DOCTOR_ID, appointment.doctorId))
            "video_call" -> startActivity(Intent(requireContext(), VideoCallActivity::class.java).apply {
                putExtra(Constants.EXTRA_RECEIVER_NAME, appointment.doctorName)
                putExtra(Constants.EXTRA_RECEIVER_IMAGE, appointment.doctorImageUrl)
            })
            "audio_call" -> startActivity(Intent(requireContext(), AudioCallActivity::class.java).apply {
                putExtra(Constants.EXTRA_RECEIVER_NAME, appointment.doctorName)
                putExtra(Constants.EXTRA_RECEIVER_IMAGE, appointment.doctorImageUrl)
            })
        }
    }

    private fun showConfirmationDialog(title: String, message: String, onConfirm: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Yes", { _, _ -> onConfirm() })
            .setNegativeButton("No", null)
            .show()
    }

    companion object {
        fun newInstance() = AppointmentsFragment()
    }
}
