package com.example.doctorappointmentapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doctorappointmentapp.model.MedicalReport
import com.example.doctorappointmentapp.repository.MedicalReportRepository
import com.example.doctorappointmentapp.utils.Resource
import kotlinx.coroutines.launch

class MedicalReportViewModel(private val repo: MedicalReportRepository = MedicalReportRepository()) : ViewModel() {

    private val _reports = MutableLiveData<Resource<List<MedicalReport>>>()
    val reports: LiveData<Resource<List<MedicalReport>>> = _reports

    private val _uploadState = MutableLiveData<Resource<String>>()
    val uploadState: LiveData<Resource<String>> = _uploadState

    private val _deleteState = MutableLiveData<Resource<Unit>>()
    val deleteState: LiveData<Resource<Unit>> = _deleteState

    fun loadReports(patientId: String) {
        _reports.value = Resource.Loading
        viewModelScope.launch { _reports.value = repo.getReports(patientId) }
    }

    fun uploadReport(patientId: String, uri: Uri, report: MedicalReport) {
        _uploadState.value = Resource.Loading
        viewModelScope.launch { _uploadState.value = repo.uploadReport(patientId, uri, report) }
    }

    fun deleteReport(reportId: String, fileUrl: String) {
        viewModelScope.launch { _deleteState.value = repo.deleteReport(reportId, fileUrl) }
    }
}
