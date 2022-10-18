package com.abdanzakialifian.storyapp.utils

import android.content.Context
import android.graphics.Canvas
import android.text.InputType
import android.util.AttributeSet
import android.view.ViewGroup
import com.abdanzakialifian.storyapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomEditTextEmail : TextInputLayout {
    private lateinit var editText: TextInputEditText
    private lateinit var hintEmail: String
    private lateinit var errorEmail: String

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        setWillNotDraw(false)
        editText = TextInputEditText(context)
        hintEmail = resources.getString(R.string.email)
        errorEmail = resources.getString(R.string.invalid_email)
        createEditBox(editText)
    }

    private fun createEditBox(editText: TextInputEditText) {
        // set edittext inside text input layout
        val layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            150
        )
        editText.layoutParams = layoutParams
        addView(editText)
        editText.hint = hintEmail
        editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setBoxCornerRadii(30F, 30F, 30F, 30F)
        isHintEnabled = false
        if (isErrorEnabled) {
            error = errorEmail
        }
    }
}