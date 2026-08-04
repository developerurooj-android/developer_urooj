package com.example.doctorappointmentapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.databinding.ActivityMainBinding
import com.example.doctorappointmentapp.fragments.AppointmentsFragment
import com.example.doctorappointmentapp.fragments.HomeFragment
import com.example.doctorappointmentapp.fragments.MessagesFragment
import com.example.doctorappointmentapp.fragments.ProfileFragment

/**
 * Legacy host activity — kept for backward compatibility.
 * New role-based flow uses PatientHomeActivity / DoctorHomeActivity / AdminDashboardActivity.
 */
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val homeFragment        by lazy { HomeFragment.newInstance() }
    private val appointmentsFragment by lazy { AppointmentsFragment.newInstance() }
    private val messagesFragment    by lazy { MessagesFragment.newInstance() }
    private val profileFragment     by lazy { ProfileFragment.newInstance() }
    private var activeFragment: Fragment = homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            binding.bottomNavigation.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, homeFragment, "home")
                .commit()
            activeFragment = homeFragment
            binding.bottomNavigation.selectedItemId = R.id.nav_home
        }

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home         -> switchFragment(homeFragment, "home")
                R.id.nav_appointments -> switchFragment(appointmentsFragment, "appointments")
                R.id.nav_messages     -> switchFragment(messagesFragment, "messages")
                R.id.nav_profile      -> switchFragment(profileFragment, "profile")
                else -> false
            }
        }
        binding.bottomNavigation.setOnItemReselectedListener { /* no-op */ }
    }

    private fun switchFragment(target: Fragment, tag: String): Boolean {
        if (target === activeFragment) return true
        val ft = supportFragmentManager.beginTransaction()
        ft.setCustomAnimations(
            androidx.appcompat.R.anim.abc_fade_in,
            androidx.appcompat.R.anim.abc_fade_out,
            androidx.appcompat.R.anim.abc_fade_in,
            androidx.appcompat.R.anim.abc_fade_out
        )
        ft.hide(activeFragment)
        if (!target.isAdded) ft.add(R.id.fragmentContainer, target, tag)
        else ft.show(target)
        ft.commit()
        activeFragment = target
        return true
    }
}
