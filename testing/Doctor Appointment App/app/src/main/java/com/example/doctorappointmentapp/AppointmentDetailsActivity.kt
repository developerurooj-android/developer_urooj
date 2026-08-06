package com.example.doctorappointmentapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.doctorappointmentapp.databinding.ActivityAppointmentDetailsBinding

class AppointmentDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAppointmentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvDoctorName.text = intent.getStringExtra("doctorName")
        binding.tvSpecialization.text = intent.getStringExtra("specialization")
        binding.tvDate.text = intent.getStringExtra("date")
        binding.tvTime.text = intent.getStringExtra("time")
        binding.tvStatus.text = intent.getStringExtra("status")
        binding.tvFee.text = "Rs. ${intent.getDoubleExtra("fee", 0.0)}"

        binding.btnBack.setOnClickListener {
            finish()
        }

        Glide.with(this)
            .load(intent.getStringExtra("profileImage"))
            .placeholder(R.drawable.doctor_placeholder)
            .into(binding.imgDoctor)
    }
}