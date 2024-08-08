package com.example.submissionawal.ui.viewmodel.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionawal.data.remote.response.StoryResponse
import com.example.submissionawal.data.repository.StoryRepository
import com.example.submissionawal.data.repository.UserRepository
import kotlinx.coroutines.launch

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _stories = MutableLiveData<StoryResponse>()
    val stories: LiveData<StoryResponse> = _stories

    fun fetchStoriesWithLocation() {
        viewModelScope.launch {
            val response = storyRepository.getStoriesWithLocation()
            _stories.postValue(response)
        }
    }
}
