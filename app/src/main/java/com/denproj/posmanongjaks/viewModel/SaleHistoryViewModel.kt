package com.denproj.posmanongjaks.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denproj.posmanongjaks.hilt.qualifier.FirebaseImpl
import com.denproj.posmanongjaks.hilt.qualifier.FirestoreImpl
import com.denproj.posmanongjaks.model.Sale
import com.denproj.posmanongjaks.repository.base.SaleRepository
import com.denproj.posmanongjaks.repository.firestoreImpl.FirestoreSaleRepository
import com.denproj.posmanongjaks.repository.impl.SessionRepository
import com.denproj.posmanongjaks.session.SessionSimple
import com.denproj.posmanongjaks.util.OnDataReceived
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SaleHistoryViewModel @Inject constructor(@FirestoreImpl firestoreSaleRepository: FirestoreSaleRepository, var sessionRepository: SessionRepository) : ViewModel() {

    private val _sales: MutableStateFlow<List<Sale>> = MutableStateFlow(emptyList())
    private val _session: MutableStateFlow<SessionSimple?> = MutableStateFlow(null)
    private val _stats: MutableStateFlow<HashMap<String, Int>> = MutableStateFlow(hashMapOf())

    val sales: StateFlow<List<Sale>> = _sales
    val session: StateFlow<SessionSimple?> = _session
    val stats: StateFlow<HashMap<String, Int>> = _stats

    init {
        viewModelScope.launch {
            val sessionSimple = sessionRepository.getSession()
            _session.emit(sessionSimple)

            firestoreSaleRepository.observeSales(sessionSimple!!.branchId!!, object : OnDataReceived<List<Sale>> {
                override fun onSuccess(result: List<Sale>?) {
                    _sales.update { it -> result!! }
                }

                override fun onFail(e: java.lang.Exception?) {
                   Log.e("Error", e!!.message!!)
                }

            })


            firestoreSaleRepository.observeCurrentSaleStat(sessionSimple!!.branchId!!, object : OnDataReceived<HashMap<String, Int>> {
                override fun onSuccess(result: HashMap<String, Int>?) {
                    _stats.value = result!!
                }

                override fun onFail(e: Exception?) {
                    _stats.value = HashMap()
                }
            })
        }
    }

    private fun getAllSalesRecord(branchId: String?, onDataReceived: OnDataReceived<List<Sale>>) {
//        saleRepository.getAllRecordedSalesOnBranch(branchId!!, onDataReceived)
    }

    fun getAllSalesRecordTest(branchId: String?) {
//        viewModelScope.launch {
//            try {
//                val sale = saleRepository.getAllSales(branchId!!)
//                _sales.emit(sale)
//            } catch (e: Exception) {
//                _sales.emit(emptyList())
//            }
//        }
    }
}
