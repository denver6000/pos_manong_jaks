package com.denproj.posmanongjaks.repository.base

import androidx.lifecycle.LiveData
import com.denproj.posmanongjaks.model.Item
import com.denproj.posmanongjaks.model.ProductWrapper
import com.denproj.posmanongjaks.model.Sale
import com.denproj.posmanongjaks.model.SaleItem
import com.denproj.posmanongjaks.model.SaleProduct
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseSaleRepository.OnSaleStatus
import com.denproj.posmanongjaks.util.OnDataReceived

interface SaleRepository {

    fun processSale(
        branchId: String,
        selectedItemToSel: HashMap<Long, ProductWrapper>,
        addOns: HashMap<Item, Int>,
        year: Int,
        month: Int,
        day: Int,
        total: Double,
        amountToBePaid: Double,
        onSaleStatus: OnSaleStatus
    )

    fun getAllAddOnsWithSaleId(saleId: Int): LiveData<List<SaleItem>>
    fun getAllProductsWithSaleId(saleId: Int): LiveData<List<SaleProduct>>
    fun getAllRecordedSalesOnBranch(branchId: String, onDataFetched: OnDataReceived<List<Sale>>)
    suspend fun getAllSales(branchId: String): List<Sale>
}
