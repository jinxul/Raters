package com.givekesh.raters.databinding

import android.content.res.TypedArray
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.givekesh.raters.R
import com.givekesh.raters.di.scopes.BindingAdaptersScope
import javax.inject.Inject

@BindingAdaptersScope
class BindingAdapters @Inject constructor(
    private val keys: Array<String>,
    private val values: TypedArray
) {

    @BindingAdapter("indicator")
    fun setTextColor(view: TextView, indicator: String) {
        val color = when {
            indicator.contains("-") -> ContextCompat.getColor(view.context, R.color.low)
            indicator.contains("+") -> ContextCompat.getColor(view.context, R.color.high)
            else -> ContextCompat.getColor(view.context, android.R.color.tab_indicator_text)
        }
        view.setTextColor(color)
    }

    @BindingAdapter("src")
    fun setImageResource(view: ImageView, alpha2: String) {
        try {
            val drawableRes = values.getResourceId(
                keys.indexOf(alpha2),
                android.R.color.transparent
            )
            view.setImageResource(drawableRes)
        } catch (e: Exception) {
        }
    }
}