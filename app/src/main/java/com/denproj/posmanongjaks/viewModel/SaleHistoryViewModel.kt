package com.denproj.posmanongjaks.viewModel

import android.util.Log
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denproj.posmanongjaks.hilt.qualifier.FirestoreImpl
import com.denproj.posmanongjaks.model.Sale
import com.denproj.posmanongjaks.model.SaleItem
import com.denproj.posmanongjaks.model.SaleProduct
import com.denproj.posmanongjaks.repository.firestoreImpl.FirestoreSaleRepository
import com.denproj.posmanongjaks.repository.impl.SessionRepository
import com.denproj.posmanongjaks.session.SessionSimple
import com.denproj.posmanongjaks.util.OnDataReceived
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SaleHistoryViewModel @Inject constructor(@FirestoreImpl val firestoreSaleRepository: FirestoreSaleRepository, val sessionRepository: SessionRepository) : ViewModel() {

    private val _sales: MutableStateFlow<List<Sale>> = MutableStateFlow(emptyList())
    private val _session: MutableStateFlow<SessionSimple?> = MutableStateFlow(null)
    private val _stats: MutableStateFlow<HashMap<String, Int>> = MutableStateFlow(hashMapOf())

    val sales: StateFlow<List<Sale>> = _sales
    val session: StateFlow<SessionSimple?> = _session
    val stats: StateFlow<HashMap<String, Int>> = _stats

    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading = _isLoading

    init {
        getStatistics(null)
    }

    fun getStatistics(onFinished: (() -> Unit)?) {
        viewModelScope.launch {
            val sessionSimple = sessionRepository.getSession()
            _session.emit(sessionSimple)

            firestoreSaleRepository.observeSales(sessionSimple!!.branchId!!, object : OnDataReceived<List<Sale>> {
                override fun onSuccess(result: List<Sale>?) {
                    _sales.update { it -> result!! }
                    onFinished?.invoke()
                }

                override fun onFail(e: java.lang.Exception?) {
                    Log.e("Error", e!!.message!!)
                    onFinished?.invoke()
                }

            })


            firestoreSaleRepository.observeCurrentSaleStat(sessionSimple!!.branchId!!, object : OnDataReceived<HashMap<String, Int>> {
                override fun onSuccess(result: HashMap<String, Int>?) {
                    _stats.value = result!!
                    onFinished?.invoke()
                }

                override fun onFail(e: Exception?) {
                    _stats.value = HashMap()
                    onFinished?.invoke()
                }
            })
        }
    }

    suspend fun getSaleProducts(saleId: String): List<SaleProduct>{
        val firebaseFirestore = FirebaseFirestore.getInstance()
        val salesRecord = firebaseFirestore.collection("sale_product")
        val result = salesRecord.whereEqualTo("saleId", saleId).get().await()
        if (result == null || result.documents.isEmpty()) {
            return emptyList()
        }
        val saleProducts = result.toObjects(SaleProduct::class.java)
        return saleProducts
    }

    suspend fun getSaleAddOn(saleId: String): List<SaleItem> {
        val firebaseFirestore = FirebaseFirestore.getInstance()
        val salesRecord = firebaseFirestore.collection("sale_item")
        val result = salesRecord.whereEqualTo("saleId", saleId).get().await()
        if (result == null || result.documents.isEmpty()) {
            return emptyList()
        }
        val saleProducts = result.toObjects(SaleItem::class.java)
        return saleProducts
    }

    fun getSalesProduct(saleId: Int, onDataReceived: OnDataReceived<List<SaleProduct>>) {
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        val salesProductCollection = firestore.collection("sales_product")
        salesProductCollection.whereEqualTo("saleId", saleId.toInt()).get().addOnSuccessListener {
            val saleProducts = it.toObjects(SaleProduct::class.java)
            onDataReceived.onSuccess(saleProducts)
        }.addOnFailureListener {
            onDataReceived.onFail(it)
        }
    }

    fun getSalesItem(saleId: Int, onDataReceived: OnDataReceived<List<SaleItem>>) {
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        val salesItemCollection = firestore.collection("sales_item")
        salesItemCollection.whereEqualTo("saleId", saleId).get().addOnSuccessListener {
            val saleItems = it.toObjects(SaleItem::class.java)
            onDataReceived.onSuccess(saleItems)
        }.addOnFailureListener {
            onDataReceived.onFail(it)
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
