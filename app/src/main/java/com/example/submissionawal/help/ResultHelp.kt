package com.example.submissionawal.help

sealed class ResultHelp<out R> private constructor() {
    data class Success<out T>(val data: T) : ResultHelp<T>()
    data class Error(val error: String?) : ResultHelp<Nothing>()
    object Loading : ResultHelp<Nothing>()
}