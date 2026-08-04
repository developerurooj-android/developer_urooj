package com.example.doctorappointmentapp.activities

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doctorappointmentapp.adapter.AppointmentAdapter
import com.example.doctorappointmentapp.databinding.ActivityMedicalHistoryBinding
import com.example.doctorappointmentapp.utils.Constants
import com.example.doctorappointmentapp.viewmodel.AppointmentViewModel
import com.google.firebase.auth.FirebaseAuth

class MedicalHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicalHistoryBinding
    private val viewModel: AppointmentViewModel by viewModels()
    private val auth by lazy { FirebaseAuth.getInstance() }
    private lateinit var adapter: AppointmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicalHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        setupRecyclerView()
        setupObservers()

        auth.currentUser?.uid?.let { viewModel.startListeningPatient(it) }
    }

    private fun setupRecyclerView() {
        adapter = AppointmentAdapter { appointment, action ->
            // Handle details or prescription view
            if (action == "prescription" && appointment.prescriptionId.isNotEmpty()) {
                val intent = android.content.Intent(this, PrescriptionActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRESCRIPTION_ID, appointment.prescriptionId)
                startActivity(intent)
            }
        }
        binding.rvMedicalHistory.layoutManager = LinearLayoutManager(this)
        binding.rvMedicalHistory.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.appointments.observe(this) { list ->
            val completed = list.filter { it.status == Constants.STATUS_COMPLETED }
            adapter.submitList(completed)
            
            binding.layoutEmpty.visibility = if (completed.isEmpty()) View.VISIBLE else View.GONE
            binding.rvMedicalHistory.visibility = if (completed.isNotEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.loading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroy() {
        viewModel.stopListening()
        super.onDestroy()
    }
}
