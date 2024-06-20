package com.dicoding.dapurnusantara.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.dapurnusantara.R
import com.google.android.material.textfield.TextInputEditText

class CVPassword : AppCompatEditText, View.OnTouchListener {

    var isPasswordValid: Boolean = false
    private var isPasswordVisible: Boolean = false

    init {
        initialize()
    }

    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initialize()
    }

    private fun initialize() {
        background = ContextCompat.getDrawable(context, R.drawable.input_bg)
        transformationMethod = PasswordTransformationMethod.getInstance()
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val drawableEnd = if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                compoundDrawables[0]
            } else {
                compoundDrawables[2]
            }

            drawableEnd?.let {
                if (event.rawX >= (right - it.bounds.width())) {
                    isPasswordVisible = !isPasswordVisible
                    transformationMethod = if (isPasswordVisible) {
                        setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.pass_visible, 0)
                        HideReturnsTransformationMethod.getInstance()
                    } else {
                        setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.pass_invisible, 0)
                        PasswordTransformationMethod.getInstance()
                    }
                    text?.let { setSelection(it.length) }
                    return true
                }
            }
        }
        return false
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.hint_masukkan_password)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (!focused) {
            validatePassword()
        }
    }

    private fun validatePassword() {
        isPasswordValid = (text?.length ?: 0) >= 8
        error = if (!isPasswordValid) {
            resources.getString(R.string.error_password_less_than_8_character)
        } else {
            null
        }
    }
}

