package com.denproj.posmanongjaks.repository.firestoreImpl

import co.yml.charts.common.extensions.isNotNull
import com.denproj.posmanongjaks.model.CompleteSaleInfo
import com.denproj.posmanongjaks.model.Item
import com.denproj.posmanongjaks.model.ItemNameAndAmountToReduce
import com.denproj.posmanongjaks.model.ProductWrapper
import com.denproj.posmanongjaks.model.Recipe
import com.denproj.posmanongjaks.model.Sale
import com.denproj.posmanongjaks.model.SaleItem
import com.denproj.posmanongjaks.model.SaleProduct
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseSaleRepository.OnSaleStatus
import com.denproj.posmanongjaks.util.OnDataReceived
import com.denproj.posmanongjaks.util.TimeUtil
import com.denproj.posmanongjaks.util.TimeUtil.Companion.getCurrentDate
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Random

class FirestoreSaleRepository {
    val firestore = FirebaseFirestore.getInstance()

    fun observeSales(branchId: String, onDataReceived: OnDataReceived<List<Sale>>) {
        val sales = firestore
            .collection("sales_record")
            .whereEqualTo("branchId", branchId)
            .whereEqualTo("date", getCurrentDate())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    onDataReceived.onFail(error)
                    return@addSnapshotListener
                }

                if (value == null || value.isEmpty) {
                    onDataReceived.onSuccess(emptyList())
                    return@addSnapshotListener
                }

                val salesList = ArrayList<Sale>()
                for (document in value.documents) {
                    val sale = document.toObject(Sale::class.java)
                    if (sale != null) {
                        salesList.add(sale)
                    }
                }
                onDataReceived.onSuccess(salesList)
            }
    }

    suspend fun processSale(branchId: String,
                    selectedProducts: HashMap<Long, ProductWrapper>,
                    selectedAddOns: HashMap<Item, Int>,
                    year: Int,
                    month: Int,
                    day: Int,
                    total: Double,
                    amountToBePaid: Double,
                    onSaleStatus: OnSaleStatus
    ) {
        val itemsAndReductionAmount = HashMap<Int, ItemNameAndAmountToReduce>()

        selectedAddOns.forEach { (item: Item?, integer: Int?) ->
            itemsAndReductionAmount[item.item_id] = ItemNameAndAmountToReduce(item.item_name!!, integer.toDouble())
        }

        selectedProducts.forEach {
            if (it.value.product.recipes == null) {
                return@forEach
            } else {
                val product = it.value.product
                val productAmt = it.value.addOnAmount
                product.recipes!!.forEach { (s: String?, recipe: Recipe?) ->
                    val recipeItemKey = s.toInt()
                    itemsAndReductionAmount.compute(recipeItemKey) { k: Int?, v: ItemNameAndAmountToReduce? ->
                        if ((v == null))
                            return@compute (ItemNameAndAmountToReduce(recipeItemKey.toString(), recipe.amount!! * productAmt.toDouble()))
                        else {
                            v.amountToReduce += recipe.amount!! * productAmt
                            v
                        }
                    }
                }
            }
        }

        val itemsFailed = HashMap<String?, String?>()
        val itemSnapshots = ArrayList<DocumentSnapshot>()
        itemsAndReductionAmount.forEach {
            val item = firestore.collection("branch_items").document(branchId).collection("items").whereEqualTo("item_id", it.key).get().await()
            if (item.isEmpty || item.documents.isEmpty()) {
                itemsFailed[it.value.itemName] = "does not exist"
                return@forEach
            }

            val currentStock = getCurrentStock(item.documents[0], it.key)

            if (currentStock < it.value.amountToReduce) {
                itemsFailed[it.value.itemName] = " does not have enough stock to meet the order. Current Stock is $currentStock and ${it.value.amountToReduce} is needed to fulfill order."
            }
            itemSnapshots.add(item.documents[0])
        }

        if (itemsFailed.isNotEmpty()) {
            onSaleStatus.failed(null, itemsFailed)
            return
        }


        itemsAndReductionAmount.forEach {
            val item = firestore.collection("branch_items").document(branchId).collection("items").whereEqualTo("item_id", it.key).get().await()
            reduceStock(branchId, item.documents[0], it.value.amountToReduce)
        }

        val saleRecord = Sale()
        saleRecord.saleId = generateId().toInt()
        saleRecord.total = total
        saleRecord.change = amountToBePaid - total
        saleRecord.branchId = branchId
        saleRecord.date = getCurrentDate()
        saleRecord.time = getCurrentTime()
        saleRecord.paidAmount = amountToBePaid
        val timeBreakdown = saleRecord.time!!.split(":")
        saleRecord.hour = timeBreakdown[0].toInt()
        saleRecord.minute = timeBreakdown[1].toInt()
        addSaleRecord(saleRecord, selectedAddOns, selectedProducts, onSaleStatus)


    }

    suspend fun addSaleRecord(sale: Sale, selectedAddOns: HashMap<Item, Int>, selectedProducts: HashMap<Long, ProductWrapper>, onSaleStatus: OnSaleStatus) {
        val saleRecordCollection = firestore.collection("sales_record")
        saleRecordCollection.add(sale).await()
        val soldItems = firestore.collection("sales_item")
        val soldProducts = firestore.collection("sales_product")
        val productsMap = ArrayList<SaleProduct>()
        val itemsMap = ArrayList<SaleItem>()

        selectedAddOns.forEach {
            val saleItem = SaleItem()
            saleItem.saleId = sale.saleId
            saleItem.item_id = it.key.item_id
            saleItem.itemName = it.key.item_name
            saleItem.amount = it.value
            soldItems.add(saleItem)
            itemsMap.add(saleItem)
        }

        selectedProducts.forEach {
            val saleProduct = SaleProduct()
            saleProduct.saleId = sale.saleId
            saleProduct.productId = it.value.product.product_id
            saleProduct.amount = it.value.addOnAmount
            saleProduct.name = it.value.product.product_name
            soldProducts.add(saleProduct)
            productsMap.add(saleProduct)
            insertToSaleStatistic(sale.branchId!!, saleProduct)
        }

        onSaleStatus.success(CompleteSaleInfo(sale, itemsMap, productsMap))

    }

    suspend fun reduceStock(branchId: String, itemSnapshot: DocumentSnapshot, amountToReduce: Double) {
        val currentStock = itemSnapshot.getDouble("item_quantity")
        val newStock = currentStock!! - amountToReduce
        itemSnapshot.reference.update("item_quantity", newStock)
        recordStockMovement(itemSnapshot, amountToReduce, branchId)
    }

     fun getCurrentStock(itemSnapshot: DocumentSnapshot, itemId: Int): Double {
        val item = itemSnapshot
        return item.get("item_quantity").toString().toDouble()
    }

    fun generateId(): String {
        val random = Random()
        val randomSixDigit = 100000 + random.nextInt(900000)
        return "10$randomSixDigit"
    }

    fun observeCurrentSaleStat(branchId: String, onDataReceived: OnDataReceived<HashMap<String, Int>>) {
        val saleStats = firestore.collection("sale_statistics")
            .whereEqualTo("branch_id", branchId)
            .whereEqualTo("sale_date", getCurrentDate()).addSnapshotListener { value, error ->
                if (error != null) {
                    onDataReceived.onFail(error)
                    return@addSnapshotListener
                }
                if (value?.isEmpty == null || value.documents.isEmpty()) {
                    onDataReceived.onSuccess(HashMap())
                    return@addSnapshotListener
                }
                val hashMap = HashMap<String, Int>()

                value.documents[0].reference.collection("products_sold").addSnapshotListener { productsSold, error ->
                    if (error != null) {
                        onDataReceived.onFail(error)
                        return@addSnapshotListener
                    }

                    if (productsSold?.isEmpty == null || productsSold.isEmpty)  {
                        onDataReceived.onSuccess(hashMap)
                        return@addSnapshotListener
                    }

                    productsSold.documents.forEach { docs ->
                        hashMap[docs.getString("product_name")!!] = docs.get("amount_sold").toString().toFloat().toInt()
                    }
                    onDataReceived.onSuccess(hashMap)
                }
            }
    }

    suspend fun recordStockMovement(item: DocumentSnapshot, amount: Double, branchId: String) {
        val stockRecordsCollection = firestore.collection("stock_record")
        var itemRecordOfBranchToday = stockRecordsCollection.whereEqualTo("sale_date", getCurrentDate()).whereEqualTo("branch_id", branchId).get().await()

        if (itemRecordOfBranchToday.isEmpty) {
            val newStockRecord = HashMap<String, Any>()
            newStockRecord["sale_time"] = getCurrentTime()
            val sale_time = getCurrentTime().split(":")
            newStockRecord["sale_hour"] = sale_time[0]
            newStockRecord["sale_minute"] = sale_time[1]
            newStockRecord["sale_date"] = getCurrentDate()
            newStockRecord["branch_id"] = branchId
            stockRecordsCollection.add(newStockRecord).await()
            itemRecordOfBranchToday = stockRecordsCollection.whereEqualTo("sale_date", getCurrentDate()).whereEqualTo("branch_id", branchId).get().await()
        }

        val soldItemsCollection = itemRecordOfBranchToday.documents[0].reference.collection("sold_items")
        val recordOfItem = soldItemsCollection.whereEqualTo("item_id", item.id.toInt()).get().await()
        if (recordOfItem.isEmpty) {
            val soldItems = itemRecordOfBranchToday.documents[0].reference.collection("sold_items")
            val newItemRecord = HashMap<String, Any>()
            newItemRecord["item_id"] = item.id.toInt()
            newItemRecord["total_reduced_stock_amount"] = amount
            newItemRecord["stock_at_start"] = getCurrentStock(item, item.id.toInt())
            soldItems.add(newItemRecord).await()
        } else {
            val itemTotalReducedStockAmount = recordOfItem.documents[0].get("total_reduced_stock_amount").toString().toDouble()
            recordOfItem.documents[0].reference.update("total_reduced_stock_amount", itemTotalReducedStockAmount + amount)
        }
    }

    private suspend fun insertToSaleStatistic(branchId: String, saleProduct: SaleProduct) {
        val saleStatsCollection = firestore.collection("sale_statistics")
        val saleStatistic = saleStatsCollection
            .whereEqualTo("branch_id", branchId)
            .whereEqualTo("sale_date", getCurrentDate()).get().await()

        if (saleStatistic.isEmpty) {
            val hash = HashMap<String, Any>()
            hash["branch_id"] = branchId
            hash["sale_date"] = getCurrentDate()
            val productSoldCollections = saleStatsCollection.document().collection("products_sold")
            val hashMap = HashMap<String, Any>()
            hashMap["product_id"] = saleProduct.productId!!
            hashMap["amount_sold"] = saleProduct.amount
            hashMap["product_name"] = saleProduct.name!!
            productSoldCollections.add(hashMap)
            saleStatsCollection.add(hash)
        } else {
            val statDocId = saleStatistic.documents[0].id
            val saleStatisticRef = saleStatsCollection.document(statDocId)
            val productSoldCollections = saleStatisticRef.collection("products_sold")
            val statSnapshot = productSoldCollections
                .whereEqualTo("product_id", saleProduct.productId).get().await()
            if (statSnapshot.isEmpty || statSnapshot.documents.isEmpty()) {
                val hash = HashMap<String, Any>()
                hash["product_id"] = saleProduct.productId!!
                hash["amount_sold"] = saleProduct.amount
                hash["product_name"] = saleProduct.name!!
                productSoldCollections.add(hash)
            } else {
                val statDocSnapshot = statSnapshot.documents[0]
                val amountSold = statDocSnapshot.get("amount_sold").toString().toDouble()
                statDocSnapshot.reference.update("amount_sold", amountSold + saleProduct.amount)
            }
        }

    }

    private fun getCurrentTime(): String {
        return TimeUtil.getCurrentTime()
    }

    private fun getCurrentDate(): String {
        return TimeUtil.getCurrentDate()
    }


}