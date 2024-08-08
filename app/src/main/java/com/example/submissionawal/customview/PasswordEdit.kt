package com.example.submissionawal.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.submissionawal.R

class PasswordEdit @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var clearButtonImage: Drawable
    private var passwordToggleImage: Drawable
    private var isPasswordVisible = false

    init {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.baseline_clear_24) as Drawable
        passwordToggleImage = ContextCompat.getDrawable(context, R.drawable.baseline_visibility_24) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    error = "Password tidak boleh kurang dari 8 karakter"
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // do nothing
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Masukkan password"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        showToggleButton()
    }

    private fun showClearButton() {
        setButtonDrawables(startOfTheText = passwordToggleImage, endOfTheText = clearButtonImage)
    }

    private fun hideClearButton() {
        setButtonDrawables(startOfTheText = passwordToggleImage)
    }

    private fun showToggleButton() {
        setButtonDrawables(startOfTheText = passwordToggleImage)
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null,
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[0] != null) {
            val toggleButtonStart: Float
            val toggleButtonEnd: Float
            val isToggleButtonClicked: Boolean
            val clearButtonStart: Float
            val isClearButtonClicked: Boolean

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                toggleButtonEnd = (passwordToggleImage.intrinsicWidth + paddingStart).toFloat()
                isToggleButtonClicked = event.x < toggleButtonEnd

                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                isClearButtonClicked = event.x > clearButtonStart
            } else {
                toggleButtonStart = (paddingStart).toFloat()
                isToggleButtonClicked = event.x < toggleButtonStart + passwordToggleImage.intrinsicWidth

                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                isClearButtonClicked = event.x > clearButtonStart
            }

            if (isToggleButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        passwordToggleImage = if (isPasswordVisible) {
                            ContextCompat.getDrawable(context, R.drawable.baseline_visibility_24) as Drawable
                        } else {
                            ContextCompat.getDrawable(context, R.drawable.baseline_visibility_off_24) as Drawable
                        }
                        showToggleButton()
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        isPasswordVisible = !isPasswordVisible
                        inputType = if (isPasswordVisible) {
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        } else {
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        }
                        setSelection(text?.length ?: 0)
                        passwordToggleImage = if (isPasswordVisible) {
                            ContextCompat.getDrawable(context, R.drawable.baseline_visibility_24) as Drawable
                        } else {
                            ContextCompat.getDrawable(context, R.drawable.baseline_visibility_off_24) as Drawable
                        }
                        showToggleButton()
                        return true
                    }

                    else -> return false
                }
            }

            if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        showClearButton()
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        text?.clear()
                        hideClearButton()
                        return true
                    }

                    else -> return false
                }
            }
        }
        return false
    }
}
