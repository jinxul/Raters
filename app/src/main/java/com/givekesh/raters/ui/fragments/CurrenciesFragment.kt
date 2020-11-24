package com.givekesh.raters.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.givekesh.raters.R
import com.givekesh.raters.data.models.RecyclerItemModel
import com.givekesh.raters.ui.adapters.RecyclerViewAdapter
import com.givekesh.raters.ui.viewmodels.CurrenciesViewModel
import com.givekesh.raters.utils.DataState
import com.givekesh.raters.utils.MainIntent
import com.givekesh.raters.utils.Utils
import com.google.android.material.transition.MaterialFadeThrough
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_layout.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import java.lang.Exception

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CurrenciesFragment : Fragment() {

    private val currenciesViewModel: CurrenciesViewModel by activityViewModels()
    private val adapter: RecyclerViewAdapter = RecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_layout, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_refresh -> sendIntent(MainIntent.RefreshCurrencies)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSwipeRefresh()
        subscribeObserver()
    }

    private fun setupSwipeRefresh() {
        swipe?.setOnRefreshListener {
            sendIntent(MainIntent.RefreshCurrencies)
        }
    }

    private fun subscribeObserver() {
        lifecycleScope.launch {
            currenciesViewModel.dataState.collect { dataState ->
                when (dataState) {
                    is DataState.Idle -> sendIntent(MainIntent.GetCurrencies)
                    is DataState.Loading -> showLoading()
                    is DataState.Refreshing -> showRefreshIndicator()
                    is DataState.Success -> updateData(dataState.data)
                    is DataState.Failed -> showError(dataState.exception)
                }
            }
        }
    }

    private fun sendIntent(intent: MainIntent) {
        lifecycleScope.launch {
            currenciesViewModel.channel.send(intent)
        }
    }

    private fun showLoading() {
        loading_layout?.visibility = View.VISIBLE
        list?.visibility = View.GONE
        list_error?.visibility = View.GONE
    }

    private fun showRefreshIndicator() {
        swipe?.isRefreshing = true
    }

    private fun updateData(currencies: List<RecyclerItemModel>) {
        list?.visibility = View.VISIBLE
        list_error?.visibility = View.GONE
        loading_layout?.visibility = View.GONE
        adapter.updateData(currencies)
        swipe?.isRefreshing = false
        list?.adapter = adapter
    }

    private fun showError(exception: Exception) {
        list?.visibility = View.GONE
        loading_layout?.visibility = View.GONE
        list_error?.visibility = View.VISIBLE
        val errorMessage = Utils().getErrorMessage(requireContext(), exception)
        list_error?.text = errorMessage
        swipe?.isRefreshing = false
        FirebaseCrashlytics.getInstance().recordException(exception)
    }
}