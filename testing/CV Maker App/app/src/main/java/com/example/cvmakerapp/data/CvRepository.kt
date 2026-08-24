package com.example.cvmakerapp.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object CvRepository {
    private val _cvs = MutableStateFlow<List<CvData>>(emptyList())
    val cvs: StateFlow<List<CvData>> = _cvs

    private var _previewCv: CvData? = null
    var previewCv: CvData?
        get() = _previewCv
        set(value) { _previewCv = value }

    private var _selectedTemplate: CvTemplate = CvTemplate.CLASSIC
    var selectedTemplate: CvTemplate
        get() = _selectedTemplate
        set(value) { _selectedTemplate = value }

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
