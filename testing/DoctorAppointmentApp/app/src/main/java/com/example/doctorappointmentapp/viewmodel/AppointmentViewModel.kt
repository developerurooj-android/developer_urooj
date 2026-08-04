package com.example.doctorappointmentapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doctorappointmentapp.model.Appointment
import com.example.doctorappointmentapp.model.PaymentModel
import com.example.doctorappointmentapp.model.Prescription
import com.example.doctorappointmentapp.repository.AppointmentRepository
import com.example.doctorappointmentapp.utils.Constants
import com.example.doctorappointmentapp.utils.Resource
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch

class AppointmentViewModel(
    private val repo: AppointmentRepository = AppointmentRepository()
) : ViewModel() {

    // ── Live appointment list (real-time) ─────────────────────────
    private val _appointments = MutableLiveData<List<Appointment>>()
    val appointments: LiveData<List<Appointment>> = _appointments

    // ── One-shot states ───────────────────────────────────────────
    private val _bookState    = MutableLiveData<Resource<String>>()
    val bookState: LiveData<Resource<String>> = _bookState

    private val _updateState  = MutableLiveData<Resource<Unit>>()
    val updateState: LiveData<Resource<Unit>> = _updateState

    private val _paymentState = MutableLiveData<Resource<String>>()
    val paymentState: LiveData<Resource<String>> = _paymentState

    private val _prescription = MutableLiveData<Resource<Prescription>>()
    val prescription: LiveData<Resource<Prescription>> = _prescription

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    // ── Listener registration (must be removed on destroy) ────────
    private var appointmentListener: ListenerRegistration? = null

    // ── Real-time subscriptions ───────────────────────────────────

    fun startListeningPatient(patientId: String) {
        appointmentListener?.remove()
        _loading.value = true
        appointmentListener = repo.listenPatientAppointments(patientId) { list ->
            _loading.postValue(false)
            _appointments.postValue(list)
        }
    }

    fun startListeningDoctor(doctorId: String) {
        appointmentListener?.remove()
        _loading.value = true
        appointmentListener = repo.listenDoctorAppointments(doctorId) { list ->
            _loading.postValue(false)
            _appointments.postValue(list)
        }
    }

    fun stopListening() {
        appointmentListener?.remove()
        appointmentListener = null
    }

    // ── Legacy one-shot (kept for places that still use it) ───────

    fun loadPatientAppointments(patientId: String) {
        _loading.value = true
        viewModelScope.launch {
            val res = repo.getPatientAppointments(patientId)
            _loading.postValue(false)
            if (res is Resource.Success) _appointments.postValue(res.data)
        }
    }

    fun loadDoctorAppointments(doctorId: String) {
        _loading.value = true
        viewModelScope.launch {
            val res = repo.getDoctorAppointments(doctorId)
            _loading.postValue(false)
            if (res is Resource.Success) _appointments.postValue(res.data)
        }
    }

    // ── Actions ───────────────────────────────────────────────────

    fun bookAppointment(appointment: Appointment) {
        _bookState.value = Resource.Loading
        viewModelScope.launch { _bookState.value = repo.bookAppointment(appointment) }
    }

    fun cancelAppointment(appointmentId: String) {
        viewModelScope.launch {
            _updateState.value = repo.updateAppointmentStatus(appointmentId, Constants.STATUS_CANCELLED)
        }
    }

    fun acceptAppointment(appointmentId: String) {
        viewModelScope.launch {
            _updateState.value = repo.updateAppointmentStatus(appointmentId, Constants.STATUS_ACCEPTED)
        }
    }

    fun rejectAppointment(appointmentId: String) {
        viewModelScope.launch {
            _updateState.value = repo.updateAppointmentStatus(appointmentId, Constants.STATUS_CANCELLED)
        }
    }

    fun completeAppointment(appointmentId: String) {
        viewModelScope.launch {
            _updateState.value = repo.updateAppointmentStatus(appointmentId, Constants.STATUS_COMPLETED)
        }
    }

    fun submitPayment(payment: PaymentModel) {
        _paymentState.value = Resource.Loading
        viewModelScope.launch { _paymentState.value = repo.submitPayment(payment) }
    }

    fun loadPrescription(prescriptionId: String) {
        _prescription.value = Resource.Loading
        viewModelScope.launch { _prescription.value = repo.getPrescription(prescriptionId) }
    }

    override fun onCleared() {
        stopListening()
        super.onCleared()
    }
}
