package com.example.doctorappointmentapp.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.doctorappointmentapp.databinding.ActivitySettingsBinding
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val auth by lazy { FirebaseAuth.getInstance() }
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        binding.switchNotifications.isChecked = isGranted
        if (!isGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                     com.google.android.material.snackbar.Snackbar.make(
                        binding.root, "Notifications help you stay updated.", com.google.android.material.snackbar.Snackbar.LENGTH_LONG
                    ).setAction("Allow") {
                        askNotificationPermission()
                    }.show()
                } else {
                     com.google.android.material.snackbar.Snackbar.make(
                        binding.root, "Permission denied. Enable in Settings.", com.google.android.material.snackbar.Snackbar.LENGTH_LONG
                    ).setAction("Settings") {
                        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = android.net.Uri.fromParts("package", packageName, null)
                        }
                        startActivity(intent)
                    }.show()
                }
            }
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupThemeSelection()
        setupNotificationSwitch()
        setupOtherRows()
    }

    private fun setupToolbar() {
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setupThemeSelection() {
        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val currentTheme = prefs.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        when (currentTheme) {
            AppCompatDelegate.MODE_NIGHT_NO -> binding.toggleTheme.check(binding.btnLightTheme.id)
            AppCompatDelegate.MODE_NIGHT_YES -> binding.toggleTheme.check(binding.btnDarkTheme.id)
            else -> binding.toggleTheme.check(binding.btnSystemTheme.id)
        }

        binding.toggleTheme.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val mode = when (checkedId) {
                    binding.btnLightTheme.id -> AppCompatDelegate.MODE_NIGHT_NO
                    binding.btnDarkTheme.id -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
                
                if (AppCompatDelegate.getDefaultNightMode() != mode) {
                    prefs.edit().putInt("theme_mode", mode).apply()
                    AppCompatDelegate.setDefaultNightMode(mode)
                }
            }
        }
    }

    private fun setupNotificationSwitch() {
        // Check current permission status
        val isGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Prior to Android 13, permission is granted at install time
        }
        
        binding.switchNotifications.isChecked = isGranted

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        askNotificationPermission()
                    }
                }
            }
        }
    }

    private fun setupOtherRows() {
        binding.rowProfile.tvTitle.text = "Account Details"
        binding.rowProfile.root.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.rowSecurity.tvTitle.text = "Change Password"
        binding.rowSecurity.root.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.rowAbout.tvTitle.text = "About App"
        binding.rowAbout.root.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("About")
                .setMessage("Doctor Appointment App\nVersion 1.0.0\n\nBuilt with ❤️ using Kotlin + Firebase")
                .setPositiveButton("OK", null)
                .show()
        }

        binding.rowLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout") { _, _ ->
                    auth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
