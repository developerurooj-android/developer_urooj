package com.example.doctorappointmentapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.adapter.ReviewAdapter
import com.example.doctorappointmentapp.adapter.TimeSlotAdapter
import com.example.doctorappointmentapp.databinding.ActivityDoctorDetailsBinding
import com.example.doctorappointmentapp.model.Appointment
import com.example.doctorappointmentapp.model.TimeSlot
import com.example.doctorappointmentapp.utils.Constants
import com.example.doctorappointmentapp.utils.Resource
import com.example.doctorappointmentapp.viewmodel.AppointmentViewModel
import com.example.doctorappointmentapp.viewmodel.DoctorViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class DoctorDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorDetailsBinding
    private val doctorVM: DoctorViewModel by viewModels()
    private val appointmentVM: AppointmentViewModel by viewModels()
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db   by lazy { FirebaseFirestore.getInstance() }
    private var doctorId = ""
    private var selectedSlot = ""
    private var selectedDate = ""
    private var currentFee = 0.0

    private lateinit var slotAdapter: TimeSlotAdapter
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        doctorId = intent.getStringExtra(Constants.EXTRA_DOCTOR_ID) ?: ""

        setupAdapters()
        observeViewModels()

        doctorVM.loadDoctor(doctorId)
        doctorVM.loadReviews(doctorId)

        binding.btnBack.setOnClickListener { finish() }

        binding.btnBookAppointment.setOnClickListener { bookAppointment() }

        binding.btnFavourite.setOnClickListener {
            val uid = auth.currentUser?.uid ?: return@setOnClickListener
            doctorVM.toggleFavourite(uid, doctorId, true)
            binding.btnFavourite.setImageResource(R.drawable.ic_favourite_filled)
        }

        // Animate button on load
        binding.btnBookAppointment.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.fade_in_up)
        )
    }

    private fun setupAdapters() {
        slotAdapter = TimeSlotAdapter { slot ->
            selectedSlot = slot.time
        }
        binding.rvTimeSlots.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvTimeSlots.adapter = slotAdapter
        slotAdapter.submitList(defaultSlots())

        reviewAdapter = ReviewAdapter()
        binding.rvReviews.layoutManager = LinearLayoutManager(this)
        binding.rvReviews.isNestedScrollingEnabled = false
        binding.rvReviews.adapter = reviewAdapter
    }

    private fun observeViewModels() {
        doctorVM.doctor.observe(this) { res ->
            when (res) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val d = res.data
                    currentFee = d.fee
                    binding.tvName.text          = "Dr. ${d.name}"
                    binding.tvSpecialty.text     = d.specialty
                    binding.tvHospital.text      = d.hospital.ifEmpty { "Private Clinic" }
                    binding.tvExperience.text    = "${d.experience} Experience"
                    binding.tvFee.text           = if (d.fee > 0) "Rs ${d.fee.toInt()}" else "Free"
                    binding.tvRating.text        = d.rating.toString()
                    binding.tvReviewCount.text   = "(${d.reviewsCount} reviews)"
                    binding.tvAbout.text         = d.description.ifEmpty { "No description available." }
                    binding.tvQualifications.text = d.qualifications.ifEmpty { "MBBS, FCPS" }
                    binding.tvLanguages.text     = d.languages.joinToString(", ").ifEmpty { "English, Urdu" }
                    binding.ivOnline.visibility  = if (d.isOnline) View.VISIBLE else View.GONE
                    binding.ivVerified.visibility = if (d.isVerified) View.VISIBLE else View.GONE
                    Glide.with(this).load(d.imageUrl)
                        .placeholder(R.drawable.ic_doctor)
                        .into(binding.ivDoctorImage)
                }
                is Resource.Error -> binding.progressBar.visibility = View.GONE
            }
        }

        doctorVM.reviews.observe(this) { res ->
            if (res is Resource.Success) reviewAdapter.submitList(res.data)
        }

        appointmentVM.bookState.observe(this) { res ->
            when (res) {
                is Resource.Loading -> binding.btnBookAppointment.isEnabled = false
                is Resource.Success -> {
                    binding.btnBookAppointment.isEnabled = true
                    startActivity(
                        Intent(this, PaymentActivity::class.java)
                            .putExtra(Constants.EXTRA_APPOINTMENT_ID, res.data)
                    )
                }
                is Resource.Error   -> {
                    binding.btnBookAppointment.isEnabled = true
                    com.google.android.material.snackbar.Snackbar
                        .make(binding.root, res.message, com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun bookAppointment() {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            com.google.android.material.snackbar.Snackbar
                .make(binding.root, "Please login first", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).show()
            return
        }
        if (selectedSlot.isEmpty()) {
            com.google.android.material.snackbar.Snackbar
                .make(binding.root, "Please select a time slot", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).show()
            return
        }
        selectedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

        db.collection(Constants.USERS_COLLECTION).document(uid).get()
            .addOnSuccessListener { userDoc ->
                val patientName = userDoc.getString("name") ?: "Patient"
                val doctorName  = binding.tvName.text.toString()
                
                // Get doctor image and hospital from current UI state or model
                val imageUrl = doctorVM.doctor.value?.let { if (it is Resource.Success) it.data.imageUrl else "" } ?: ""
                val hospital = binding.tvHospital.text.toString()
                
                val appt = Appointment(
                    patientId       = uid,
                    doctorId        = doctorId,
                    patientName     = patientName,
                    doctorName      = doctorName,
                    doctorImageUrl  = imageUrl,
                    doctorSpecialty = binding.tvSpecialty.text.toString(),
                    hospitalName    = hospital,
                    date            = selectedDate,
                    time            = selectedSlot,
                    fee             = currentFee,
                    status          = Constants.STATUS_UPCOMING,
                    paymentStatus   = Constants.PAYMENT_UNPAID,
                    createdAt       = System.currentTimeMillis()
                )
                appointmentVM.bookAppointment(appt)
            }
    }

    private fun defaultSlots() = listOf(
        TimeSlot("09:00 AM", true), TimeSlot("10:00 AM", true),
        TimeSlot("11:00 AM", false), TimeSlot("02:00 PM", true),
        TimeSlot("03:00 PM", true), TimeSlot("04:00 PM", false),
        TimeSlot("05:00 PM", true)
    )
}
