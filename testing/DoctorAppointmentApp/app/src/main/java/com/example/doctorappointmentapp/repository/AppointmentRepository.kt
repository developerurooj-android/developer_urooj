package com.example.doctorappointmentapp.repository

import com.example.doctorappointmentapp.model.Appointment
import com.example.doctorappointmentapp.model.PaymentModel
import com.example.doctorappointmentapp.model.Prescription
import com.example.doctorappointmentapp.utils.Constants
import com.example.doctorappointmentapp.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class AppointmentRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    // ── One-shot ─────────────────────────────────────────────────

    suspend fun bookAppointment(appointment: Appointment): Resource<String> = try {
        // Ensure default status is Upcoming
        val finalAppt = appointment.copy(status = Constants.STATUS_UPCOMING)
        val ref = db.collection(Constants.APPOINTMENTS_COLLECTION).add(finalAppt).await()
        Resource.Success(ref.id)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Failed to book appointment")
    }

    suspend fun getPatientAppointments(patientId: String): Resource<List<Appointment>> = try {
        val snap = db.collection(Constants.APPOINTMENTS_COLLECTION)
            .whereEqualTo("patientId", patientId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get().await()
        val list = snap.documents.mapNotNull { it.toObject(Appointment::class.java)?.copy(id = it.id) }
        Resource.Success(list)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Failed to load appointments")
    }

    suspend fun getDoctorAppointments(doctorId: String): Resource<List<Appointment>> = try {
        val snap = db.collection(Constants.APPOINTMENTS_COLLECTION)
            .whereEqualTo("doctorId", doctorId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get().await()
        val list = snap.documents.mapNotNull { it.toObject(Appointment::class.java)?.copy(id = it.id) }
        Resource.Success(list)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Failed to load appointments")
    }

    // ── Real-time listeners ───────────────────────────────────────

    /**
     * Attach a real-time snapshot listener for a patient's appointments.
     * Returns the [ListenerRegistration] so the caller can remove it in onDestroy.
     */
    fun listenPatientAppointments(
        patientId: String,
        onUpdate: (List<Appointment>) -> Unit
    ): ListenerRegistration =
        db.collection(Constants.APPOINTMENTS_COLLECTION)
            .whereEqualTo("patientId", patientId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, _ ->
                val list = snap?.documents?.mapNotNull {
                    it.toObject(Appointment::class.java)?.copy(id = it.id)
                } ?: emptyList()
                onUpdate(list)
            }

    fun listenDoctorAppointments(
        doctorId: String,
        onUpdate: (List<Appointment>) -> Unit
    ): ListenerRegistration =
        db.collection(Constants.APPOINTMENTS_COLLECTION)
            .whereEqualTo("doctorId", doctorId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, _ ->
                val list = snap?.documents?.mapNotNull {
                    it.toObject(Appointment::class.java)?.copy(id = it.id)
                } ?: emptyList()
                onUpdate(list)
            }

    // ── Mutations ─────────────────────────────────────────────────

    suspend fun updateAppointmentStatus(appointmentId: String, status: String): Resource<Unit> = try {
        db.collection(Constants.APPOINTMENTS_COLLECTION)
            .document(appointmentId)
            .update("status", status).await()
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Failed to update appointment")
    }

    suspend fun submitPayment(payment: PaymentModel): Resource<String> = try {
        val ref = db.collection(Constants.PAYMENTS_COLLECTION).add(payment).await()
        db.collection(Constants.APPOINTMENTS_COLLECTION)
            .document(payment.appointmentId)
            .update(mapOf(
                "paymentStatus" to payment.status,
                "paymentId"     to ref.id
            )).await()
        Resource.Success(ref.id)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Failed to submit payment")
    }

    suspend fun getPrescription(prescriptionId: String): Resource<Prescription> {
        return try {
            val doc = db.collection(Constants.PRESCRIPTIONS_COLLECTION).document(prescriptionId).get().await()
            val p = doc.toObject(Prescription::class.java)?.copy(id = doc.id)
                ?: return Resource.Error("Prescription not found")
            Resource.Success(p)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to load prescription")
        }
    }
}
