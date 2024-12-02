package com.denproj.posmanongjaks.repository.base

import com.denproj.posmanongjaks.model.Item
import com.denproj.posmanongjaks.util.OnDataReceived
import com.google.firebase.firestore.FirebaseFirestore

interface AddOnsRepository {
    suspend fun observeAddOnsList(branchId: String, onDataReceived: OnDataReceived<List<Item?>?>?) {
        throw NotImplementedError()
    }

    fun observeAddOnsList(branch: String, onListFetched: (addOnsList: List<Item>) -> Unit) {
        val firestore = FirebaseFirestore.getInstance()
        val addOns = firestore.collection("branch_items").document(branch)
    }
}
