package com.example.doctorappointmentapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.databinding.ActivitySplashBinding
import com.example.doctorappointmentapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db   by lazy { FirebaseFirestore.getInstance() }
    private val navigationRunnable = Runnable { checkAuthAndNavigate() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settings = getSharedPreferences("settings", MODE_PRIVATE)
        val mode = settings.getInt("theme_mode", androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(mode)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutLogo.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.fade_in)
        )

        binding.layoutLogo.postDelayed(navigationRunnable, 2000)
    }

    override fun onDestroy() {
        binding.layoutLogo.removeCallbacks(navigationRunnable)
        super.onDestroy()
    }

    private fun checkAuthAndNavigate() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            val prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE)
            val onboardingSeen = !prefs.getBoolean(Constants.IS_FIRST_TIME, true)
            if (onboardingSeen) {
                goTo(LoginActivity::class.java)
            } else {
                prefs.edit().putBoolean(Constants.IS_FIRST_TIME, false).apply()
                goTo(OnboardingActivity::class.java)
            }
        } else {
            db.collection(Constants.USERS_COLLECTION).document(currentUser.uid).get()
                .addOnSuccessListener { doc ->
                    val role = doc.getString("role") ?: Constants.ROLE_PATIENT.lowercase()
                    navigateByRole(role)
                }
                .addOnFailureListener {
                    goTo(LoginActivity::class.java)
                }
        }
    }

    private fun navigateByRole(role: String) {
        val normalized = role.lowercase()
        val target = when {
            normalized == Constants.ROLE_DOCTOR.lowercase()  -> DoctorHomeActivity::class.java
            normalized == Constants.ROLE_ADMIN.lowercase()   -> AdminDashboardActivity::class.java
            else                                             -> PatientHomeActivity::class.java
        }
        goTo(target)
    }

    private fun goTo(target: Class<*>) {
        startActivity(Intent(this, target).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
