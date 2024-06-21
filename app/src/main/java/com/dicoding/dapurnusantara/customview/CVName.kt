package com.dicoding.dapurnusantara.customview

import android.content.Context
import android.graphics.Canvas
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.dapurnusantara.R

class CVName : AppCompatEditText, View.OnFocusChangeListener {

    var isNameValid = false

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
        inputType = InputType.TYPE_CLASS_TEXT
        onFocusChangeListener = this
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.hint_masukkan_nama)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            validateName()
        }
    }

    private fun validateName() {
        isNameValid = text.toString().trim().isNotEmpty()
        error = if (!isNameValid) {
            resources.getString(R.string.error_empty_name)
        } else {
            null
        }
    }
}