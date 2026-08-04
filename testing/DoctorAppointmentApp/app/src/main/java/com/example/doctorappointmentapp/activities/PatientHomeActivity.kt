package com.example.doctorappointmentapp.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.databinding.ActivityPatientHomeBinding
import com.example.doctorappointmentapp.fragments.AppointmentsFragment
import com.example.doctorappointmentapp.fragments.HomeFragment
import com.example.doctorappointmentapp.fragments.MessagesFragment
import com.example.doctorappointmentapp.fragments.ProfileFragment

class PatientHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPatientHomeBinding
    private val fragmentManager by lazy { supportFragmentManager }
    private var activeFragment: Fragment? = null

    private val homeFragment by lazy { HomeFragment() }
    private val appointmentsFragment by lazy { AppointmentsFragment() }
    private val messagesFragment by lazy { MessagesFragment() }
    private val profileFragment by lazy { ProfileFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge
        androidx.core.view.WindowCompat.setDecorFitsSystemWindows(window, false)
        
        binding = ActivityPatientHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFragments()
        setupBottomNavigation()
        handleWindowInsets()
    }

    private fun handleWindowInsets() {
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            // Apply bottom margin to account for system navigation bar + floating margin
            val params = binding.bottomNavCard.layoutParams as android.view.ViewGroup.MarginLayoutParams
            params.bottomMargin = systemBars.bottom + resources.getDimensionPixelSize(R.dimen.bottom_nav_margin)
            binding.bottomNavCard.layoutParams = params
            insets
        }
    }

    private fun setupFragments() {
        // Initial setup of fragments using hide/show to preserve state
        val transaction = fragmentManager.beginTransaction()
        
        // Check if fragments already exist (e.g. after rotation) to avoid duplication
        val existingHome = fragmentManager.findFragmentByTag("HOME")

        if (existingHome == null) {
            transaction.add(R.id.nav_host_fragment, profileFragment, "PROFILE").hide(profileFragment)
            transaction.add(R.id.nav_host_fragment, messagesFragment, "CHATS").hide(messagesFragment)
            transaction.add(R.id.nav_host_fragment, appointmentsFragment, "APPOINTMENTS").hide(appointmentsFragment)
            transaction.add(R.id.nav_host_fragment, homeFragment, "HOME")
            activeFragment = homeFragment
        } else {
            // Restore references to existing fragments to avoid recreation
            activeFragment = existingHome
            // Re-assign lazy properties if possible, or just use findFragmentByTag in setupBottomNavigation
        }
        
        transaction.commit()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val targetTag = when (item.itemId) {
                R.id.nav_home -> "HOME"
                R.id.nav_appointments -> "APPOINTMENTS"
                R.id.nav_messages -> "CHATS"
                R.id.nav_profile -> "PROFILE"
                else -> "HOME"
            }
            
            val targetFragment = fragmentManager.findFragmentByTag(targetTag)
            if (targetFragment != null) {
                switchFragment(targetFragment)
                return@setOnItemSelectedListener true
            }
            false
        }
    }

    private fun switchFragment(target: Fragment) {
        if (activeFragment == target) return
        
        fragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .hide(activeFragment!!)
            .show(target)
            .commit()
        
        activeFragment = target
    }

    override fun onBackPressed() {
        if (activeFragment?.tag != "HOME") {
            binding.bottomNavigation.selectedItemId = R.id.nav_home
        } else {
            super.onBackPressed()
        }
    }
}
