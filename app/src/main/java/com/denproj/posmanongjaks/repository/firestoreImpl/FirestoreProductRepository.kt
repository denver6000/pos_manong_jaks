package com.denproj.posmanongjaks.repository.firestoreImpl

import com.denproj.posmanongjaks.model.Product
import com.denproj.posmanongjaks.repository.base.ProductRepository
import com.denproj.posmanongjaks.util.OnDataReceived
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FirestoreProductRepository : ProductRepository {
    override fun observeProductList(
        branchId: String?,
        onDataReceived: OnDataReceived<List<Product>?>
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()

        firestore
            .collection("branch_menus")
            .document(branchId!!)
            .collection("menus")
            .get().addOnSuccessListener {
                if (it.documents.isEmpty()) {
                    onDataReceived.onSuccess(emptyList())
                    return@addOnSuccessListener
                }
                val productsList = ArrayList<Product>()
                it.documents.forEach { prodSnapShot ->
                    try {
                        val product = prodSnapShot.toObject(Product::class.java)
                        if (product != null) {
                            storage.getReference(product.product_image_path!!).downloadUrl.addOnSuccessListener {
                                    uri ->
                                product.uri = uri
                                onDataReceived.onSuccess(productsList)
                            }
                            productsList.add(product)
                        }
                    } catch (e: RuntimeException) {
                        onDataReceived.onFail(Exception("A product is misconfigured: ${prodSnapShot.get("product_name")}"))
                    }

                }
                onDataReceived.onSuccess(productsList)
            }


//        firestore
//            .collection("branch_menus")
//            .document(branchId!!)
//            .collection("menus").addSnapshotListener { value, error ->
//                if (error != null) {
//                    onDataReceived?.onFail(error)
//                    return@addSnapshotListener
//                }
//                if (value != null) {
//                    val products: ArrayList<Product> = ArrayList()
//                    if (value.documents.isNotEmpty()) {
//                        value.documents.forEach {
//                            try {
//                                val product = it.toObject(Product::class.java)
//                                if (product != null) {
//                                    products.add(product)
//                                    storage.getReference(product.product_image_path!!).downloadUrl.addOnSuccessListener {
//                                            uri ->
//                                        product.uri = uri
//                                        onDataReceived?.onSuccess(products)
//                                    }
//                                }
//                            } catch (e: RuntimeException) {
//                                onDataReceived.onFail(Exception("An error occurred when loading a product."))
//                            }
//                        }
//                    } else {
//                        onDataReceived?.onSuccess(emptyList())
//                    }
//                }
//            }
    }
}