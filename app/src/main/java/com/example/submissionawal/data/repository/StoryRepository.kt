package com.example.submissionawal.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.submissionawal.data.local.room.StoryDatabase
import com.example.submissionawal.data.local.room.UserDatabase
import com.example.submissionawal.data.mediator.StoryRemokeMediator
import com.example.submissionawal.data.remote.pref.UserPreference
import com.example.submissionawal.data.remote.response.DetailStoryResponse
import com.example.submissionawal.data.remote.response.ListStory
import com.example.submissionawal.data.remote.response.StoryResponse
import com.example.submissionawal.data.remote.retrofit.ApiService
import com.example.submissionawal.help.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow


class StoryRepository private constructor(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase,
    private val userPreference: UserPreference
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getStoriesAll(): LiveData<PagingData<ListStory>> {
        wrapEspressoIdlingResource {
            return Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
                remoteMediator = StoryRemokeMediator(storyDatabase, apiService),
                pagingSourceFactory = {
                    storyDatabase.storyDao().getAllStories()
                }
            ).liveData
        }
    }

    suspend fun getStoriesWithLocation(): StoryResponse {
        return apiService.getStoriesWithLocation(location = 1)
    }

    suspend fun getStoryDetail(storyId: String): DetailStoryResponse {
        return apiService.getStoryDetail(storyId)
    }


    fun getSession(): Flow<UserDatabase> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        fun getInstance(
            apiService: ApiService,
            storyDatabase: StoryDatabase,
            userPreference: UserPreference
        ) = StoryRepository(apiService, storyDatabase, userPreference)
    }
}