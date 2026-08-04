package com.example.doctorappointmentapp.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doctorappointmentapp.adapter.DoctorListAdapter
import com.example.doctorappointmentapp.databinding.ActivityDoctorListBinding
import com.example.doctorappointmentapp.model.Doctor
import com.example.doctorappointmentapp.utils.Constants
import com.example.doctorappointmentapp.utils.Resource
import com.example.doctorappointmentapp.viewmodel.DoctorViewModel
import com.google.firebase.auth.FirebaseAuth

class DoctorListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorListBinding
    private val viewModel: DoctorViewModel by viewModels()
    private val auth by lazy { FirebaseAuth.getInstance() }
    private lateinit var adapter: DoctorListAdapter
    private var allDoctors: List<Doctor> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()
        viewModel.loadDoctors()

        binding.btnBack.setOnClickListener { finish() }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterDoctors(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) = Unit
        })

        binding.btnFilter.setOnClickListener { showFilterSheet() }
    }

    private fun setupRecyclerView() {
        adapter = DoctorListAdapter(
            onDoctorClick = { doctor ->
                startActivity(
                    Intent(this, DoctorDetailsActivity::class.java)
                        .putExtra(Constants.EXTRA_DOCTOR_ID, doctor.id)
                )
            },
            onFavouriteClick = { doctor, add ->
                val uid = auth.currentUser?.uid ?: return@DoctorListAdapter
                viewModel.toggleFavourite(uid, doctor.id, add)
            }
        )
        binding.rvDoctors.layoutManager = LinearLayoutManager(this)
        binding.rvDoctors.adapter = adapter
        binding.rvDoctors.setHasFixedSize(false)
    }

    private fun observeViewModel() {
        viewModel.doctors.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.layoutEmpty.visibility  = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    allDoctors = resource.data
                    // Use static list if Firestore empty (demo)
                    if (allDoctors.isEmpty()) allDoctors = sampleDoctors()
                    adapter.submitList(allDoctors)
                    binding.layoutEmpty.visibility = if (allDoctors.isEmpty()) View.VISIBLE else View.GONE
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    allDoctors = sampleDoctors()
                    adapter.submitList(allDoctors)
                }
            }
        }
    }

    private fun filterDoctors(query: String) {
        val q = query.trim().lowercase()
        val filtered = if (q.isEmpty()) allDoctors
        else allDoctors.filter {
            it.name.lowercase().contains(q) || it.specialty.lowercase().contains(q)
        }
        adapter.submitList(filtered)
        binding.layoutEmpty.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showFilterSheet() {
        // Bottom-sheet filter — placeholder for future expansion
        com.google.android.material.snackbar.Snackbar
            .make(binding.root, "Filter coming soon", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
            .show()
    }

    private fun sampleDoctors() = listOf(
        Doctor("1","James Wilson","Cardiologist",4.8,120,"","Heart specialist",experience="8 years",hospital="City Hospital",fee=1500.0,isOnline=true,isVerified=true),
        Doctor("2","Sarah Ahmed","Dermatologist",4.7,98,"","Skin specialist",experience="5 years",hospital="Skin Care Clinic",fee=1200.0,isOnline=false,isVerified=true),
        Doctor("3","Michael Chen","Neurologist",4.9,200,"","Brain specialist",experience="12 years",hospital="Neuro Center",fee=2000.0,isOnline=true,isVerified=true),
        Doctor("4","Priya Sharma","Pediatrician",4.6,85,"","Child specialist",experience="7 years",hospital="Kids Hospital",fee=1000.0,isOnline=true,isVerified=false),
        Doctor("5","Amina Khalid","Gynecologist",4.5,60,"","Women specialist",experience="10 years",hospital="Women Care",fee=1800.0,isOnline=false,isVerified=true),
    )
}
