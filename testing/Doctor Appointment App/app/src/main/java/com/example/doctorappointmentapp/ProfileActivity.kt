package com.example.doctorappointmentapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.doctorappointmentapp.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        loadProfile()

        binding.btnAppointments.setOnClickListener {
            startActivity(Intent(this, AppointmentActivity::class.java))
        }

        binding.layoutAppointments.setOnClickListener {
            startActivity(Intent(this, AppointmentActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {

            auth.signOut()

            startActivity(
                Intent(this, LoginActivity::class.java)
            )

            finish()
        }
    }


    private fun loadProfile(){

        val uid = auth.currentUser?.uid ?: return


        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->


                if(document.exists()){


                    binding.tvName.text =
                        document.getString("name")


                    binding.tvEmail.text =
                        document.getString("email")


                    binding.tvPhone.text =
                        document.getString("phone")


                    binding.tvGender.text =
                        document.getString("gender")


                    binding.tvDob.text =
                        document.getString("dob")


                    val image =
                        document.getString("profileImage")


                    Glide.with(this)
                        .load(image)
                        .placeholder(R.drawable.profile_placeholder)
                        .into(binding.imgProfile)

                }

            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Failed to load profile",
                    Toast.LENGTH_SHORT
                ).show()

            }

    }
}