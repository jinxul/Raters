package com.givekesh.raters.ui.currencies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.givekesh.raters.BR
import com.givekesh.raters.data.models.CurrenciesModel
import com.givekesh.raters.databinding.ListItemCurrencyBinding
import com.givekesh.raters.utils.diff.CurrenciesDiffCallBack

class CurrenciesAdapter : RecyclerView.Adapter<CurrenciesViewHolder>() {
    private val items = mutableListOf<CurrenciesModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCurrencyBinding.inflate(inflater, parent, false)
        return CurrenciesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrenciesViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun getItem(position: Int): CurrenciesModel {
        return items[position]
    }

    fun updateData(items: List<CurrenciesModel>) {
        val diffCallback = CurrenciesDiffCallBack(this.items, items)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallback)
        this.items.clear()
        this.items.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }
}

class CurrenciesViewHolder(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(currency: CurrenciesModel) {
        binding.setVariable(BR.currency, currency)
    }
}