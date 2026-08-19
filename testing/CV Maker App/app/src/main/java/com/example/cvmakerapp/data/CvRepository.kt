package com.example.cvmakerapp.data

import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ExperienceEntry(
    val company: String = "",
    val role: String = "",
    val dates: String = "",
    val description: String = ""
)

data class EducationEntry(
    val school: String = "",
    val degree: String = "",
    val dates: String = ""
)

data class CvData(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "",
    val jobTitle: String = "",
    val location: String = "",
    val email: String = "",
    val phone: String = "",
    val linkedIn: String = "",
    val website: String = "",
    val profileImageUri: Uri? = null,
    val summary: String = "",
    val experiences: List<ExperienceEntry> = emptyList(),
    val education: List<EducationEntry> = emptyList(),
    val skills: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

object CvRepository {
    private val _cvs = MutableStateFlow<List<CvData>>(emptyList())
    val cvs: StateFlow<List<CvData>> = _cvs

    private var _previewCv: CvData? = null
    var previewCv: CvData?
        get() = _previewCv
        set(value) { _previewCv = value }

    fun saveCv(cv: CvData) {
        val currentList = _cvs.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == cv.id }
        if (index != -1) {
            currentList[index] = cv
        } else {
            currentList.add(cv)
        }
        _cvs.value = currentList
    }

}
