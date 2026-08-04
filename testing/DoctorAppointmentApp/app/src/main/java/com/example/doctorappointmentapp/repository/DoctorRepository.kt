package com.example.doctorappointmentapp.repository

import com.example.doctorappointmentapp.model.Doctor
import com.example.doctorappointmentapp.model.Review
import com.example.doctorappointmentapp.utils.Constants
import com.example.doctorappointmentapp.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DoctorRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun getAllDoctors(): Resource<List<Doctor>> = try {
        val snap = db.collection(Constants.DOCTORS_COLLECTION).get().await()
        val list = snap.documents.mapNotNull { doc ->
            doc.toObject(Doctor::class.java)?.copy(id = doc.id)
        }
        Resource.Success(list)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Failed to load doctors")
    }

    suspend fun getDoctorById(doctorId: String): Resource<Doctor> {
        return try {
            val doc = db.collection(Constants.DOCTORS_COLLECTION).document(doctorId).get().await()
            val doctor = doc.toObject(Doctor::class.java)?.copy(id = doc.id)
                ?: return Resource.Error("Doctor not found")
            Resource.Success(doctor)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to load doctor")
        }
    }

    suspend fun getDoctorReviews(doctorId: String): Resource<List<Review>> = try {
        val snap = db.collection(Constants.REVIEWS_COLLECTION)
            .whereEqualTo("doctorId", doctorId)
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get().await()
        val list = snap.documents.mapNotNull { it.toObject(Review::class.java)?.copy(id = it.id) }
        Resource.Success(list)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Failed to load reviews")
    }

    suspend fun submitReview(review: Review): Resource<String> = try {
        val ref = db.collection(Constants.REVIEWS_COLLECTION).add(review).await()
        Resource.Success(ref.id)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Failed to submit review")
    }

    suspend fun toggleFavourite(patientId: String, doctorId: String, add: Boolean): Resource<Unit> = try {
        val ref = db.collection(Constants.USERS_COLLECTION).document(patientId)
        if (add) ref.update("favourites", com.google.firebase.firestore.FieldValue.arrayUnion(doctorId)).await()
        else     ref.update("favourites", com.google.firebase.firestore.FieldValue.arrayRemove(doctorId)).await()
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Failed to update favourites")
    }
}
