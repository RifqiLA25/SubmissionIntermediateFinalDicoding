package com.example.submissionawal.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.submissionawal.data.local.room.UserDatabase
import com.example.submissionawal.data.remote.response.ListStory
import com.example.submissionawal.data.repository.StoryRepository
import com.example.submissionawal.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val story: LiveData<PagingData<ListStory>> =
        repository.getStoriesAll().cachedIn(viewModelScope)

    fun getSession(): LiveData<UserDatabase> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}