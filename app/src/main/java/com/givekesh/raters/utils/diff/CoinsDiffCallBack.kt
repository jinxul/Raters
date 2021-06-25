package com.givekesh.raters.utils.diff

import androidx.recyclerview.widget.DiffUtil
import com.givekesh.raters.data.models.CoinsModel

class CoinsDiffCallBack(
    private val oldList: MutableList<CoinsModel>,
    private val newList: List<CoinsModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean = oldList[oldItemPosition].title == newList[newItemPosition].title

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean = oldList[oldItemPosition] == newList[newItemPosition]
}