package com.givekesh.raters.ui.fragments

import android.view.View
import androidx.fragment.app.Fragment
import com.givekesh.raters.data.models.RecyclerItemModel
import com.givekesh.raters.databinding.FragmentLayoutBinding
import com.givekesh.raters.ui.adapters.RecyclerViewAdapter
import com.givekesh.raters.utils.Utils
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.lang.Exception

abstract class BaseFragment : Fragment() {

    abstract var fragmentBinding: FragmentLayoutBinding?
    abstract var adapter: RecyclerViewAdapter

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }

    protected fun showLoading() {
        fragmentBinding?.loadingLayout?.root?.visibility = View.VISIBLE
        fragmentBinding?.list?.visibility = View.GONE
        fragmentBinding?.listError?.visibility = View.GONE
    }

    protected fun showRefreshIndicator() {
        fragmentBinding?.swipe?.isRefreshing = true
    }

    protected fun updateData(coins: List<RecyclerItemModel>) {
        fragmentBinding?.list?.visibility = View.VISIBLE
        fragmentBinding?.listError?.visibility = View.GONE
        fragmentBinding?.loadingLayout?.root?.visibility = View.GONE
        fragmentBinding?.list?.adapter = adapter
        fragmentBinding?.swipe?.isRefreshing = false
        adapter.updateData(coins)
    }

    protected fun showError(exception: Exception) {
        val errorMessage = Utils().getErrorMessage(requireContext(), exception)
        FirebaseCrashlytics.getInstance().recordException(exception)
        fragmentBinding?.list?.visibility = View.GONE
        fragmentBinding?.loadingLayout?.root?.visibility = View.GONE
        fragmentBinding?.listError?.visibility = View.VISIBLE
        fragmentBinding?.listError?.text = errorMessage
        fragmentBinding?.swipe?.isRefreshing = false
    }
}