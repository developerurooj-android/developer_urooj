package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
// credentials
        val correctEmail = "admin@gmail.com"
        val correctPassword = "123456"

        btnLogin.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email == correctEmail && password == correctPassword) {

                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)

                finish()

            } else {

                Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show()

            }
            val btnLogout = findViewById<Button>(R.id.btnLogout)

            btnLogout.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}