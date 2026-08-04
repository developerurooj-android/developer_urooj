package com.example.doctorappointmentapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.doctorappointmentapp.databinding.ActivityOnboardingBinding
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var adapter: OnboardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val onboardingItems = listOf(

            OnboardingItem(
                R.drawable.onboarding_1,
                "Find Trusted Doctors",
                "Search and connect with experienced doctors near you."
            ),

            OnboardingItem(
                R.drawable.onboarding_2,
                "Book Appointments",
                "Choose your preferred doctor and schedule appointments easily."
            ),

            OnboardingItem(
                R.drawable.onboardin_3,
                "Online Consultation",
                "Talk to doctors through secure video and audio calls."
            )
        )

        adapter = OnboardingAdapter(onboardingItems)

        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabIndicator, binding.viewPager) { _, _ ->
        }.attach()

        binding.btnSkip.setOnClickListener {
            openMainScreen()
        }

        binding.btnNext.setOnClickListener {

            if (binding.viewPager.currentItem < onboardingItems.size - 1) {

                binding.viewPager.currentItem += 1

            } else {

                openMainScreen()
            }
        }

        binding.btnGetStarted.setOnClickListener {
            openMainScreen()
        }

        binding.viewPager.registerOnPageChangeCallback(
            object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    if (position == onboardingItems.size - 1) {

                        binding.btnNext.visibility = android.view.View.GONE
                        binding.btnGetStarted.visibility = android.view.View.VISIBLE

                    } else {

                        binding.btnNext.visibility = android.view.View.VISIBLE
                        binding.btnGetStarted.visibility = android.view.View.GONE
                    }
                }
            }
        )
    }

    private fun openMainScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}