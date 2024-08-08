package com.example.submissionawal.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText

class EmailEdit : TextInputEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        // Setup here if needed
    }

    fun isValidEmail(): Boolean {
        val email = text?.toString() ?: ""
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}