package com.givekesh.raters.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.givekesh.raters.data.models.RecyclerItemModel

class RecyclerViewAdapter : RecyclerView.Adapter<BindingViewHolder>() {
    private val items = mutableListOf<RecyclerItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(inflater, viewType, parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        getItem(position).bind(holder.binding)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).layoutId
    }

    private fun getItem(position: Int): RecyclerItemModel {
        return items[position]
    }

    fun updateData(items: List<RecyclerItemModel>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}

class BindingViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)