package com.example.submissionawal.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.submissionawal.data.remote.response.ListStory

@Dao
interface StoryDao {
    @Query("SELECT * FROM story")
    fun getAllStories(): PagingSource<Int, ListStory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stories: List<ListStory>)

    @Query("DELETE FROM story")
    suspend fun clearAll()
}