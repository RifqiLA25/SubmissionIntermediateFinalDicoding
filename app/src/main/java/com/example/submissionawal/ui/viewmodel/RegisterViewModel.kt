package com.example.submissionawal.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.submissionawal.data.remote.response.RegisterResponse
import com.example.submissionawal.data.repository.UserRepository

class RegisterViewModel (private val repository: UserRepository) : ViewModel() {
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return repository.register(name,email,password)
    }
}