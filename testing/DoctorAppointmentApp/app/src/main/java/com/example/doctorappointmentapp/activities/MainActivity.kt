package com.example.doctorappointmentapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.doctorappointmentapp.utils.Constants

/**
 * Entry-point redirect activity (unused once SplashActivity is the launcher).
 * Kept to avoid any manifest-related issues during build.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Simply delegate to SplashActivity on first open
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }
}
