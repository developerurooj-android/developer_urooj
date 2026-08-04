package com.example.doctorappointmentapp.repository

import android.net.Uri
import com.example.doctorappointmentapp.model.MedicalReport
import com.example.doctorappointmentapp.utils.Constants
import com.example.doctorappointmentapp.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class MedicalReportRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) {
    suspend fun uploadReport(patientId: String, uri: Uri, report: MedicalReport): Resource<String> = try {
        val fileName = "report_${System.currentTimeMillis()}.${report.fileType}"
        val ref = storage.reference.child("${Constants.STORAGE_REPORTS}/$patientId/$fileName")
        ref.putFile(uri).await()
        val url = ref.downloadUrl.await().toString()
        val saved = report.copy(fileUrl = url, patientId = patientId)
        val docRef = db.collection(Constants.REPORTS_COLLECTION).add(saved).await()
        Resource.Success(docRef.id)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Upload failed")
    }

    suspend fun getReports(patientId: String): Resource<List<MedicalReport>> = try {
        val snap = db.collection(Constants.REPORTS_COLLECTION)
            .whereEqualTo("patientId", patientId)
            .orderBy("uploadedAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get().await()
        val list = snap.documents.mapNotNull { it.toObject(MedicalReport::class.java)?.copy(id = it.id) }
        Resource.Success(list)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Failed to load reports")
    }

    suspend fun deleteReport(reportId: String, fileUrl: String): Resource<Unit> = try {
        storage.getReferenceFromUrl(fileUrl).delete().await()
        db.collection(Constants.REPORTS_COLLECTION).document(reportId).delete().await()
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Failed to delete report")
    }
}
