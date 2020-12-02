package com.givekesh.raters.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.givekesh.raters.data.models.RecyclerItemModel
import com.givekesh.raters.databinding.FragmentLayoutBinding
import com.givekesh.raters.ui.activities.MainActivity
import com.givekesh.raters.ui.adapters.RecyclerViewAdapter
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.lang.Exception

@ExperimentalCoroutinesApi
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
        fragmentBinding?.loadingLayout?.root?.visibility = View.VISIBLE
        fragmentBinding?.swipe?.visibility = View.GONE
    }

    protected fun showRefreshIndicator() {
        fragmentBinding?.swipe?.isRefreshing = true
    }

    protected fun updateData(coins: List<RecyclerItemModel>) {
        fragmentBinding?.swipe?.visibility = View.VISIBLE
        fragmentBinding?.list?.visibility = View.VISIBLE
        fragmentBinding?.errorLayout?.visibility = View.GONE
        fragmentBinding?.loadingLayout?.root?.visibility = View.GONE
        fragmentBinding?.list?.adapter = adapter
        fragmentBinding?.swipe?.isRefreshing = false
        adapter.updateData(coins)
    }

    protected fun showError(exception: Exception) {
        val errorMessage = (activity as MainActivity).utils.getErrorMessage(exception)
        FirebaseCrashlytics.getInstance().recordException(exception)
        fragmentBinding?.swipe?.visibility = View.VISIBLE
        fragmentBinding?.list?.visibility = View.GONE
        fragmentBinding?.loadingLayout?.root?.visibility = View.GONE
        fragmentBinding?.errorLayout?.visibility = View.VISIBLE
        fragmentBinding?.listError?.text = errorMessage
        fragmentBinding?.swipe?.isRefreshing = false
    }
}