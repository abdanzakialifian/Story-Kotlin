package com.abdanzakialifian.storyapp.utils

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
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

        editText.doOnTextChanged { _, _, _, _ ->
            if (editText.text?.isNotEmpty() == true || editText.text?.isEmpty() == true) isErrorEnabled =
                false
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setBoxCornerRadii(30F, 30F, 30F, 30F)
        isHintEnabled = false
    }
}