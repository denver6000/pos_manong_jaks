package com.denproj.posmanongjaks.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denproj.posmanongjaks.hilt.qualifier.FirestoreImpl
import com.denproj.posmanongjaks.model.ItemStockRecord
import com.denproj.posmanongjaks.repository.base.StockReportRepository
import com.denproj.posmanongjaks.repository.impl.SessionRepository
import com.denproj.posmanongjaks.util.OnDataReceived
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockReportViewModel @Inject constructor(@FirestoreImpl var stockReportRepository: StockReportRepository, var sessionRepository: SessionRepository) : ViewModel () {

    private val _mutableStateOfStockReport = mutableStateOf(emptyList<ItemStockRecord>())
    val state = _mutableStateOfStockReport

    private val _isLoading = mutableStateOf(true)
    val isLoading = _isLoading

    private val _isRefreshing: MutableState<Boolean> = mutableStateOf(false)
    val isRefreshing = _isRefreshing

    init {
        getStockReport(null)
    }

    fun getStockReport(finished: (() -> Unit)?) {
        viewModelScope.launch {
            val session = sessionRepository.getSession()
            if (session != null) {
                stockReportRepository.getReportsOfBranch(session.branchId!!, object : OnDataReceived<List<ItemStockRecord>> {
                    override fun onSuccess(result: List<ItemStockRecord>?) {
                        _mutableStateOfStockReport.value = result ?: emptyList()
                        _isLoading.value = false
                        finished?.invoke()
                    }

                    override fun onFail(e: Exception?) {
                        _isLoading.value = false
                        finished?.invoke()
                    }

                })
            }
        }
    }


}
