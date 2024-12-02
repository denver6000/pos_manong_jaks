package com.denproj.posmanongjaks.repository.firestoreImpl

import com.denproj.posmanongjaks.model.ItemStockRecord
import com.denproj.posmanongjaks.repository.base.StockReportRepository
import com.denproj.posmanongjaks.util.OnDataReceived
import com.denproj.posmanongjaks.util.TimeUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class FirestoreStockReportRepository : StockReportRepository {

    val firestore = FirebaseFirestore.getInstance()

    override suspend fun getReportsOfBranch(branchId: String, onDataReceived: OnDataReceived<List<ItemStockRecord>>) {
        val branchStockReport =
            firestore
                .collection("stock_record")
                .whereEqualTo("branch_id", branchId)
                .whereEqualTo("sale_date", TimeUtil.getCurrentDate())
                .get().await()

        if (branchStockReport.isEmpty) {
            onDataReceived.onSuccess(emptyList())
            return
        }

        val stockRecordsCollection = branchStockReport.documents[0].reference.collection("sold_items")
        val stockRecords = stockRecordsCollection.get().await()

        if (stockRecords.isEmpty) {
            onDataReceived.onSuccess(emptyList())
            return
        }

        val itemStockRecords = ArrayList<ItemStockRecord>()
        val storage = FirebaseStorage.getInstance()
        val itemCollection = firestore.collection("items_list")
        stockRecords.documents.forEach {

            val stockRecordObj = it.toObject(ItemStockRecord::class.java)


            if (stockRecordObj != null) {
                val matchedItems = itemCollection.whereEqualTo("item_id", stockRecordObj.item_id).get().await()
                if (matchedItems.documents.isNotEmpty()) {
                    val imagePath = matchedItems.documents[0].getString("item_image_path")
                    if (imagePath != null) {
                        val url = storage.getReference(imagePath).downloadUrl.await()
                        stockRecordObj.imageUri = url
                    }

                }
                itemStockRecords.add(stockRecordObj)
            }
        }

        onDataReceived.onSuccess(itemStockRecords)
    }
}