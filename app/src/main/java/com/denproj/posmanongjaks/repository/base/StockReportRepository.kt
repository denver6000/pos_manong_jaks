package com.denproj.posmanongjaks.repository.base

import com.denproj.posmanongjaks.model.ItemStockRecord
import com.denproj.posmanongjaks.util.OnDataReceived

interface StockReportRepository {

    suspend fun getReportsOfBranch(branchId: String, onDataReceived: OnDataReceived<List<ItemStockRecord>>)
}