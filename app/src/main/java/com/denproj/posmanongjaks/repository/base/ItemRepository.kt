package com.denproj.posmanongjaks.repository.base

import androidx.lifecycle.LiveData
import com.denproj.posmanongjaks.model.Item
import com.denproj.posmanongjaks.util.OnDataReceived

interface ItemRepository {
    suspend fun observeItemsFromBranch(
        branchId: String,
        onDataFetched: OnDataReceived<List<Item>>
    )

}
