package com.abdanzakialifian.storyapp.utils

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.ViewGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomEditText : TextInputLayout {
    private lateinit var editText: TextInputEditText

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

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText.text?.isNotEmpty() == true || editText.text?.isEmpty() == true) isErrorEnabled =
                    false
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setBoxCornerRadii(30F, 30F, 30F, 30F)
        isHintEnabled = false
    }
}