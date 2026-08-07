package com.example.doctorappointmentapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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

    private val doctorList = ArrayList<TopDoctor>()
    private val categoryList = ArrayList<Category>()
    private val healthList = ArrayList<HealthTip>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPatientHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        setupRecyclerViews()

        binding.tvSeeAppointments.setOnClickListener {
            startActivity(Intent(this, AppointmentActivity::class.java))
        }

        loadCategories()
        loadDoctors()
        loadHealthTip()
        loadUpcomingAppointment()
        loadCurrentUser()
        setupBottomNavigation()

        binding.tvSeeAllDoctors.setOnClickListener {
            startActivity(Intent(this, TopDoctorsActivity::class.java))
        }
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
                
                // Temporary seed trigger for demonstration
                binding.tvPatientName.setOnClickListener {
                    DataSeeder.seedAllData { success ->
                        if (success) {
                            Toast.makeText(this, "Firestore Collections Created & Data Seeded!", Toast.LENGTH_LONG).show()
                            loadDoctors()
                            loadHealthTip()
                            loadUpcomingAppointment()
                        } else {
                            Toast.makeText(this, "Seeding Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

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
            .orderBy(
                "rating",
                com.google.firebase.firestore.Query.Direction.DESCENDING
            )
            .limit(10)
            .get()
            .addOnSuccessListener { documents ->

                doctorList.clear()

                for (document in documents) {

                    val doctor = document.toObject(TopDoctor::class.java)

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

    private fun loadHealthTip() {

        db.collection("healthTip")
            .get()
            .addOnSuccessListener { documents ->

                Log.d("Firestore", "Documents found: ${documents.size()}")

                healthList.clear()

                for (document in documents) {

                    Log.d(
                        "Firestore",
                        document.data.toString()
                    )

                    val tip = document.toObject(
                        HealthTip::class.java
                    )

                    healthList.add(tip)
                }

                healthAdapter.notifyDataSetChanged()

            }
            .addOnFailureListener { e ->

                Log.e(
                    "Firestore",
                    "HealthTip Error",
                    e
                )

                Toast.makeText(
                    this,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun loadUpcomingAppointment() {

        val uid = auth.currentUser?.uid ?: return

        db.collection("Appointments")
            .whereEqualTo("patientId", uid)
            .get()
            .addOnSuccessListener { documents ->

                if (!documents.isEmpty) {

                    val appointment = documents.documents[0].toObject(Appointment::class.java)

                    appointment?.let {
                        binding.cardAppointment.visibility = View.VISIBLE
                        binding.tvDoctorName.text = it.doctorName
                        binding.tvDoctorSpecialization.text = it.specialization
                        binding.tvAppointmentDate.text = it.date
                        binding.tvAppointmentTime.text = it.time
                    }

                } else {
                    binding.cardAppointment.visibility = View.GONE
                }
            }
            .addOnFailureListener {
                binding.cardAppointment.visibility = View.GONE
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