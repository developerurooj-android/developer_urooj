package com.example.doctorappointmentapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        
        Handler(Looper.getMainLooper()).postDelayed({
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                // User is logged in, check role
                val uid = user.uid
                val db = FirebaseFirestore.getInstance()
                
                db.collection("users").document(uid).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val role = document.getString("role")
                            if (role == "Patient") {
                                startActivity(Intent(this, PatientHomeActivity::class.java))
                            } else {
                                // Default for Doctor or others if implemented
                                startActivity(Intent(this, PatientHomeActivity::class.java))
                            }
                        } else {
                            // If not in users, check doctors
                            db.collection("doctors").document(uid).get()
                                .addOnSuccessListener { doc ->
                                    startActivity(Intent(this, PatientHomeActivity::class.java))
                                }
                                .addOnFailureListener {
                                    startActivity(Intent(this, LoginActivity::class.java))
                                }
                        }
                        finish()
                    }
                    .addOnFailureListener {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
            } else {
                // Not logged in, go to onboarding
                startActivity(Intent(this, OnboardingActivity::class.java))
                finish()
            }
        }, 3000)
    }
}
