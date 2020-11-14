package com.givekesh.raters.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.givekesh.raters.R

object BindingAdapters {
    @BindingAdapter("indicator")
    @JvmStatic
    fun setTextColor(view: TextView, indicator: String) {
        val color = when {
            indicator.contains("-") -> ContextCompat.getColor(view.context, R.color.low)
            indicator.contains("+") -> ContextCompat.getColor(view.context, R.color.high)
            else -> ContextCompat.getColor(view.context, android.R.color.tab_indicator_text)
        }
        view.setTextColor(color)
    }

    @BindingAdapter("src")
    @JvmStatic
    fun setImageResource(view: ImageView, alpha2: String) {
        val resource = view.context.resources
        val keys = resource.getStringArray(R.array.flag_alpha2)
        val values = resource.obtainTypedArray(R.array.flag_drawable)
        val drawableRes = values.getResourceId(keys.indexOf(alpha2), android.R.color.transparent)
        view.setImageResource(drawableRes)
        values.recycle()
    }
}