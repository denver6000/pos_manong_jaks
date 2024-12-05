package com.denproj.posmanongjaks.repository.firestoreImpl

import androidx.lifecycle.MutableLiveData
import com.denproj.posmanongjaks.model.Item
import com.denproj.posmanongjaks.repository.base.ItemRepository
import com.denproj.posmanongjaks.util.OnDataReceived
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class FirestoreItemRepository : ItemRepository {
    override suspend fun observeItemsFromBranch(branchId: String, onDataFetched: OnDataReceived<List<Item>>) {
        val firestore = FirebaseFirestore.getInstance()

        val arrayList = ArrayList<Item>()
        firestore.collection("branch_items")
            .document(branchId)
            .collection("items").get().addOnSuccessListener { itemsCollection ->
            itemsCollection.documents.forEach {
                val item = it.toObject(Item::class.java)
                if (item != null) {
//                    val url = firebaseStorage.getReference(item.item_image_path!!).downloadUrl.addOnSuccessListener {
//
//                    }
//                    item.image_url = url
                    arrayList.add(item)
                }
            }
                onDataFetched.onSuccess(arrayList)
        }


//        firestore.collection("branch_items").whereEqualTo("branch_id", branchId).get().addOnSuccessListener {
//            val itemsRef = it.documents[0].reference.collection("items")
//
//            val globalItemCollection = firestore.collection("items_list")
//            itemsRef.addSnapshotListener { value, error ->
//                if (error != null) {
//                    return@addSnapshotListener
//                } else if (value != null) {
//                    val items: ArrayList<Item> = ArrayList()
//                    value.documents.forEach { document ->
//                        val itemId = document.get("item_id").toString().toInt()
//                        val itemPrice: Double = document.get("item_price").toString().toDouble()
//                        val itemQuantity = document.get("item_quantity").toString().toDouble()
//                        val isAdOn = document.get("ads_on").toString().toBoolean()
//
//                        globalItemCollection.whereEqualTo("item_id", itemId).get()
//                            .addOnSuccessListener { globalItem ->
//                                if (globalItem.documents.isNotEmpty())  {
//                                    val docOfGlobalItem = globalItem.documents[0]
//                                    val item = docOfGlobalItem.toObject(Item::class.java)
//                                    item?.item_price = itemPrice
//                                    item?.item_quantity = itemQuantity
//                                    item?.isAds_on = isAdOn
//
//                                    if (item != null) {
//                                        items.add(item)
//                                    }
//
//                                    itemsMutableLiveData.value = items
//                                }
//
//                            }
//                    }
//                    return@addSnapshotListener
//                }
//            }
//        }

    }
}