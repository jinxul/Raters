package com.givekesh.raters.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.givekesh.raters.R
import com.givekesh.raters.databinding.FragmentLayoutBinding
import com.givekesh.raters.ui.MainActivity
import com.givekesh.raters.ui.adapters.RecyclerViewAdapter
import com.givekesh.raters.ui.viewmodels.CurrenciesViewModel
import com.givekesh.raters.utils.DataState
import com.givekesh.raters.utils.MainIntent
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CurrenciesFragment : BaseFragment() {

    private val currenciesViewModel: CurrenciesViewModel by activityViewModels()

    override var fragmentBinding: FragmentLayoutBinding? = null
    override var adapter: RecyclerViewAdapter = RecyclerViewAdapter()

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
        fragmentBinding = binding
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_refresh -> sendIntent(MainIntent.RefreshCurrencies)
            R.id.menu_choose_theme -> (activity as MainActivity).utils.showThemeMenu()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSwipeRefresh()
        subscribeObserver()
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }

    private fun setupSwipeRefresh() {
        fragmentBinding?.swipe?.setOnRefreshListener {
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
}