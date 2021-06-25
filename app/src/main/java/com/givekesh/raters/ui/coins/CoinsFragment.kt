package com.givekesh.raters.ui.coins

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.givekesh.raters.R
import com.givekesh.raters.data.models.RecyclerItemModel
import com.givekesh.raters.ui.adapters.RecyclerViewAdapter
import com.givekesh.raters.utils.DataState
import com.givekesh.raters.utils.MainIntent
import dagger.hilt.android.AndroidEntryPoint
import com.givekesh.raters.databinding.FragmentLayoutBinding
import com.givekesh.raters.ui.main.MainActivity
import com.givekesh.raters.utils.onQueryTextChanged
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

@AndroidEntryPoint
class CoinsFragment : Fragment() {

    private var _binding: FragmentLayoutBinding? = null
    private val binding get() = _binding!!

    private var adapter: RecyclerViewAdapter = RecyclerViewAdapter()

    private val coinsViewModel: CoinsViewModel by activityViewModels()

    private lateinit var searchView: SearchView

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
        val searchQuery = coinsViewModel.searchQuery
        if (searchQuery != null && searchQuery != "") {
            searchView.onActionViewExpanded()
            searchView.setQuery(searchQuery, false)
        }
        searchView.onQueryTextChanged {
            sendIntent(MainIntent.SearchCoins(it))
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_refresh -> sendIntent(MainIntent.RefreshCoins)
            R.id.menu_choose_theme -> (activity as MainActivity).utils.showThemeMenu()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSwipeRefresh()
        subscribeObserver()
        (activity as MainActivity).registerNetworkListener {
            sendIntent(MainIntent.RefreshCoins)
        }
        binding.refresh.setOnClickListener {
            sendIntent(MainIntent.GetCoins)
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipe.setOnRefreshListener {
            sendIntent(MainIntent.RefreshCoins)
            searchView.onActionViewCollapsed()
        }
    }

    private fun subscribeObserver() {
        lifecycleScope.launch {
            coinsViewModel.dataState.collect { dataState ->
                when (dataState) {
                    is DataState.Idle -> sendIntent(MainIntent.GetCoins)
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
            coinsViewModel.channel.send(intent)
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
        _binding = null
        super.onDestroyView()
    }
}