package com.example.doctorappointmentapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.doctorappointmentapp.databinding.ActivityTopDoctorsBinding
import com.google.firebase.firestore.FirebaseFirestore

class TopDoctorsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTopDoctorsBinding

    private lateinit var doctorAdapter: DoctorAdapter

    private lateinit var db: FirebaseFirestore

    private val doctorList = ArrayList<TopDoctor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTopDoctorsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        setupRecyclerView()
        loadDoctors()
        setupClicks()
        setupBottomNavigation()
    }

    private fun setupRecyclerView() {

        doctorAdapter = DoctorAdapter(doctorList)

        binding.rvDoctors.layoutManager =
            GridLayoutManager(this, 2)

        binding.rvDoctors.adapter = doctorAdapter
    }

    private fun loadDoctors() {

        db.collection("doctors")
            .get()
            .addOnSuccessListener { documents ->

                doctorList.clear()

                for (document in documents) {

                    val doctor = document.toObject(TopDoctor::class.java)

                    if (doctor != null) {
                        doctorList.add(doctor)
                    }
                }

                doctorAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Failed to load doctors",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun setupClicks() {

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupBottomNavigation() {

        binding.bottomNavigation.selectedItemId = R.id.nav_home

        binding.bottomNavigation.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.nav_home -> {
                    startActivity(
                        Intent(this, PatientHomeActivity::class.java)
                    )
                    finish()
                    true
                }

                R.id.nav_appointments -> {
                    startActivity(
                        Intent(this, AppointmentActivity::class.java)
                    )
                    finish()
                    true
                }

                R.id.nav_chat -> {
                    startActivity(
                        Intent(this, ChatActivity::class.java)
                    )
                    finish()
                    true
                }

                R.id.nav_profile -> {
                    startActivity(
                        Intent(this, ProfileActivity::class.java)
                    )
                    finish()
                    true
                }

                else -> false
            }
        }
    }
}