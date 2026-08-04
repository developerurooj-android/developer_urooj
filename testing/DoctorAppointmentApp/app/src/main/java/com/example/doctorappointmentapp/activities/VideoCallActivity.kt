package com.example.doctorappointmentapp.activities

import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.databinding.ActivityVideoCallBinding
import com.example.doctorappointmentapp.utils.Constants

class VideoCallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoCallBinding
    private var isMuted   = false
    private var isCamOff  = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name  = intent.getStringExtra(Constants.EXTRA_RECEIVER_NAME)  ?: "Doctor"
        val image = intent.getStringExtra(Constants.EXTRA_RECEIVER_IMAGE) ?: ""

        binding.tvName.text = name
        Glide.with(this).load(image).placeholder(R.drawable.ic_doctor).into(binding.ivRemoteVideo)

        binding.chronometer.base  = SystemClock.elapsedRealtime()
        binding.chronometer.start()

        binding.btnEndCall.setOnClickListener { binding.chronometer.stop(); finish() }

        binding.btnMute.setOnClickListener {
            isMuted = !isMuted
            binding.btnMute.setImageResource(if (isMuted) R.drawable.ic_mic_off else R.drawable.ic_mic)
        }

        binding.btnCamera.setOnClickListener {
            isCamOff = !isCamOff
            binding.btnCamera.setImageResource(
                if (isCamOff) R.drawable.ic_cam_off else R.drawable.ic_cam
            )
        }

        binding.btnSwitchCamera.setOnClickListener {
            com.google.android.material.snackbar.Snackbar
                .make(binding.root, "Camera switched", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).show()
        }
    }
}
