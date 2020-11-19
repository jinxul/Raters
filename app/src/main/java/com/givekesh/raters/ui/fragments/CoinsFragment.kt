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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_offline.*
import kotlinx.android.synthetic.main.dialog_offline.view.*
import kotlinx.android.synthetic.main.fragment_layout.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CoinsFragment : Fragment() {

    private val coinsViewModel: CoinsViewModel by activityViewModels()
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
            R.id.menu_refresh -> sendIntent(MainIntent.RefreshCoins)
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
                    is DataState.Success -> updateData(dataState.data, dataState.isOffline)
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
        loading_layout?.visibility = View.VISIBLE
        list?.visibility = View.GONE
        list_error?.visibility = View.GONE
    }

    private fun showRefreshIndicator() {
        swipe?.isRefreshing = true
    }

    private fun updateData(coins: List<RecyclerItemModel>, isOffline: Boolean) {
        list?.visibility = View.VISIBLE
        list_error?.visibility = View.GONE
        loading_layout?.visibility = View.GONE
        adapter.updateData(coins)
        list?.adapter = adapter
        swipe?.isRefreshing = false
        if (isOffline)
            showOfflineDialog()
    }

    private fun showOfflineDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetTheme)
        val sheetView = layoutInflater.inflate(R.layout.dialog_offline, bottom_sheet)
        sheetView.offline_continue.setOnClickListener { bottomSheetDialog.dismiss() }
        sheetView.retry_online.setOnClickListener {
            sendIntent(MainIntent.RefreshCoins)
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(sheetView)
        bottomSheetDialog.show()
    }

    private fun showError(exception: Exception) {
        list?.visibility = View.GONE
        loading_layout?.visibility = View.GONE
        list_error?.visibility = View.VISIBLE
        val errorMessage = Utils().getErrorMessage(requireContext(), exception)
        list_error?.text = errorMessage
        swipe?.isRefreshing = false
    }
}