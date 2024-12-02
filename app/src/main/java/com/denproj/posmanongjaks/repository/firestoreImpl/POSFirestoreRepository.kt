package com.denproj.posmanongjaks.repository.firestoreImpl

import com.denproj.posmanongjaks.model.Item
import com.denproj.posmanongjaks.model.Product
import com.denproj.posmanongjaks.util.OnDataReceived

interface POSFirestoreRepository {

    suspend fun getProductsOfBranch(branchId: String, onDataReceived: OnDataReceived<List<Product>>)
    suspend fun getAddOnsOfBranch(branchId: String, onDataReceived: OnDataReceived<List<Item>>)
    suspend fun processSale()

}