package com.example.submissionawal.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionawal.data.remote.response.DetailStoryResponse
import com.example.submissionawal.data.repository.StoryRepository
import com.example.submissionawal.data.repository.UserRepository
import kotlinx.coroutines.launch

class DetailStoryViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _story = MutableLiveData<DetailStoryResponse>()
    val story: LiveData<DetailStoryResponse> = _story

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStoryDetail(storyId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getStoryDetail(storyId)
                _story.value = response
            } catch (e: Exception) {
                _error.value = DetailStoryResponse(error = true, message = e.message).toString()
            } finally {
                _isLoading.value = false
            }
        }
    }
}