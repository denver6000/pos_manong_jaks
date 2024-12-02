package com.denproj.posmanongjaks.repository.base

import com.denproj.posmanongjaks.model.Product
import com.denproj.posmanongjaks.util.OnDataReceived

interface ProductRepository {
    fun observeProductList(branchId: String?, onDataReceived: OnDataReceived<List<Product>?>) {
        throw NotImplementedError()
    }


}
