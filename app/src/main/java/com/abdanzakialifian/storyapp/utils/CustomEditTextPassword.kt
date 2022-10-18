package com.abdanzakialifian.storyapp.utils

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.ViewGroup
import com.abdanzakialifian.storyapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomEditTextPassword : TextInputLayout {

    private lateinit var editText: TextInputEditText
    private lateinit var errorPassword: String
    private lateinit var hintPassword: String
    private var isLengthPassword: Boolean = false

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
        errorPassword = resources.getString(R.string.error_password)
        hintPassword = resources.getString(R.string.password)
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
        editText.hint = hintPassword
        editText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD

        // check edit text is empty or not before typing event
        isLengthPassword = editText.length() < 6 && editText.text.toString().isNotEmpty()
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // check edit text is empty or not after typing event
                isLengthPassword = editText.length() < 6 && editText.text.toString().isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        isPasswordVisibilityToggleEnabled = true
        setBoxCornerRadii(30F, 30F, 30F, 30F)
        isHintEnabled = false
        if (isLengthPassword) {
            isErrorEnabled = true
            error = errorPassword
        } else {
            isErrorEnabled = false
        }
    }
}