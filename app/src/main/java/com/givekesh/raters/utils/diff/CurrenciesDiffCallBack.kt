package com.givekesh.raters.utils.diff

import androidx.recyclerview.widget.DiffUtil
import com.givekesh.raters.data.models.CurrenciesModel

class CurrenciesDiffCallBack(
    private val oldList: MutableList<CurrenciesModel>,
    private val newList: List<CurrenciesModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean = oldList[oldItemPosition].alpha3 == newList[newItemPosition].alpha3

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean = oldList[oldItemPosition] == newList[newItemPosition]
}