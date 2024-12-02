package com.denproj.posmanongjaks.repository.firestoreImpl

import com.denproj.posmanongjaks.model.Item
import com.denproj.posmanongjaks.repository.base.AddOnsRepository
import com.denproj.posmanongjaks.util.OnDataReceived
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreAddOnRepository: AddOnsRepository {

    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun observeAddOnsList(
        branchId: String,
        onDataReceived: OnDataReceived<List<Item?>?>?
    ) {
        val branchItemsCollection = firestore
            .collection("branch_items")
            .document(branchId)
            .collection("items")
        val branchItems = branchItemsCollection
        val ads_on = branchItems.whereEqualTo("ads_on", true)
            .get()
            .await()
        val addOns = ArrayList<Item>()
        ads_on.documents.forEach { ad_on ->
            val itemObj = ad_on.toObject(Item::class.java)
            if (itemObj != null) {
                addOns.add(itemObj)
            }
        }
        onDataReceived?.onSuccess(addOns)
    }
}