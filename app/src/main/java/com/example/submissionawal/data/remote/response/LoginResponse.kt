package com.example.submissionawal.data.remote.response

import com.google.gson.annotations.SerializedName

class LoginResponse(

@field:SerializedName("loginResult")
val loginResult: ResultLogin? = null,

@field:SerializedName("error")
val error: Boolean? = null,

@field:SerializedName("message")
val message: String? = null
)

data class ResultLogin(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("token")
    val token: String? = null
)