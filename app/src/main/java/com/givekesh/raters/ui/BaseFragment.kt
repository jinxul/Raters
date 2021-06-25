package com.givekesh.raters.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.givekesh.raters.data.models.RecyclerItemModel
import com.givekesh.raters.databinding.FragmentLayoutBinding
import com.givekesh.raters.ui.main.MainActivity
import com.givekesh.raters.ui.adapters.RecyclerViewAdapter
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.lang.Exception

abstract class BaseFragment : Fragment() {

    abstract var fragmentBinding: FragmentLayoutBinding?
    abstract var adapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }

    protected fun showLoading() {
        fragmentBinding?.apply {
            loadingLayout.root.visibility = View.VISIBLE
            swipe.visibility = View.GONE
        }
    }

    protected fun showRefreshIndicator() {
        fragmentBinding?.swipe?.isRefreshing = true
    }

    protected fun updateData(data: List<RecyclerItemModel>) {
        fragmentBinding?.apply {
            swipe.visibility = View.VISIBLE
            list.visibility = View.VISIBLE
            errorLayout.visibility = View.GONE
            loadingLayout.root.visibility = View.GONE
            list.adapter = adapter
            swipe.isRefreshing = false
        }
        adapter.updateData(data)
    }

    protected fun showError(exception: Exception) {
        val errorMessage = (activity as MainActivity).utils.getErrorMessage(exception)
        FirebaseCrashlytics.getInstance().recordException(exception)
        fragmentBinding?.apply {
            swipe.visibility = View.VISIBLE
            list.visibility = View.GONE
            loadingLayout.root.visibility = View.GONE
            errorLayout.visibility = View.VISIBLE
            listError.text = errorMessage
            swipe.isRefreshing = false
        }
    }
}