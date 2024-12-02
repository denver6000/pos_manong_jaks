package com.denproj.posmanongjaks.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denproj.posmanongjaks.hilt.qualifier.FirestoreImpl
import com.denproj.posmanongjaks.model.Item
import com.denproj.posmanongjaks.model.ProductWrapper
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseSaleRepository.OnSaleStatus
import com.denproj.posmanongjaks.repository.firestoreImpl.FirestoreSaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckOutDialogViewmodel @Inject constructor(@FirestoreImpl var salesRepository: FirestoreSaleRepository) : ViewModel() {
    fun sell(
        branchId: String,
        selectedItemsToSell: HashMap<Long, ProductWrapper>,
        addOns: HashMap<Item, Int>,
        year: Int,
        month: Int,
        day: Int,
        total: Double,
        payAmount: Double,
        onSaleStatus: OnSaleStatus
    ) {
        viewModelScope.launch {
            salesRepository.processSale(
                branchId,
                selectedItemsToSell,
                addOns,
                year,
                month,
                day,
                total,
                payAmount,
                onSaleStatus
            )
        }
    }
}
