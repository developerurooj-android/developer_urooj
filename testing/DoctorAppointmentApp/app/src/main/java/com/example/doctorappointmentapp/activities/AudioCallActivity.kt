package com.example.doctorappointmentapp.activities

import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.databinding.ActivityAudioCallBinding
import com.example.doctorappointmentapp.utils.Constants

class AudioCallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioCallBinding
    private var isMuted   = false
    private var isSpeaker = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name  = intent.getStringExtra(Constants.EXTRA_RECEIVER_NAME)  ?: "Doctor"
        val image = intent.getStringExtra(Constants.EXTRA_RECEIVER_IMAGE) ?: ""

        binding.tvName.text = name
        Glide.with(this).load(image).placeholder(R.drawable.ic_doctor).circleCrop().into(binding.ivDoctor)

        // Start timer
        binding.chronometer.base  = SystemClock.elapsedRealtime()
        binding.chronometer.start()

        binding.btnEndCall.setOnClickListener {
            binding.chronometer.stop()
            finish()
        }

        binding.btnMute.setOnClickListener {
            isMuted = !isMuted
            binding.btnMute.setImageResource(
                if (isMuted) R.drawable.ic_mic_off else R.drawable.ic_mic
            )
        }

        binding.btnSpeaker.setOnClickListener {
            isSpeaker = !isSpeaker
            binding.btnSpeaker.setImageResource(
                if (isSpeaker) R.drawable.ic_speaker_on else R.drawable.ic_speaker
            )
        }
    }
}
