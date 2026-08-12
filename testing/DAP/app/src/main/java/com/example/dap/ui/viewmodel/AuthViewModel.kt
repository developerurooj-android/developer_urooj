package com.example.dap.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dap.data.local.AppDatabase
import com.example.dap.data.local.entity.User
import com.example.dap.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AuthRepository
    
    var authError by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = AuthRepository(userDao)
    }

    fun signUp(name: String, email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            authError = null
            val result = repository.signup(User(name = name, email = email, password = password))
            isLoading = false
            if (result.isSuccess) {
                onSuccess()
            } else {
                authError = result.exceptionOrNull()?.message
            }
        }
    }

    fun login(email: String, password: String, onSuccess: (User) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            authError = null
            val result = repository.login(email, password)
            isLoading = false
            if (result.isSuccess) {
                onSuccess(result.getOrThrow())
            } else {
                authError = result.exceptionOrNull()?.message
            }
        }
    }

    fun clearError() {
        authError = null
    }
}
