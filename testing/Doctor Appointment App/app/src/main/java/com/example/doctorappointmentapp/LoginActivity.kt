package com.example.doctorappointmentapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.doctorappointmentapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Role Spinner
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

        // Login Button
        binding.btnLogin.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val role = binding.spRole.selectedItem.toString()

            // Email Validation
            if (email.isEmpty()) {
                binding.etEmail.error = "Please enter your email"
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
                binding.etPassword.error = "Please enter your password"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                binding.etPassword.error = "Password must be at least 6 characters"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            // Role Validation
            if (role == "Select Role") {
                Toast.makeText(
                    this,
                    "Please select your role",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Firebase Login
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {

                        val uid = auth.currentUser!!.uid
                        val db = FirebaseFirestore.getInstance()

                        db.collection("users").document(uid).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    val userRole = document.getString("role")
                                    if (userRole == role) {
                                        Toast.makeText(
                                            this,
                                            "Login Successful",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        if (role == "Patient") {
                                            startActivity(
                                                Intent(
                                                    this,
                                                    PatientHomeActivity::class.java
                                                )
                                            )
                                        } else {
                                            // TODO: Create DoctorHomeActivity and navigate there
                                            Toast.makeText(this, "Doctor Home not implemented yet", Toast.LENGTH_SHORT).show()
                                        }
                                        finish()
                                    } else {
                                        auth.signOut()
                                        Toast.makeText(
                                            this,
                                            "Role mismatch. Expected: $userRole, Selected: $role",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                } else {
                                    // If not in "users", check "doctors" (in case they were stored there)
                                    db.collection("doctors").document(uid).get()
                                        .addOnSuccessListener { doc ->
                                            if (doc.exists()) {
                                                val drRole = doc.getString("role") ?: "Doctor"
                                                if (drRole == role) {
                                                     Toast.makeText(this, "Login Successful (Doctor)", Toast.LENGTH_SHORT).show()
                                                     // Navigate to Doctor Home when ready
                                                     Toast.makeText(this, "Doctor Home not implemented yet", Toast.LENGTH_SHORT).show()
                                                     finish()
                                                } else {
                                                    auth.signOut()
                                                    Toast.makeText(this, "Role mismatch in doctors collection.", Toast.LENGTH_SHORT).show()
                                                }
                                            } else {
                                                auth.signOut()
                                                Toast.makeText(
                                                    this,
                                                    "User data not found in users or doctors collection.",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                }
                            }
                            .addOnFailureListener {
                                auth.signOut()
                                Toast.makeText(
                                    this,
                                    "Error fetching user data.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                    } else {

                        Toast.makeText(
                            this,
                            task.exception?.localizedMessage ?: "Login Failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        // Forgot Password
        binding.tvForgotPassword.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ForgotPasswordActivity::class.java
                )
            )
        }

        // Sign Up
        binding.tvSignUp.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SignUpActivity::class.java
                )
            )
        }
    }
}