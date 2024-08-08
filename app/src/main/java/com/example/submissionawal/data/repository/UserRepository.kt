package com.example.submissionawal.data.repository

import com.example.submissionawal.data.local.room.UserDatabase
import com.example.submissionawal.data.remote.pref.UserPreference
import com.example.submissionawal.data.remote.response.LoginResponse
import com.example.submissionawal.data.remote.response.RegisterResponse
import com.example.submissionawal.data.remote.retrofit.ApiService

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        val response = apiService.login(email, password)
        if (response.error == false && response.loginResult != null) {
            saveSession(UserDatabase(email, response.loginResult.token.toString(), true))
        }
        return response
    }


    suspend fun saveSession(user: UserDatabase) {
        userPreference.saveSession(user)
    }

    companion object {
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ) = UserRepository(userPreference, apiService)
    }
}