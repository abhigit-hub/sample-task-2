package com.example.sampletask2.utils.customviews

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.example.sampletask2.R

class TagHeaderView : AppCompatTextView {

    private lateinit var iconDrawable: Drawable

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }


    private fun init(attrs: AttributeSet?) {
        if (attrs == null)
            return

        iconDrawable = resources.getDrawable(R.drawable.vd_tag)

        setUpViewStates()
        showIcons()
    }

    private fun setUpViewStates() {
        setTextColor(resources.getColor(R.color.colorPrimaryDark))
        setPadding(5, 5, 5, 5)
        compoundDrawablePadding = 30
        gravity = Gravity.CENTER
        setBackgroundDrawable(resources.getDrawable(R.drawable.bt_shape))
    }

    private fun showIcons() {
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            iconDrawable,
            null,
            iconDrawable,
            null
        )
    }
}