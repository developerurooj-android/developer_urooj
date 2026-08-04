package com.example.doctorappointmentapp.activities

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.databinding.ActivityProfileBinding
import com.example.doctorappointmentapp.model.User
import com.example.doctorappointmentapp.utils.Constants
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val auth    by lazy { FirebaseAuth.getInstance() }
    private val db      by lazy { FirebaseFirestore.getInstance() }
    private val storage by lazy { FirebaseStorage.getInstance() }
    private var profileListener: ListenerRegistration? = null
    private var pickedUri: Uri? = null

    private val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            pickedUri = it
            Glide.with(this).load(it).circleCrop().into(binding.ivProfile)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startProfileListener()
        
        binding.btnBack.setOnClickListener { finish() }
        binding.btnEditImage.setOnClickListener { imagePicker.launch("image/*") }
        binding.btnSave.setOnClickListener { saveProfile() }
        binding.btnSettings.setOnClickListener { 
            startActivity(android.content.Intent(this, SettingsActivity::class.java))
        }
        binding.rowLogout.setOnClickListener {
            auth.signOut()
            startActivity(android.content.Intent(this, LoginActivity::class.java).apply {
                flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
    }

    private fun startProfileListener() {
        val uid = auth.currentUser?.uid ?: return
        binding.progressBar.visibility = View.VISIBLE
        
        profileListener = db.collection(Constants.USERS_COLLECTION).document(uid)
            .addSnapshotListener { snapshot, error ->
                binding.progressBar.visibility = View.GONE
                if (error != null) return@addSnapshotListener
                
                val user = snapshot?.toObject(User::class.java) ?: return@addSnapshotListener
                
                // Only update fields if user is not currently editing (to avoid cursor jumps)
                if (!binding.etName.hasFocus()) {
                    binding.etName.setText(user.name)
                }
                if (!binding.etPhone.hasFocus()) {
                    binding.etPhone.setText(user.phone)
                }
                
                binding.tvName.text = user.name
                binding.tvEmailDisplay.text = user.email
                binding.etEmail.setText(user.email)
                
                if (user.imageUrl.isNotEmpty() && pickedUri == null) {
                    Glide.with(this).load(user.imageUrl)
                        .placeholder(R.drawable.ic_user)
                        .circleCrop()
                        .into(binding.ivProfile)
                }
            }
    }

    private fun saveProfile() {
        val uid = auth.currentUser?.uid ?: return
        val newName = binding.etName.text?.toString()?.trim() ?: ""
        val newPhone = binding.etPhone.text?.toString()?.trim() ?: ""

        if (newName.isEmpty()) {
            binding.etName.error = "Name cannot be empty"
            return
        }

        binding.btnSave.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE

        fun updateFirestore(imageUrl: String?) {
            val updates = mutableMapOf<String, Any>(
                "name" to newName,
                "phone" to newPhone
            )
            if (imageUrl != null) updates["imageUrl"] = imageUrl
            
            db.collection(Constants.USERS_COLLECTION).document(uid).update(updates)
                .addOnSuccessListener {
                    binding.btnSave.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, "Profile updated successfully", Snackbar.LENGTH_SHORT).show()
                    pickedUri = null // Reset picked URI after successful upload
                }
                .addOnFailureListener { e ->
                    binding.btnSave.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                    val msg = if (e.message?.contains("PERMISSION_DENIED") == true) 
                        "Permission Denied. Check Firestore Rules." 
                        else "Error: ${e.message}"
                    Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
                }
        }

        if (pickedUri != null) {
            val ref = storage.reference.child("${Constants.STORAGE_PROFILE_PICS}/$uid.jpg")
            ref.putFile(pickedUri!!).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { url -> updateFirestore(url.toString()) }
            }.addOnFailureListener { updateFirestore(null) }
        } else {
            updateFirestore(null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        profileListener?.remove()
    }
}
