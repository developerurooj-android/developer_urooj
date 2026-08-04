package com.example.doctorappointmentapp.activities

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doctorappointmentapp.adapter.MedicineAdapter
import com.example.doctorappointmentapp.databinding.ActivityPrescriptionBinding
import com.example.doctorappointmentapp.utils.Constants
import com.example.doctorappointmentapp.utils.Resource
import com.example.doctorappointmentapp.viewmodel.AppointmentViewModel

class PrescriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrescriptionBinding
    private val viewModel: AppointmentViewModel by viewModels()
    private lateinit var medicineAdapter: MedicineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prescriptionId = intent.getStringExtra(Constants.EXTRA_PRESCRIPTION_ID) ?: ""

        medicineAdapter = MedicineAdapter()
        binding.rvMedicines.layoutManager = LinearLayoutManager(this)
        binding.rvMedicines.adapter = medicineAdapter

        viewModel.prescription.observe(this) { res ->
            when (res) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val p = res.data
                    binding.tvDoctorName.text = "Dr. ${p.doctorName}"
                    binding.tvPatientName.text = p.patientName
                    binding.tvDiagnosis.text   = p.diagnosis.ifEmpty { "—" }
                    binding.tvNotes.text       = p.notes.ifEmpty { "—" }
                    medicineAdapter.submitList(p.medicines)
                }
                is Resource.Error -> binding.progressBar.visibility = View.GONE
            }
        }

        if (prescriptionId.isNotEmpty()) viewModel.loadPrescription(prescriptionId)

        binding.btnBack.setOnClickListener { finish() }
        binding.btnShare.setOnClickListener {
            com.google.android.material.snackbar.Snackbar
                .make(binding.root, "Sharing coming soon", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).show()
        }
    }
}
