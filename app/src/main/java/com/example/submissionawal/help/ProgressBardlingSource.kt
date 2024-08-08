package com.example.submissionawal.help

import android.view.View
import android.widget.ProgressBar
import androidx.test.espresso.IdlingResource

class ProgressBardlingSource(private val progressBar: ProgressBar) : IdlingResource {

    @Volatile
    private var callback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return ProgressBardlingSource::class.java.name
    }

    override fun isIdleNow(): Boolean {
        val idle = progressBar.visibility != View.VISIBLE
        if (idle) {
            callback?.onTransitionToIdle()
        }
        return idle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }
}