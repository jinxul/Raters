package com.givekesh.raters.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.givekesh.raters.R
import com.givekesh.raters.data.models.RecyclerItemModel
import com.givekesh.raters.databinding.FragmentLayoutBinding
import com.givekesh.raters.ui.adapters.RecyclerViewAdapter
import com.givekesh.raters.ui.viewmodels.CurrenciesViewModel
import com.givekesh.raters.utils.DataState
import com.givekesh.raters.utils.MainIntent
import com.givekesh.raters.utils.Utils
import com.google.android.material.transition.MaterialFadeThrough
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import java.lang.Exception

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CurrenciesFragment : Fragment() {

    private val currenciesViewModel: CurrenciesViewModel by activityViewModels()
    private val adapter: RecyclerViewAdapter = RecyclerViewAdapter()
    private var fragmentCurrenciesBinding: FragmentLayoutBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLayoutBinding.inflate(inflater, container, false)
        fragmentCurrenciesBinding = binding
        return binding.root
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

    override fun onDestroyView() {
        fragmentCurrenciesBinding = null
        super.onDestroyView()
    }

    private fun setupSwipeRefresh() {
        fragmentCurrenciesBinding?.swipe?.setOnRefreshListener {
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
        fragmentCurrenciesBinding?.loadingLayout?.root?.visibility = View.VISIBLE
        fragmentCurrenciesBinding?.list?.visibility = View.GONE
        fragmentCurrenciesBinding?.listError?.visibility = View.GONE
    }

    private fun showRefreshIndicator() {
        fragmentCurrenciesBinding?.swipe?.isRefreshing = true
    }

    private fun updateData(currencies: List<RecyclerItemModel>) {
        fragmentCurrenciesBinding?.list?.visibility = View.VISIBLE
        fragmentCurrenciesBinding?.listError?.visibility = View.GONE
        fragmentCurrenciesBinding?.loadingLayout?.root?.visibility = View.GONE
        adapter.updateData(currencies)
        fragmentCurrenciesBinding?.swipe?.isRefreshing = false
        fragmentCurrenciesBinding?.list?.adapter = adapter
    }

    private fun showError(exception: Exception) {
        fragmentCurrenciesBinding?.list?.visibility = View.GONE
        fragmentCurrenciesBinding?.loadingLayout?.root?.visibility = View.GONE
        fragmentCurrenciesBinding?.listError?.visibility = View.VISIBLE
        val errorMessage = Utils().getErrorMessage(requireContext(), exception)
        fragmentCurrenciesBinding?.listError?.text = errorMessage
        fragmentCurrenciesBinding?.swipe?.isRefreshing = false
        FirebaseCrashlytics.getInstance().recordException(exception)
    }
}