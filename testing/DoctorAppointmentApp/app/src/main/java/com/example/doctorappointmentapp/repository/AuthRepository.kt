package com.example.doctorappointmentapp.repository

import com.example.doctorappointmentapp.model.User
import com.example.doctorappointmentapp.utils.Constants
import com.example.doctorappointmentapp.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun loginUser(email: String, password: String): Resource<String> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(auth.currentUser?.uid ?: "")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Login Failed")
        }
    }

    suspend fun registerUser(user: User, password: String): Resource<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(user.email, password).await()
            val uid = result.user?.uid ?: throw Exception("UID null")
            val newUser = user.copy(uid = uid)
            db.collection(Constants.USERS_COLLECTION).document(uid).set(newUser).await()
            Resource.Success(uid)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Registration Failed")
        }
    }

    suspend fun getUserRole(uid: String): Resource<String> {
        return try {
            val document = db.collection(Constants.USERS_COLLECTION).document(uid).get().await()
            val role = document.getString("role") ?: Constants.ROLE_PATIENT.lowercase()
            Resource.Success(role)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to fetch role")
        }
    }

    suspend fun resetPassword(email: String): Resource<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to send reset email")
        }
    }

    fun logout() {
        auth.signOut()
    }
}
