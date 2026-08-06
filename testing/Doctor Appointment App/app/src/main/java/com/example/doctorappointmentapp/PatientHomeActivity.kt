package com.example.doctorappointmentapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doctorappointmentapp.databinding.ActivityPatientHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PatientHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPatientHomeBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var doctorAdapter: DoctorAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var healthAdapter: HealthTipAdapter

    private val doctorList = ArrayList<Doctor>()
    private val categoryList = ArrayList<Category>()
    private val healthList = ArrayList<HealthTip>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPatientHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        setupRecyclerViews()

        loadCategories()

        loadDoctors()

        loadHealthTips()

        loadUpcomingAppointment()

        loadCurrentUser()

        setupBottomNavigation()
    }

    private fun setupRecyclerViews() {

        // Categories
        categoryAdapter = CategoryAdapter(categoryList)

        binding.rvCategories.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.rvCategories.adapter = categoryAdapter


        // Doctors
        doctorAdapter = DoctorAdapter(doctorList)

        binding.rvDoctors.layoutManager =
            LinearLayoutManager(this)

        binding.rvDoctors.adapter = doctorAdapter


        // Health Tips
        healthAdapter = HealthTipAdapter(healthList)

        binding.rvHealthTips.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.rvHealthTips.adapter = healthAdapter
    }

    private fun loadCurrentUser() {

        val uid = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {

                val name = it.getString("name") ?: "Patient"

                binding.tvPatientName.text = name

            }
    }

    private fun loadCategories() {

        categoryList.clear()

        categoryList.add(
            Category("General", R.drawable.ic_general)
        )

        categoryList.add(
            Category("Dentist", R.drawable.ic_dentist)
        )

        categoryList.add(
            Category("Cardiology", R.drawable.ic_cardiology)
        )

        categoryList.add(
            Category("Neurology", R.drawable.ic_neurology)
        )

        categoryList.add(
            Category("Orthopedic", R.drawable.ic_bone)
        )

        categoryList.add(
            Category("Eye", R.drawable.ic_eye)
        )

        categoryList.add(
            Category("Pediatrics", R.drawable.ic_child)
        )

        categoryList.add(
            Category("Gynecology", R.drawable.ic_woman)
        )

        categoryAdapter.notifyDataSetChanged()
    }

    private fun loadDoctors() {

        db.collection("doctors")
            .get()
            .addOnSuccessListener { documents ->

                doctorList.clear()

                for (document in documents) {

                    val doctor = document.toObject(Doctor::class.java)

                    doctorList.add(doctor)
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

    private fun loadHealthTips() {

        db.collection("healthTips")
            .get()
            .addOnSuccessListener { documents ->

                healthList.clear()

                for (document in documents) {
                    val tip = document.toObject(HealthTip::class.java)
                    healthList.add(tip)
                }

                healthAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load health tips", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadUpcomingAppointment() {
        val uid = auth.currentUser?.uid ?: return

        db.collection("appointments")
            .whereEqualTo("patientId", uid)
            .whereEqualTo("status", "Confirmed")
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val appointment = documents.documents[0].toObject(Appointment::class.java)
                    appointment?.let {
                        binding.tvDoctorName.text = it.doctorName
                        binding.tvDoctorSpecialization.text = it.specialization
                        binding.tvAppointmentDate.text = it.date
                        binding.tvAppointmentTime.text = it.time
                        // Assuming rating is not in Appointment model based on previous read, 
                        // setting default or hiding if needed.
                    }
                }
            }
    }

    private fun setupBottomNavigation() {

        binding.bottomNavigation.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.nav_home -> {
                    true
                }

                R.id.nav_appointments -> {

                    startActivity(
                        Intent(
                            this,
                            AppointmentActivity::class.java
                        )
                    )

                    true
                }

                R.id.nav_chat -> {

                    startActivity(
                        Intent(
                            this,
                            ChatActivity::class.java
                        )
                    )

                    true
                }

                R.id.nav_profile -> {

                    startActivity(
                        Intent(
                            this,
                            ProfileActivity::class.java
                        )
                    )

                    true
                }

                else -> false
            }
        }
    }
}