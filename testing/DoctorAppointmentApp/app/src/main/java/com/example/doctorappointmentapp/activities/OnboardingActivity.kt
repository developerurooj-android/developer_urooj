package com.example.doctorappointmentapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.adapter.OnboardingViewPagerAdapter
import com.example.doctorappointmentapp.databinding.ActivityOnboardingBinding
import com.example.doctorappointmentapp.model.OnboardingItem

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var adapter: OnboardingViewPagerAdapter
    private val dots = mutableListOf<ImageView>()

    private val onboardingItems = listOf(
        OnboardingItem(
            image = R.drawable.ic_onboarding_1,
            title = "Doctor Appointment Booking",
            description = "Book appointments quickly with verified doctors near you."
        ),
        OnboardingItem(
            image = R.drawable.ic_onboarding_2,
            title = "Chat & Video Consultation",
            description = "Consult doctors using secure chat, voice and video calls."
        ),
        OnboardingItem(
            image = R.drawable.ic_onboarding_3,
            title = "Medical Reports & Prescriptions",
            description = "Upload reports and receive digital prescriptions anytime."
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        setupDots(0)

        binding.btnSkip.setOnClickListener { finishOnboarding() }
        binding.btnGetStarted.setOnClickListener { finishOnboarding() }
        binding.btnNext.setOnClickListener {
            val current = binding.viewPager.currentItem
            if (current < onboardingItems.size - 1) {
                binding.viewPager.currentItem = current + 1
            } else {
                finishOnboarding()
            }
        }
    }

    private fun setupViewPager() {
        adapter = OnboardingViewPagerAdapter(onboardingItems)
        binding.viewPager.adapter = adapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setupDots(position)
                val isLast = position == onboardingItems.size - 1
                binding.btnNext.visibility        = if (isLast) View.GONE    else View.VISIBLE
                binding.btnGetStarted.visibility  = if (isLast) View.VISIBLE else View.GONE
                binding.btnSkip.visibility        = if (isLast) View.GONE    else View.VISIBLE
            }
        })
    }

    private fun setupDots(activeIndex: Int) {
        binding.layoutDots.removeAllViews()
        dots.clear()
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(8, 0, 8, 0) }

        for (i in onboardingItems.indices) {
            val dot = ImageView(this).apply {
                layoutParams = params
                setImageDrawable(
                    ContextCompat.getDrawable(
                        this@OnboardingActivity,
                        if (i == activeIndex) R.drawable.onboarding_dot_active
                        else R.drawable.onboarding_dot_inactive
                    )
                )
            }
            dots.add(dot)
            binding.layoutDots.addView(dot)
        }
    }

    private fun finishOnboarding() {
        getSharedPreferences(com.example.doctorappointmentapp.utils.Constants.PREFS_NAME, MODE_PRIVATE)
            .edit().putBoolean(com.example.doctorappointmentapp.utils.Constants.IS_FIRST_TIME, false).apply()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
