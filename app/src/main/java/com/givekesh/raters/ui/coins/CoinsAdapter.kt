package com.givekesh.raters.ui.coins

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.givekesh.raters.BR
import com.givekesh.raters.data.models.CoinsModel
import com.givekesh.raters.databinding.ListItemCoinBinding
import com.givekesh.raters.utils.diff.CoinsDiffCallBack

class CoinsAdapter : RecyclerView.Adapter<CoinsViewHolder>() {
    private val items = mutableListOf<CoinsModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCoinBinding.inflate(inflater, parent, false)
        return CoinsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun getItem(position: Int): CoinsModel {
        return items[position]
    }

    fun updateData(items: List<CoinsModel>) {
        val diffCallback = CoinsDiffCallBack(this.items, items)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallback)
        this.items.clear()
        this.items.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }
}

class CoinsViewHolder(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(coin: CoinsModel) {
        binding.setVariable(BR.coin, coin)
    }
}