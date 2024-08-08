package com.example.submissionawal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionawal.data.local.room.UserDatabase
import com.example.submissionawal.data.remote.response.LoginResponse
import com.example.submissionawal.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel (private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: UserDatabase) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
    suspend fun loginView(email: String?, password: String?): LoginResponse {
        // Pastikan email dan password tidak null
        val safeEmail = email ?: ""
        val safePassword = password ?: ""
        return repository.login(safeEmail, safePassword)
    }
}