package com.givekesh.raters.utils

import android.content.Context
import android.content.res.TypedArray
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.givekesh.raters.R

object BindingAdapters {

    @JvmStatic
    private var TEMP_STRING_ARRAY: Array<String>? = null

    @JvmStatic
    private fun getTempStringArray(context: Context): Array<String> {
        var tempArray: Array<String>? = TEMP_STRING_ARRAY
        if (tempArray == null) {
            tempArray = context.resources.getStringArray(R.array.flag_alpha2)
            TEMP_STRING_ARRAY = tempArray
        }
        return tempArray
    }

    @JvmStatic
    private var TEMP_TYPED_ARRAY: TypedArray? = null

    @JvmStatic
    private fun getTempTypedArray(context: Context): TypedArray {
        var tempArray: TypedArray? = TEMP_TYPED_ARRAY
        if (tempArray == null) {
            tempArray = context.resources.obtainTypedArray(R.array.flag_drawable)
            TEMP_TYPED_ARRAY = tempArray
        }
        return tempArray
    }


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
        val keys = getTempStringArray(view.context)
        val values = getTempTypedArray(view.context)
        val drawableRes = values.getResourceId(keys.indexOf(alpha2), android.R.color.transparent)
        view.setImageResource(drawableRes)
    }
}