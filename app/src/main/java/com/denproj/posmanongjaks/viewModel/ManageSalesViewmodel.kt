package com.denproj.posmanongjaks.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.denproj.posmanongjaks.hilt.qualifier.FirebaseImpl
import com.denproj.posmanongjaks.model.SaleItem
import com.denproj.posmanongjaks.model.SaleProduct
import com.denproj.posmanongjaks.repository.base.SaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ManageSalesViewmodel @Inject constructor(@param:FirebaseImpl var saleRepository: SaleRepository) :
    ViewModel() {
    fun getSoldProductsAsync(saleId: Int): LiveData<List<SaleProduct>> {
        return saleRepository.getAllProductsWithSaleId(saleId)
    }

    fun getSoldAddOnsAsync(saleId: Int): LiveData<List<SaleItem>> {
        return saleRepository.getAllAddOnsWithSaleId(saleId)
    }
}
