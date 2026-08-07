package com.example.doctorappointmentapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

interface AppointmentRepository {
    fun getAppointments(onResult: (List<Appointment>) -> Unit, onError: (Exception) -> Unit)
    fun addAppointment(appointment: Appointment, onComplete: (Boolean) -> Unit)
    fun updateAppointment(appointment: Appointment, onComplete: (Boolean) -> Unit)
    fun deleteAppointment(appointmentId: String, onComplete: (Boolean) -> Unit)
}

class FirestoreAppointmentRepository : AppointmentRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val collectionPath = "Appointments"

    override fun getAppointments(onResult: (List<Appointment>) -> Unit, onError: (Exception) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onError(Exception("User not authenticated"))
            return
        }
        
        db.collection(collectionPath)
            .whereEqualTo("patientId", uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    onError(e)
                    return@addSnapshotListener
                }
                
                val list = snapshot?.documents?.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    Appointment(
                        appointmentId = doc.id,
                        patientId = data["patientId"]?.toString() ?: "",
                        doctorId = data["doctorId"]?.toString() ?: "",
                        patientName = data["patientName"]?.toString() ?: "",
                        doctorName = data["doctorName"]?.toString() ?: "",
                        specialization = data["specialization"]?.toString() ?: "",
                        date = data["date"]?.toString() ?: "",
                        time = data["time"]?.toString() ?: "",
                        status = data["status"]?.toString() ?: "",
                        symptoms = data["symptoms"]?.toString() ?: "",
                        notes = data["notes"]?.toString() ?: "",
                        consultationFee = data["consultationFee"]?.toString() ?: "",
                        profileImage = data["profileImage"]?.toString() ?: ""
                    )
                } ?: emptyList()
                onResult(list)
            }
    }

    override fun addAppointment(appointment: Appointment, onComplete: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        val newDoc = db.collection(collectionPath).document()
        val data = appointment.copy(appointmentId = newDoc.id, patientId = uid)
        
        newDoc.set(data)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    override fun updateAppointment(appointment: Appointment, onComplete: (Boolean) -> Unit) {
        if (appointment.appointmentId.isEmpty()) return
        
        db.collection(collectionPath).document(appointment.appointmentId)
            .set(appointment)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    override fun deleteAppointment(appointmentId: String, onComplete: (Boolean) -> Unit) {
        if (appointmentId.isEmpty()) return
        
        db.collection(collectionPath).document(appointmentId)
            .delete()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun seedMockData(onComplete: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        val mockData = listOf(
            Appointment(
                doctorName = "Dr. Sarah Ahmed",
                specialization = "Cardiologist",
                date = "12 Oct 2023",
                time = "10:30 AM",
                status = "Upcoming",
                consultationFee = "2500.0",
                profileImage = "https://img.freepik.com/free-photo/woman-doctor-wearing-lab-coat-with-stethoscope-isolated_1303-29791.jpg"
            ),
            Appointment(
                doctorName = "Dr. Ali Khan",
                specialization = "Dermatologist",
                date = "15 Oct 2023",
                time = "02:00 PM",
                status = "Upcoming",
                consultationFee = "2000.0",
                profileImage = "https://img.freepik.com/free-photo/doctor-offering-medical-tele-consultation_23-2149329007.jpg"
            ),
            Appointment(
                doctorName = "Dr. Maria Qureshi",
                specialization = "Pediatrician",
                date = "10 Oct 2023",
                time = "09:00 AM",
                status = "Completed",
                consultationFee = "1500.0",
                profileImage = "https://img.freepik.com/free-photo/smiling-female-doctor-white-coat-standing-with-arms-crossed-hospital-office_231208-12966.jpg"
            )
        )

        val batch = db.batch()
        mockData.forEach { appointment ->
            val docRef = db.collection(collectionPath).document()
            val data = appointment.copy(appointmentId = docRef.id, patientId = uid)
            batch.set(docRef, data)
        }

        batch.commit()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}
