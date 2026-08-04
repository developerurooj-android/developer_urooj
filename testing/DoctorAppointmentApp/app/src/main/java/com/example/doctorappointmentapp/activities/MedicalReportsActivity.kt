package com.example.doctorappointmentapp.activities

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doctorappointmentapp.adapter.MedicalReportAdapter
import com.example.doctorappointmentapp.databinding.ActivityMedicalReportsBinding
import com.example.doctorappointmentapp.model.MedicalReport
import com.example.doctorappointmentapp.utils.Resource
import com.example.doctorappointmentapp.viewmodel.MedicalReportViewModel
import com.google.firebase.auth.FirebaseAuth

class MedicalReportsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicalReportsBinding
    private val viewModel: MedicalReportViewModel by viewModels()
    private val auth by lazy { FirebaseAuth.getInstance() }
    private lateinit var adapter: MedicalReportAdapter
    private var pickedUri: Uri? = null

    private val filePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            pickedUri = it
            val uid = auth.currentUser?.uid ?: return@let
            val ext = contentResolver.getType(it)?.substringAfter("/") ?: "pdf"
            val report = MedicalReport(
                title    = "Report ${System.currentTimeMillis()}",
                fileType = ext,
                fileSize = ""
            )
            viewModel.uploadReport(uid, it, report)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicalReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MedicalReportAdapter(
            onDelete = { report -> viewModel.deleteReport(report.id, report.fileUrl) }
        )
        binding.rvReports.layoutManager = LinearLayoutManager(this)
        binding.rvReports.adapter = adapter

        observeViewModel()
        auth.currentUser?.uid?.let { viewModel.loadReports(it) }

        binding.btnBack.setOnClickListener { finish() }
        binding.btnUpload.setOnClickListener { filePicker.launch("*/*") }
    }

    private fun observeViewModel() {
        viewModel.reports.observe(this) { res ->
            when (res) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(res.data)
                    binding.layoutEmpty.visibility = if (res.data.isEmpty()) View.VISIBLE else View.GONE
                }
                is Resource.Error   -> binding.progressBar.visibility = View.GONE
            }
        }
        viewModel.uploadState.observe(this) { res ->
            if (res is Resource.Success) {
                auth.currentUser?.uid?.let { viewModel.loadReports(it) }
            }
        }
        viewModel.deleteState.observe(this) { res ->
            if (res is Resource.Success) {
                auth.currentUser?.uid?.let { viewModel.loadReports(it) }
            }
        }
    }
}
