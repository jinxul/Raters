package com.givekesh.raters.utils

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
            else -> ContextCompat.getColor(view.context, R.color.default_text)
        }
        view.setTextColor(color)
    }
}