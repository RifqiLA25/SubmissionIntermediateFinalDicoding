package com.example.submissionawal.help

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingSource {

    private const val RESOURCE = "GLOBAL"

    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}

inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {
    EspressoIdlingSource.increment()
    return try {
        function()
    } finally {
        EspressoIdlingSource.decrement()
    }
}