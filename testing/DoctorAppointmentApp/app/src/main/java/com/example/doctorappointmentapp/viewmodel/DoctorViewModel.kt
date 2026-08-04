package com.example.doctorappointmentapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doctorappointmentapp.model.Doctor
import com.example.doctorappointmentapp.model.Review
import com.example.doctorappointmentapp.repository.DoctorRepository
import com.example.doctorappointmentapp.utils.Resource
import kotlinx.coroutines.launch

class DoctorViewModel(private val repo: DoctorRepository = DoctorRepository()) : ViewModel() {

    private val _doctors = MutableLiveData<Resource<List<Doctor>>>()
    val doctors: LiveData<Resource<List<Doctor>>> = _doctors

    private val _doctor = MutableLiveData<Resource<Doctor>>()
    val doctor: LiveData<Resource<Doctor>> = _doctor

    private val _reviews = MutableLiveData<Resource<List<Review>>>()
    val reviews: LiveData<Resource<List<Review>>> = _reviews

    private val _favouriteState = MutableLiveData<Resource<Unit>>()
    val favouriteState: LiveData<Resource<Unit>> = _favouriteState

    private val _reviewSubmit = MutableLiveData<Resource<String>>()
    val reviewSubmit: LiveData<Resource<String>> = _reviewSubmit

    fun loadDoctors() {
        _doctors.value = Resource.Loading
        viewModelScope.launch { _doctors.value = repo.getAllDoctors() }
    }

    fun loadDoctor(doctorId: String) {
        _doctor.value = Resource.Loading
        viewModelScope.launch { _doctor.value = repo.getDoctorById(doctorId) }
    }

    fun loadReviews(doctorId: String) {
        _reviews.value = Resource.Loading
        viewModelScope.launch { _reviews.value = repo.getDoctorReviews(doctorId) }
    }

    fun toggleFavourite(patientId: String, doctorId: String, add: Boolean) {
        viewModelScope.launch { _favouriteState.value = repo.toggleFavourite(patientId, doctorId, add) }
    }

    fun submitReview(review: Review) {
        _reviewSubmit.value = Resource.Loading
        viewModelScope.launch { _reviewSubmit.value = repo.submitReview(review) }
    }
}
