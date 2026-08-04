package com.example.doctorappointmentapp.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.databinding.ActivityRegisterBinding
import com.example.doctorappointmentapp.model.User
import com.example.doctorappointmentapp.utils.Constants
import com.example.doctorappointmentapp.utils.Resource
import com.example.doctorappointmentapp.utils.gone
import com.example.doctorappointmentapp.utils.snackbar
import com.example.doctorappointmentapp.utils.visible
import com.example.doctorappointmentapp.viewmodel.AuthViewModel
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()
    private val storage by lazy { FirebaseStorage.getInstance() }
    private var selectedImageUri: Uri? = null
    private val navigateRunnable = Runnable {
        val role = binding.actRole.text.toString().trim()
        navigateByRole(role)
    }

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            Glide.with(this).load(it).circleCrop().into(binding.ivProfile)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDropdowns()
        observeViewModel()

        binding.fabAddImage.setOnClickListener { imagePickerLauncher.launch("image/*") }
        binding.ivProfile.setOnClickListener   { imagePickerLauncher.launch("image/*") }
        binding.etDob.setOnClickListener       { showDatePicker() }
        binding.btnRegister.setOnClickListener { attemptRegister() }
        binding.tvLogin.setOnClickListener     { finish() }
    }

    override fun onDestroy() {
        binding.root.removeCallbacks(navigateRunnable)
        super.onDestroy()
    }

    private fun setupDropdowns() {
        binding.actRole.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line,
                listOf(Constants.ROLE_PATIENT, Constants.ROLE_DOCTOR))
        )
        binding.actGender.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line,
                listOf(Constants.GENDER_MALE, Constants.GENDER_FEMALE, Constants.GENDER_OTHER))
        )
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, day ->
            cal.set(year, month, day)
            binding.etDob.setText(
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(cal.time)
            )
        }, cal.get(Calendar.YEAR) - 20,
           cal.get(Calendar.MONTH),
           cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun observeViewModel() {
        viewModel.registerState.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visible()
                    binding.btnRegister.isEnabled = false
                }
                is Resource.Success -> {
                    binding.progressBar.gone()
                    binding.btnRegister.isEnabled = true
                    binding.root.snackbar(getString(R.string.success_register))
                    binding.root.postDelayed(navigateRunnable, 800)
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    binding.btnRegister.isEnabled = true
                    binding.root.snackbar(resource.message)
                }
            }
        }
    }

    private fun attemptRegister() {
        val name            = binding.etName.text?.toString()?.trim()    ?: ""
        val email           = binding.etEmail.text?.toString()?.trim()   ?: ""
        val phone           = binding.etPhone.text?.toString()?.trim()   ?: ""
        val dob             = binding.etDob.text?.toString()?.trim()     ?: ""
        val role            = binding.actRole.text?.toString()?.trim()   ?: ""
        val gender          = binding.actGender.text?.toString()?.trim() ?: ""
        val password        = binding.etPassword.text?.toString()        ?: ""
        val confirmPassword = binding.etConfirmPassword.text?.toString() ?: ""

        listOf(binding.tilName, binding.tilEmail, binding.tilPhone, binding.tilDob,
               binding.tilRole, binding.tilGender, binding.tilPassword,
               binding.tilConfirmPassword).forEach { it.error = null }

        var valid = true
        if (name.isEmpty())    { binding.tilName.error = getString(R.string.err_empty_name);     valid = false }
        if (email.isEmpty())   { binding.tilEmail.error = getString(R.string.err_empty_email);   valid = false }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.err_invalid_email); valid = false
        }
        if (phone.isEmpty())   { binding.tilPhone.error = getString(R.string.err_empty_phone);   valid = false }
        if (dob.isEmpty())     { binding.tilDob.error = getString(R.string.err_empty_dob);       valid = false }
        if (role.isEmpty())    { binding.tilRole.error = getString(R.string.err_empty_role);     valid = false }
        if (gender.isEmpty())  { binding.tilGender.error = getString(R.string.err_empty_gender); valid = false }
        if (password.isEmpty())        { binding.tilPassword.error = getString(R.string.err_empty_password); valid = false }
        else if (password.length < 6) { binding.tilPassword.error = getString(R.string.err_password_short); valid = false }
        if (password != confirmPassword) {
            binding.tilConfirmPassword.error = getString(R.string.err_password_mismatch); valid = false
        }
        if (!valid) return

        // If user picked a profile image, upload first then register
        if (selectedImageUri != null) {
            uploadImageThenRegister(name, email, phone, dob, role, gender, password)
        } else {
            val user = buildUser(name, email, phone, dob, role, gender, "")
            viewModel.register(user, password)
        }
    }

    private fun uploadImageThenRegister(
        name: String, email: String, phone: String, dob: String,
        role: String, gender: String, password: String
    ) {
        binding.progressBar.visible()
        binding.btnRegister.isEnabled = false
        // Use a temp path; after registration the uid will replace it
        val tempRef = storage.reference.child("profile_pictures/temp_${System.currentTimeMillis()}.jpg")
        tempRef.putFile(selectedImageUri!!)
            .addOnSuccessListener {
                tempRef.downloadUrl.addOnSuccessListener { uri ->
                    val user = buildUser(name, email, phone, dob, role, gender, uri.toString())
                    viewModel.register(user, password)
                }.addOnFailureListener {
                    // Upload failed — proceed without image
                    val user = buildUser(name, email, phone, dob, role, gender, "")
                    viewModel.register(user, password)
                }
            }
            .addOnFailureListener {
                val user = buildUser(name, email, phone, dob, role, gender, "")
                viewModel.register(user, password)
            }
    }

    private fun buildUser(
        name: String, email: String, phone: String, dob: String,
        role: String, gender: String, imageUrl: String
    ) = User(
        uid      = "",   // will be set in AuthRepository after createUser
        name     = name,
        email    = email,
        phone    = phone,
        gender   = gender,
        dob      = dob,
        role     = role.lowercase(),
        imageUrl = imageUrl
    )

    private fun navigateByRole(role: String) {
        val normalized = role.lowercase()
        val target = if (normalized == Constants.ROLE_DOCTOR.lowercase()) {
            DoctorHomeActivity::class.java
        } else {
            PatientHomeActivity::class.java
        }
        startActivity(Intent(this, target).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
