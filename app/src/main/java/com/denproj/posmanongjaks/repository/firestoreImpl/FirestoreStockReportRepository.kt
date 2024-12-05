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

    override suspend fun getReportsOfBranch(
        branchId: String,
        onDataReceived: OnDataReceived<List<ItemStockRecord>>
    ) {
        firestore
            .collection("stock_record")
            .whereEqualTo("branch_id", branchId)
            .whereEqualTo("sale_date", TimeUtil.getCurrentDate())
            .addSnapshotListener { branchStockReport, error ->

                if (branchStockReport == null) {
                    onDataReceived.onSuccess(emptyList())
                    return@addSnapshotListener
                }

                if (branchStockReport!!.isEmpty) {
                    onDataReceived.onSuccess(emptyList())
                    return@addSnapshotListener
                }

                val stockRecordsCollection =
                    branchStockReport.documents[0].reference.collection("sold_items")
                val stockRecords =
                    stockRecordsCollection.addSnapshotListener { stockRecords, error ->

                        if (stockRecords == null) {
                            onDataReceived.onSuccess(emptyList())
                            return@addSnapshotListener
                        }
                        if (stockRecords.isEmpty) {
                            onDataReceived.onSuccess(emptyList())
                            return@addSnapshotListener
                        }

                        val itemStockRecords = ArrayList<ItemStockRecord>()
                        val storage = FirebaseStorage.getInstance()
                        val itemCollection = firestore.collection("items_list")
                        stockRecords.documents.forEach {
                            val stockRecordObj = it.toObject(ItemStockRecord::class.java)


                            if (stockRecordObj != null) {
                                itemStockRecords.add(stockRecordObj)
                            }
                        }
                        onDataReceived.onSuccess(itemStockRecords)
                    }
            }
    }
}