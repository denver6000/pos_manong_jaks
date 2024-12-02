package com.denproj.posmanongjaks.repository.firebaseImpl

import androidx.lifecycle.LiveData
import com.denproj.posmanongjaks.model.CompleteSaleInfo
import com.denproj.posmanongjaks.model.Item
import com.denproj.posmanongjaks.model.ProductWrapper
import com.denproj.posmanongjaks.model.Recipe
import com.denproj.posmanongjaks.model.Sale
import com.denproj.posmanongjaks.model.Sale.Companion.generateId
import com.denproj.posmanongjaks.model.SaleItem
import com.denproj.posmanongjaks.model.SaleProduct
import com.denproj.posmanongjaks.repository.base.SaleRepository
import com.denproj.posmanongjaks.util.OnDataReceived
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import kotlinx.coroutines.tasks.await
import java.util.Random

class FirebaseSaleRepository : SaleRepository {
    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    override fun processSale(
        branchId: String,
        selectedProducts: HashMap<Long, ProductWrapper>,
        selectedAddOns: HashMap<Item, Int>,
        year: Int,
        month: Int,
        day: Int,
        total: Double,
        amountToBePaid: Double,
        onSaleStatus: OnSaleStatus
    ) {
        validateSale(branchId, selectedProducts, selectedAddOns, object : OnSaleValidation {
            override fun canProceed(itemsToReduceStock: Map<String?, Any?>) {
                reduceSaleStock(branchId, itemsToReduceStock, object : StockReductionStatus {
                    override fun onSuccess() {
                        val saleRecord = Sale()
                        saleRecord.saleId = generateId().toInt()
                        val change = amountToBePaid - total
                        saleRecord.change = change
                        saleRecord.total = total
                        saleRecord.paidAmount = amountToBePaid
                        saleRecord.branchId = branchId


                        insertSale(saleRecord, selectedProducts, selectedAddOns, onSaleStatus)
                    }

                    override fun onFailed(e: Exception?) {
                        onSaleStatus.failed(e, HashMap())
                    }
                })
            }

            override fun cannotProceed(itemsWithErrors: HashMap<String?, String?>?) {
                onSaleStatus.failed(null, itemsWithErrors)
            }
        })
    }

    fun validateSale(
        branchId: String,
        selectedProducts: HashMap<Long, ProductWrapper>,
        selectedAddOns: HashMap<Item, Int>,
        saleValidation: OnSaleValidation
    ) {
        val idAndAmountToReduce = HashMap<Int, Int>()
        val itemsFailed = HashMap<String?, String?>()
        val batchRequest: MutableMap<String?, Any?> = HashMap()

        selectedProducts.forEach { (aLong: Long?, productWrapper: ProductWrapper?) ->
            productWrapper.product.recipes!!.forEach { (s: String?, recipe: Recipe?) ->
                val recipeItemKey = s.toInt()
                idAndAmountToReduce.compute(recipeItemKey) { k: Int?, v: Int? -> if ((v == null)) (recipe.amount!! * productWrapper.addOnAmount) else v + recipe.amount!! }
            }
        }

        selectedAddOns.forEach { (item: Item?, integer: Int?) ->
            idAndAmountToReduce.compute(item.item_id) { k: Int?, v: Int? -> if ((v == null)) integer else v + integer }
        }

        firebaseDatabase
            .getReference(FirebaseItemRepository.PATH_TO_ITEMS_LIST + "/" + branchId)
            .get()
            .addOnSuccessListener { items: DataSnapshot ->
                for ((key, amountToReduce) in idAndAmountToReduce) {
                    if (!items.hasChild(key.toString())) {
                        itemsFailed[key.toString() + ""] =
                            " does not exist in your branch. Contact Branch Manager to configure."
                        continue
                    }
                    val itemName = items.child("$key/item_name").getValue(
                        String::class.java
                    )

                    val itemQuantitySnapshot = items.child("$key/item_quantity")
                    val itemQuantityVal =
                        itemQuantitySnapshot.getValue(Int::class.java)

                    if (!itemQuantitySnapshot.exists() || itemQuantityVal == null) {
                        itemsFailed[itemName] = " [Item Quantity] is not configured correctly."
                        continue
                    }

                    if (itemQuantityVal < amountToReduce) {
                        val discrepancy = amountToReduce - itemQuantityVal
                        itemsFailed[itemName] =
                            " Item Quantity (Stock) is not enough to fulfil order. $discrepancy is needed."
                        continue
                    }

                    batchRequest["$key/item_quantity"] = itemQuantityVal - amountToReduce
                }
                if (itemsFailed.isEmpty()) {
                    saleValidation.canProceed(batchRequest)
                } else {
                    saleValidation.cannotProceed(itemsFailed)
                }
            }.addOnFailureListener { e: Exception? -> saleValidation.cannotProceed(HashMap()) }
    }

    fun reduceSaleStock(
        branchId: String,
        pathAndAmountToReduce: Map<String?, Any?>,
        stockReductionStatus: StockReductionStatus
    ) {
        val itemsRef =
            firebaseDatabase.getReference(FirebaseItemRepository.PATH_TO_ITEMS_LIST + "/" + branchId)
        itemsRef.updateChildren(pathAndAmountToReduce)
            .addOnFailureListener { e: Exception? -> stockReductionStatus.onFailed(e) }
        stockReductionStatus.onSuccess()
    }

    fun insertSale(
        sale: Sale,
        selectedProducts: HashMap<Long, ProductWrapper>,
        selectedAddOns: HashMap<Item, Int>,
        onSaleStatus: OnSaleStatus
    ) {
        val saleItems: MutableList<SaleItem> = ArrayList()
        val saleProducts: MutableList<SaleProduct> = ArrayList()
        selectedProducts.forEach { (aLong: Long?, productWrapper: ProductWrapper?) ->
            saleProducts.add(
                SaleProduct(
                    productWrapper.product.product_id,
                    productWrapper.addOnAmount,
                    sale.saleId,
                    productWrapper.product.product_name
                )
            )
        }
        selectedAddOns.forEach { (item: Item?, integer: Int?) ->
            saleItems.add(SaleItem(item.item_id, sale.saleId, integer, item.item_name))
        }
        onSaleStatus.success(CompleteSaleInfo(sale, saleItems, saleProducts))
        firebaseDatabase.getReference(PATH_TO_SALE_RECORD + "/" + sale.branchId + "/" + sale.saleId)
            .setValue(sale)
    }

    override fun getAllAddOnsWithSaleId(saleId: Int): LiveData<List<SaleItem>> {
        throw NotImplementedError()
    }

    override fun getAllProductsWithSaleId(saleId: Int): LiveData<List<SaleProduct>> {
        throw NotImplementedError()
    }

    override fun getAllRecordedSalesOnBranch(branchId: String, onDataFetched: OnDataReceived<List<Sale>>) {
        firebaseDatabase.getReference("$PATH_TO_SALE_RECORD/$branchId").get()
            .addOnSuccessListener { dataSnapshot: DataSnapshot ->
                val salesList: ArrayList<Sale> = ArrayList()
                for (child in dataSnapshot.children) {
                    val sale = child.getValue(Sale::class.java)
                    if (sale != null) {
                        salesList.add(sale)
                    }
                }
                onDataFetched.onSuccess(salesList)
            }
    }

    override suspend fun getAllSales(branchId: String): List<Sale> {
        return try {
            val salesRef = firebaseDatabase.getReference("$PATH_TO_SALE_RECORD/$branchId");
            val snapshot = salesRef.get().await()

            val salesList: ArrayList<Sale> = ArrayList()

            snapshot.children.forEach {
                val sale = it.getValue<Sale>()
                salesList.add(sale!!)
            }

            salesList
        } catch (e: Exception) {
            emptyList()
        }
    }



    interface OnSaleValidation {
        fun canProceed(itemsToReduceStock: Map<String?, Any?>)
        fun cannotProceed(itemsWithErrors: HashMap<String?, String?>?)
    }

    interface OnSaleStatus {
        fun success(completeSaleInfo: CompleteSaleInfo?)
        fun failed(fatalExceptions: Exception?, itemNameAndError: HashMap<String?, String?>?)
    }

    interface StockReductionStatus {
        fun onSuccess()
        fun onFailed(e: Exception?)
    }

    companion object {
        const val PATH_TO_SALE_RECORD: String = "sales_record"
    }
}
