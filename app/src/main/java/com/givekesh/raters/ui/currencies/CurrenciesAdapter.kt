package com.givekesh.raters.ui.currencies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.givekesh.raters.BR
import com.givekesh.raters.data.models.CurrenciesModel
import com.givekesh.raters.databinding.ListItemCurrencyBinding

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
        this.items.clear()
        this.items.addAll(items)
        notifyItemRangeInserted(0, minOf(0, items.lastIndex))
    }
}

class CurrenciesViewHolder(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(currency: CurrenciesModel) {
        binding.setVariable(BR.currency, currency)
    }
}