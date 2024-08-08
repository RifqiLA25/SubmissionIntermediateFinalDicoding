package com.example.submissionawal.di

import android.content.Context
import com.example.submissionawal.data.local.room.StoryDatabase
import com.example.submissionawal.data.remote.pref.UserPreference
import com.example.submissionawal.data.remote.pref.dataStore
import com.example.submissionawal.data.remote.retrofit.ApiConfig
import com.example.submissionawal.data.repository.StoryRepository
import com.example.submissionawal.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(pref, apiService)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val storyDatabase = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(apiService, storyDatabase, pref)
    }
}
