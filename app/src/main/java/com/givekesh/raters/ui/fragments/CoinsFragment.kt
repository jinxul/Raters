package com.givekesh.raters.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.givekesh.raters.R
import com.givekesh.raters.data.models.RecyclerItemModel
import com.givekesh.raters.ui.adapters.RecyclerViewAdapter
import com.givekesh.raters.ui.viewmodels.CoinsViewModel
import com.givekesh.raters.utils.DataState
import com.givekesh.raters.utils.MainIntent
import com.givekesh.raters.utils.Utils
import com.google.android.material.transition.MaterialFadeThrough
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import com.givekesh.raters.databinding.FragmentLayoutBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CoinsFragment : Fragment() {

    private val coinsViewModel: CoinsViewModel by activityViewModels()
    private val adapter: RecyclerViewAdapter = RecyclerViewAdapter()
    private var fragmentCoinsBinding: FragmentLayoutBinding? = null

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
        fragmentCoinsBinding = binding
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_refresh -> sendIntent(MainIntent.RefreshCoins)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSwipeRefresh()
        subscribeObserver()
    }

    override fun onDestroyView() {
        fragmentCoinsBinding = null
        super.onDestroyView()
    }

    private fun setupSwipeRefresh() {
        fragmentCoinsBinding?.swipe?.setOnRefreshListener {
            sendIntent(MainIntent.RefreshCoins)
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
        fragmentCoinsBinding?.loadingLayout?.root?.visibility = View.VISIBLE
        fragmentCoinsBinding?.list?.visibility = View.GONE
        fragmentCoinsBinding?.listError?.visibility = View.GONE
    }

    private fun showRefreshIndicator() {
        fragmentCoinsBinding?.swipe?.isRefreshing = true
    }

    private fun updateData(coins: List<RecyclerItemModel>) {
        fragmentCoinsBinding?.list?.visibility = View.VISIBLE
        fragmentCoinsBinding?.listError?.visibility = View.GONE
        fragmentCoinsBinding?.loadingLayout?.root?.visibility = View.GONE
        adapter.updateData(coins)
        fragmentCoinsBinding?.list?.adapter = adapter
        fragmentCoinsBinding?.swipe?.isRefreshing = false
    }

    private fun showError(exception: Exception) {
        fragmentCoinsBinding?.list?.visibility = View.GONE
        fragmentCoinsBinding?.loadingLayout?.root?.visibility = View.GONE
        fragmentCoinsBinding?.listError?.visibility = View.VISIBLE
        val errorMessage = Utils().getErrorMessage(requireContext(), exception)
        fragmentCoinsBinding?.listError?.text = errorMessage
        fragmentCoinsBinding?.swipe?.isRefreshing = false
        FirebaseCrashlytics.getInstance().recordException(exception)
    }
}