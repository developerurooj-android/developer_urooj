package com.example.doctorappointmentapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object DataSeeder {

    fun seedAllData(onComplete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid ?: return
        val batch = db.batch()

        // 1. Seed Doctors
        val doctors = listOf(
            TopDoctor(doctorName = "Dr. Sarah Ahmed", specialization = "Cardiologist", rating = "4.9", experience = "12 Years exp", profileImage = "https://img.freepik.com/free-photo/woman-doctor-wearing-lab-coat-with-stethoscope-isolated_1303-29791.jpg"),
            TopDoctor(doctorName = "Dr. Ali Khan", specialization = "Dermatologist", rating = "4.7", experience = "8 Years exp", profileImage = "https://img.freepik.com/free-photo/doctor-offering-medical-tele-consultation_23-2149329007.jpg"),
            TopDoctor(doctorName = "Dr. Maria Qureshi", specialization = "Pediatrician", rating = "4.8", experience = "10 Years exp", profileImage = "https://img.freepik.com/free-photo/smiling-female-doctor-white-coat-standing-with-arms-crossed-hospital-office_231208-12966.jpg")
        )

        doctors.forEach { doctor ->
            val docRef = db.collection("doctors").document()
            batch.set(docRef, doctor.copy(doctorId = docRef.id))
        }

        // 2. Seed Health Tips
        val healthTips = listOf(
            HealthTipModel(title = "Drink Water", image = "https://cdn-icons-png.flaticon.com/512/3105/3105807.png"),
            HealthTipModel(title = "Morning Walk", image = "https://cdn-icons-png.flaticon.com/512/2871/2871618.png"),
            HealthTipModel(title = "Healthy Sleep", image = "https://cdn-icons-png.flaticon.com/512/3094/3094838.png")
        )

        healthTips.forEach { tip ->
            val tipRef = db.collection("healthTip").document()
            batch.set(tipRef, tip.copy(id = tipRef.id))
        }

        // 3. Seed Appointments (Linking to current user)
        val appointments = listOf(
            Appointment(
                doctorName = "Dr. Sarah Ahmed",
                specialization = "Cardiologist",
                date = "20 Oct 2023",
                time = "10:30 AM",
                status = "Upcoming",
                consultationFee = 2500.0,
                patientId = uid,
                profileImage = "https://img.freepik.com/free-photo/woman-doctor-wearing-lab-coat-with-stethoscope-isolated_1303-29791.jpg"
            )
        )

        appointments.forEach { appt ->
            // Use specific ID A001 as shown in your Firestore Console link
            val apptRef = db.collection("Appointments").document("A001")
            batch.set(apptRef, appt.copy(appointmentId = "A001"))
        }

        batch.commit()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}
