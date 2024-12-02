package com.denproj.posmanongjaks.repository.firestoreImpl

import com.denproj.posmanongjaks.model.Item
import com.denproj.posmanongjaks.model.Product
import com.denproj.posmanongjaks.util.OnDataReceived
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
class POSFirestoreRepositoryImpl : POSFirestoreRepository{

    val firestore = FirebaseFirestore.getInstance()

    override suspend fun getProductsOfBranch(
        branchId: String,
        onDataReceived: OnDataReceived<List<Product>>
    ) {
        val products = firestore.collection("branch_menus").document(branchId).collection("menus").get().await().documents
        val productList = mutableListOf<Product>()
        if (products.size != 0) {

            products.forEach {
                val product = it.toObject(Product::class.java)
                if (product != null) {
                    productList.add(product)
                }
            }

            onDataReceived.onSuccess(productList)
        } else {
            onDataReceived.onFail(Exception("No Products Found"))
        }
    }

    override suspend fun getAddOnsOfBranch(
        branchId: String,
        onDataReceived: OnDataReceived<List<Item>>
    ) {
        val items = firestore.collection(branchId).document(branchId).collection("items").get().await()
        val itemsList = ArrayList<Item>()
        items.forEach {
            val item = it.toObject(Item::class.java)
            itemsList.add(item)
        }
        onDataReceived.onSuccess(itemsList)
    }

    override suspend fun processSale() {
        TODO("Not yet implemented")
    }

}
