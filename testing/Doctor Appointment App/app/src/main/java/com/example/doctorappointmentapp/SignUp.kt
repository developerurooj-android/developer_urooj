package com.example.doctorappointmentapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.doctorappointmentapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Spinner Data
        val roles = arrayOf(
            "Select Role",
            "Doctor",
            "Patient"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            roles
        )

        binding.spRole.adapter = adapter

        // Sign Up Button
        binding.btnSignUp.setOnClickListener {

            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()
            val role = binding.spRole.selectedItem.toString()

            // Name Validation
            if (name.isEmpty()) {
                binding.etName.error = "Enter your full name"
                binding.etName.requestFocus()
                return@setOnClickListener
            }

            // Email Validation
            if (email.isEmpty()) {
                binding.etEmail.error = "Enter your email"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmail.error = "Enter a valid email"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }

            // Password Validation
            if (password.isEmpty()) {
                binding.etPassword.error = "Enter your password"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                binding.etPassword.error = "Password must be at least 6 characters"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            // Confirm Password Validation
            if (confirmPassword.isEmpty()) {
                binding.etConfirmPassword.error = "Confirm your password"
                binding.etConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                binding.etConfirmPassword.error = "Passwords do not match"
                binding.etConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            // Role Validation
            if (role == "Select Role") {
                Toast.makeText(
                    this,
                    "Please select a role",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {

                        val uid = auth.currentUser!!.uid

                        val user = hashMapOf(
                            "uid" to uid,
                            "name" to name,
                            "email" to email,
                            "role" to role
                        )

                        // Save User in Firestore
                        db.collection("users")
                            .document(uid)
                            .set(user)
                            .addOnSuccessListener {

                                Toast.makeText(
                                    this,
                                    "Registration Successful",
                                    Toast.LENGTH_SHORT
                                ).show()

                                startActivity(
                                    Intent(
                                        this,
                                        LoginActivity::class.java
                                    )
                                )

                                finish()
                            }
                            .addOnFailureListener { e ->

                                Toast.makeText(
                                    this,
                                    "Firestore Error: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                    } else {

                        Toast.makeText(
                            this,
                            task.exception?.localizedMessage
                                ?: "Registration Failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        // Login Text
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}