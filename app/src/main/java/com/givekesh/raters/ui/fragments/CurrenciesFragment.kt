package com.givekesh.raters.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.givekesh.raters.R
import com.givekesh.raters.data.models.RecyclerItemModel
import com.givekesh.raters.ui.adapters.RecyclerViewAdapter
import com.givekesh.raters.ui.viewmodels.CurrenciesViewModel
import com.givekesh.raters.utils.DataState
import com.givekesh.raters.utils.MainIntent
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_offline.*
import kotlinx.android.synthetic.main.dialog_offline.view.*
import kotlinx.android.synthetic.main.fragment_layout.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import java.lang.Exception
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CurrenciesFragment : Fragment() {

    private val currenciesViewModel: CurrenciesViewModel by activityViewModels()
    private val adapter: RecyclerViewAdapter = RecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSwipeRefresh()
        subscribeObserver()
    }

    private fun setupSwipeRefresh() {
        swipe?.setOnRefreshListener {
            sendIntent()
        }
    }

    private fun subscribeObserver() {
        lifecycleScope.launch {
            currenciesViewModel.dataState.collect { dataState ->
                when (dataState) {
                    is DataState.Idle -> sendIntent()
                    is DataState.Loading -> showLoading()
                    is DataState.Success -> updateData(dataState.data, dataState.isOffline)
                    is DataState.Failed -> showError(dataState.exception)
                }
            }
        }
    }

    private fun sendIntent() {
        lifecycleScope.launch {
            currenciesViewModel.channel.send(MainIntent.GetCurrencies)
        }
    }

    private fun showLoading() {
        swipe?.isRefreshing = true
    }

    private fun updateData(currencies: List<RecyclerItemModel>, isOffline: Boolean) {
        list?.visibility = View.VISIBLE
        list_error?.visibility = View.GONE
        adapter.updateData(currencies)
        swipe?.isRefreshing = false
        list?.adapter = adapter
        if (isOffline) {
            showOfflineDialog()
        }
    }

    private fun showOfflineDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetTheme)
        val sheetView = layoutInflater.inflate(R.layout.dialog_offline, bottom_sheet)
        sheetView.offline_continue.setOnClickListener { bottomSheetDialog.dismiss() }
        sheetView.retry_online.setOnClickListener {
            sendIntent()
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(sheetView)
        bottomSheetDialog.show()
    }

    private fun showError(error: Exception) {
        list?.visibility = View.GONE
        list_error?.visibility = View.VISIBLE
        val errorMessage = when {
            error.message.isNullOrBlank() -> getString(R.string.unexpected_error)
            error is UnknownHostException -> getString(R.string.empty_list_error)
            else -> error.message
        }
        list_error?.text = errorMessage
        swipe?.isRefreshing = false
    }
}