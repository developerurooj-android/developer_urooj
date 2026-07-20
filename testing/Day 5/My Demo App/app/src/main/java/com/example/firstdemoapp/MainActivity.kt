package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)

        loginButton.setOnClickListener {

            if (email.text.toString().isEmpty()) {
                email.error = "Enter Email"
            } else if (password.text.toString().isEmpty()) {
                password.error = "Enter Password"
            } else {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            }

        }
    }
}