package com.abdanzakialifian.storyapp.utils

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.ViewGroup
import com.abdanzakialifian.storyapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class CustomEditTextEmail : TextInputLayout {
    private lateinit var editText: TextInputEditText
    private lateinit var errorEmail: String
    private lateinit var hintEmail: String

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
        errorEmail = resources.getString(R.string.invalid_email)
        hintEmail = resources.getString(R.string.email)
        createEditBox(editText)
    }

    private fun createEditBox(editText: TextInputEditText) {
        // set edittext inside text input layout
        val layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            150
        )
        editText.layoutParams = layoutParams
        editText.hint = hintEmail
        addView(editText)

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // remove error information if text empty
                isErrorEnabled =
                    if (editText.text?.isEmpty() == true) false else !isValidateString(editText.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setBoxCornerRadii(30F, 30F, 30F, 30F)
        isHintEnabled = false
        if (isErrorEnabled) {
            error = errorEmail
        }
    }

    private fun isValidateString(str: String): Boolean =
        EMAIL_ADDRESS_PATTERN.matcher(str).matches()

    companion object {
        // email validate
        private val EMAIL_ADDRESS_PATTERN =
            Pattern.compile("[a-zA-Z0-9+._%\\-]{1,256}" + "@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+")
    }
}