package com.example.doctorappointmentapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doctorappointmentapp.model.User
import com.example.doctorappointmentapp.repository.AuthRepository
import com.example.doctorappointmentapp.utils.Resource
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _loginState = MutableLiveData<Resource<String>>()
    val loginState: LiveData<Resource<String>> = _loginState

    private val _registerState = MutableLiveData<Resource<String>>()
    val registerState: LiveData<Resource<String>> = _registerState

    private val _roleState = MutableLiveData<Resource<String>>()
    val roleState: LiveData<Resource<String>> = _roleState

    private val _resetState = MutableLiveData<Resource<Unit>>()
    val resetState: LiveData<Resource<Unit>> = _resetState

    fun login(email: String, password: String) {
        _loginState.value = Resource.Loading
        viewModelScope.launch {
            _loginState.value = repository.loginUser(email, password)
        }
    }

    fun register(user: User, password: String) {
        _registerState.value = Resource.Loading
        viewModelScope.launch {
            _registerState.value = repository.registerUser(user, password)
        }
    }

    fun getRole(uid: String) {
        _roleState.value = Resource.Loading
        viewModelScope.launch {
            _roleState.value = repository.getUserRole(uid)
        }
    }

    fun resetPassword(email: String) {
        _resetState.value = Resource.Loading
        viewModelScope.launch {
            _resetState.value = repository.resetPassword(email)
        }
    }

    fun signOut() {
        repository.logout()
    }
}
