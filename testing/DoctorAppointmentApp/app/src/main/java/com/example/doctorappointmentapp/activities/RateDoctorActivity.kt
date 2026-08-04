package com.example.doctorappointmentapp.activities

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.doctorappointmentapp.databinding.ActivityRateDoctorBinding
import com.example.doctorappointmentapp.model.Review
import com.example.doctorappointmentapp.utils.Constants
import com.example.doctorappointmentapp.utils.Resource
import com.example.doctorappointmentapp.viewmodel.DoctorViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RateDoctorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRateDoctorBinding
    private val viewModel: DoctorViewModel by viewModels()
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db   by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRateDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val doctorId   = intent.getStringExtra(Constants.EXTRA_DOCTOR_ID) ?: ""
        val doctorName = intent.getStringExtra(Constants.EXTRA_DOCTOR_NAME) ?: "Doctor"

        binding.tvDoctorName.text = "Rate Dr. $doctorName"

        viewModel.reviewSubmit.observe(this) { res ->
            when (res) {
                is Resource.Loading -> { binding.btnSubmit.isEnabled = false; binding.progressBar.visibility = View.VISIBLE }
                is Resource.Success -> {
                    binding.btnSubmit.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                    com.google.android.material.snackbar.Snackbar
                        .make(binding.root, "Review submitted!", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).show()
                    binding.root.postDelayed({ finish() }, 1200)
                }
                is Resource.Error -> {
                    binding.btnSubmit.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                    com.google.android.material.snackbar.Snackbar
                        .make(binding.root, res.message, com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnBack.setOnClickListener { finish() }

        binding.btnSubmit.setOnClickListener {
            val rating  = binding.ratingBar.rating
            val comment = binding.etReview.text?.toString()?.trim() ?: ""
            if (rating == 0f) {
                com.google.android.material.snackbar.Snackbar
                    .make(binding.root, "Please select a rating", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val uid = auth.currentUser?.uid ?: return@setOnClickListener
            db.collection(Constants.USERS_COLLECTION).document(uid).get()
                .addOnSuccessListener { doc ->
                    val review = Review(
                        doctorId    = doctorId,
                        patientId   = uid,
                        patientName = doc.getString("name") ?: "Anonymous",
                        rating      = rating,
                        comment     = comment
                    )
                    viewModel.submitReview(review)
                }
        }
    }
}
