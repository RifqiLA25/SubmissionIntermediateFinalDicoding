package com.example.submissionawal.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_key")
class RemoteKey(
    @PrimaryKey
    val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)