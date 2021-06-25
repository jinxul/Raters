package com.givekesh.raters.ui.currencies

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.givekesh.raters.R
import com.givekesh.raters.data.models.RecyclerItemModel
import com.givekesh.raters.databinding.FragmentLayoutBinding
import com.givekesh.raters.ui.main.MainActivity
import com.givekesh.raters.ui.adapters.RecyclerViewAdapter
import com.givekesh.raters.utils.DataState
import com.givekesh.raters.utils.MainIntent
import com.givekesh.raters.utils.onQueryTextChanged
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import java.lang.Exception

@AndroidEntryPoint
class CurrenciesFragment : Fragment() {

    private var _binding: FragmentLayoutBinding? = null
    private val binding get() = _binding!!

    private var adapter: RecyclerViewAdapter = RecyclerViewAdapter()

    private val currenciesViewModel: CurrenciesViewModel by activityViewModels()

    private lateinit var searchView: SearchView
    private var uiJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.menu_search)
        searchView = searchItem.actionView as SearchView
        val searchQuery = currenciesViewModel.searchQuery
        if (searchQuery != null && searchQuery != "") {
            searchView.onActionViewExpanded()
            searchView.setQuery(searchQuery, false)
        }
        searchView.onQueryTextChanged {
            sendIntent(MainIntent.SearchCurrencies(it))
        }
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
        (activity as MainActivity).registerNetworkListener {
            sendIntent(MainIntent.RefreshCurrencies)
        }
        binding.refresh.setOnClickListener {
            sendIntent(MainIntent.GetCurrencies)
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipe.setOnRefreshListener {
            sendIntent(MainIntent.RefreshCurrencies)
            searchView.onActionViewCollapsed()
        }
    }

    private fun subscribeObserver() {
        uiJob = lifecycleScope.launch {
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
        binding.apply {
            loadingLayout.root.visibility = View.VISIBLE
            swipe.visibility = View.GONE
        }
    }

    private fun showRefreshIndicator() {
        binding.swipe.isRefreshing = true
    }

    private fun updateData(data: List<RecyclerItemModel>) {
        binding.apply {
            swipe.visibility = View.VISIBLE
            list.visibility = View.VISIBLE
            errorLayout.visibility = View.GONE
            loadingLayout.root.visibility = View.GONE
            list.adapter = adapter
            swipe.isRefreshing = false
        }
        adapter.updateData(data)
    }

    private fun showError(exception: Exception) {
        val errorMessage = (activity as MainActivity).utils.getErrorMessage(exception)
        FirebaseCrashlytics.getInstance().recordException(exception)
        binding.apply {
            swipe.visibility = View.VISIBLE
            list.visibility = View.GONE
            loadingLayout.root.visibility = View.GONE
            errorLayout.visibility = View.VISIBLE
            listError.text = errorMessage
            swipe.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        uiJob?.cancel()
    }
}