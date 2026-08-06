package com.example.doctorappointmentapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doctorappointmentapp.databinding.ActivityAppointmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AppointmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentBinding

    private lateinit var adapter: AppointmentAdapter

    private lateinit var appointmentList: ArrayList<Appointment>

    private lateinit var db: FirebaseFirestore

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        auth = FirebaseAuth.getInstance()

        appointmentList = ArrayList()

        adapter = AppointmentAdapter(appointmentList)

        binding.rvAppointments.layoutManager =
            LinearLayoutManager(this)

        binding.rvAppointments.adapter = adapter

        loadAppointments()
    }

    private fun loadAppointments() {

        val uid = auth.currentUser?.uid ?: return

        db.collection("appointments")
            .whereEqualTo("patientId", uid)
            .get()
            .addOnSuccessListener { documents ->

                appointmentList.clear()

                for (document in documents) {

                    val appointment =
                        document.toObject(Appointment::class.java)

                    appointmentList.add(appointment)
                }

                adapter.notifyDataSetChanged()
            }
    }
}