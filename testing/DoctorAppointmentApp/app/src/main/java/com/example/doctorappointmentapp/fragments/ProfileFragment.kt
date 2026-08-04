package com.example.doctorappointmentapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.activities.AppointmentsActivity
import com.example.doctorappointmentapp.activities.LoginActivity
import com.example.doctorappointmentapp.activities.MedicalReportsActivity
import com.example.doctorappointmentapp.activities.PaymentHistoryActivity
import com.example.doctorappointmentapp.activities.ProfileActivity
import com.example.doctorappointmentapp.activities.SettingsActivity
import com.example.doctorappointmentapp.databinding.FragmentProfileBinding
import com.example.doctorappointmentapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }
    private var profileListener: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        startProfileListener()
        setupSettingsRows()
        
        binding.btnEdit.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout") { _, _ ->
                    auth.signOut()
                    startActivity(
                        Intent(requireContext(), LoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                    )
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun startProfileListener() {
        val uid = auth.currentUser?.uid ?: return
        profileListener = db.collection(Constants.USERS_COLLECTION).document(uid)
            .addSnapshotListener { snapshot, error ->
                if (_binding == null || error != null) return@addSnapshotListener
                
                val name = snapshot?.getString("name") ?: "User"
                val email = snapshot?.getString("email") ?: ""
                val imageUrl = snapshot?.getString("imageUrl") ?: ""
                
                binding.tvName.text = name
                binding.tvEmail.text = email
                
                if (imageUrl.isNotEmpty()) {
                    Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_user)
                        .circleCrop()
                        .into(binding.ivProfile)
                }
            }
    }

    private fun setupSettingsRows() {
        binding.rowPersonalInfo.apply {
            tvTitle.text = "Personal Information"
            ivIcon.setImageResource(R.drawable.ic_person)
            root.setOnClickListener { startActivity(Intent(requireContext(), ProfileActivity::class.java)) }
        }
        binding.rowAppointments.apply {
            tvTitle.text = "My Appointments"
            ivIcon.setImageResource(R.drawable.ic_calendar)
            root.setOnClickListener { startActivity(Intent(requireContext(), AppointmentsActivity::class.java)) }
        }
        binding.rowMedicalHistory.apply {
            tvTitle.text = "Medical History"
            ivIcon.setImageResource(R.drawable.ic_qualifications)
            root.setOnClickListener { 
                startActivity(Intent(requireContext(), com.example.doctorappointmentapp.activities.MedicalHistoryActivity::class.java))
            }
        }
        binding.rowMedicalReports.apply {
            tvTitle.text = "Medical Reports"
            ivIcon.setImageResource(R.drawable.ic_file_pdf)
            root.setOnClickListener { startActivity(Intent(requireContext(), MedicalReportsActivity::class.java)) }
        }
        binding.rowPayments.apply {
            tvTitle.text = "Payments History"
            ivIcon.setImageResource(R.drawable.ic_payment_card)
            root.setOnClickListener {
                startActivity(Intent(requireContext(), PaymentHistoryActivity::class.java))
            }
        }
        binding.rowSettings.apply {
            tvTitle.text = "Settings"
            ivIcon.setImageResource(R.drawable.ic_settings)
            root.setOnClickListener { startActivity(Intent(requireContext(), SettingsActivity::class.java)) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        profileListener?.remove()
        _binding = null
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
