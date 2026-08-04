package com.example.doctorappointmentapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.databinding.ActivityLoginBinding
import com.example.doctorappointmentapp.utils.Constants
import com.example.doctorappointmentapp.utils.gone
import com.example.doctorappointmentapp.utils.snackbar
import com.example.doctorappointmentapp.utils.visible
import com.example.doctorappointmentapp.viewmodel.AuthViewModel
import com.example.doctorappointmentapp.utils.Resource

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRoleDropdown()
        binding.layoutForm.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.fade_in_up)
        )
        observeViewModel()

        binding.btnLogin.setOnClickListener { attemptLogin() }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setupRoleDropdown() {
        val roles = listOf(Constants.ROLE_PATIENT, Constants.ROLE_DOCTOR, Constants.ROLE_ADMIN)
        binding.actRole.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        )
    }

    private fun observeViewModel() {
        // Step 1: observe login result → then fetch role
        viewModel.loginState.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visible()
                    binding.btnLogin.isEnabled = false
                }
                is Resource.Success -> {
                    // uid returned — now fetch role
                    viewModel.getRole(resource.data)
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    binding.btnLogin.isEnabled = true
                    binding.root.snackbar(resource.message)
                }
            }
        }

        // Step 2: observe role → validate against selected role and navigate
        viewModel.roleState.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> { /* already showing progress */ }
                is Resource.Success -> {
                    binding.progressBar.gone()
                    binding.btnLogin.isEnabled = true
                    val storedRole   = resource.data
                    val selectedRole = binding.actRole.text.toString().trim()
                    if (storedRole.equals(selectedRole, ignoreCase = true)) {
                        navigateByRole(storedRole)
                    } else {
                        binding.root.snackbar(getString(R.string.err_role_mismatch))
                        viewModel.signOut()
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    binding.btnLogin.isEnabled = true
                    binding.root.snackbar(resource.message)
                    viewModel.signOut()
                }
            }
        }
    }

    private fun attemptLogin() {
        val email    = binding.etEmail.text?.toString()?.trim()    ?: ""
        val password = binding.etPassword.text?.toString()         ?: ""
        val role     = binding.actRole.text?.toString()?.trim()    ?: ""

        binding.tilEmail.error    = null
        binding.tilPassword.error = null
        binding.tilRole.error     = null

        var valid = true
        if (email.isEmpty()) {
            binding.tilEmail.error = getString(R.string.err_empty_email); valid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.err_invalid_email); valid = false
        }
        if (password.isEmpty()) {
            binding.tilPassword.error = getString(R.string.err_empty_password); valid = false
        } else if (password.length < 6) {
            binding.tilPassword.error = getString(R.string.err_password_short); valid = false
        }
        if (role.isEmpty()) {
            binding.tilRole.error = getString(R.string.err_empty_role); valid = false
        }
        if (!valid) return

        viewModel.login(email, password)
    }

    private fun navigateByRole(role: String) {
        val normalized = role.lowercase()
        val target = when {
            normalized == Constants.ROLE_DOCTOR.lowercase()  -> DoctorHomeActivity::class.java
            normalized == Constants.ROLE_ADMIN.lowercase()   -> AdminDashboardActivity::class.java
            else                                             -> PatientHomeActivity::class.java
        }
        startActivity(Intent(this, target).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
