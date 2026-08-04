package com.example.doctorappointmentapp.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.databinding.ActivityForgotPasswordBinding
import com.example.doctorappointmentapp.utils.Resource
import com.example.doctorappointmentapp.utils.gone
import com.example.doctorappointmentapp.utils.snackbar
import com.example.doctorappointmentapp.utils.visible
import com.example.doctorappointmentapp.viewmodel.AuthViewModel

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel: AuthViewModel by viewModels()
    private val finishRunnable = Runnable { finish() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()

        binding.btnBack.setOnClickListener { finish() }
        binding.btnBackToLogin.setOnClickListener { finish() }
        binding.btnSendResetLink.setOnClickListener { sendResetEmail() }
    }

    override fun onDestroy() {
        binding.root.removeCallbacks(finishRunnable)
        super.onDestroy()
    }

    private fun observeViewModel() {
        viewModel.resetState.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visible()
                    binding.btnSendResetLink.isEnabled = false
                }
                is Resource.Success -> {
                    binding.progressBar.gone()
                    binding.btnSendResetLink.isEnabled = true
                    binding.root.snackbar(getString(R.string.success_reset_link))
                    binding.root.postDelayed(finishRunnable, 2000)
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    binding.btnSendResetLink.isEnabled = true
                    binding.root.snackbar(resource.message)
                }
            }
        }
    }

    private fun sendResetEmail() {
        val email = binding.etEmail.text?.toString()?.trim() ?: ""
        binding.tilEmail.error = null

        if (email.isEmpty()) {
            binding.tilEmail.error = getString(R.string.err_empty_email)
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.err_invalid_email)
            return
        }

        viewModel.resetPassword(email)
    }
}
