package com.example.submissionawal.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.submissionawal.data.remote.response.ListStory
import com.example.submissionawal.data.remote.response.UploadResponse
import com.example.submissionawal.data.remote.retrofit.ApiConfig
import com.example.submissionawal.data.repository.StoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody


class AddStoryViewModel(repository: StoryRepository) : ViewModel() {

    private val _response = MutableLiveData<UploadResponse>()
    val response: LiveData<UploadResponse> = _response

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val story: LiveData<PagingData<ListStory>> =
        repository.getStoriesAll().cachedIn(viewModelScope)

    suspend fun uploadStoryImage(
        token: String,
        multipartBody: MultipartBody.Part,
        description: RequestBody,
        lat: Double?,
        lon: Double?
    ): UploadResponse {
        return withContext(Dispatchers.IO) {
            try {
                val apiService = ApiConfig.getApiService(token)
                val response = apiService.uploadImage(multipartBody, description, lat, lon)
                _response.postValue(response)
                response
            } catch (e: Exception) {
                throw e
            }
        }
    }
}
