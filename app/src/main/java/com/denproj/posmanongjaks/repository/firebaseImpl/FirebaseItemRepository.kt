package com.denproj.posmanongjaks.repository.firebaseImpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denproj.posmanongjaks.model.Item
import com.denproj.posmanongjaks.repository.base.ItemRepository
import com.denproj.posmanongjaks.util.OnDataReceived
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseItemRepository : ItemRepository {
    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    override suspend fun observeItemsFromBranch(branchId: String, onDataFetched: OnDataReceived<List<Item>>){
//        val itemsMutableLiveData: MutableLiveData<List<Item>> = MutableLiveData(emptyList())
//        val itemsOnBranches = firebaseDatabase.getReference(PATH_TO_ITEMS_LIST + "/" + branchId)
//        itemsOnBranches.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(itemsList: DataSnapshot) {
//                val items: ArrayList<Item> = ArrayList()
//                for (itemSnapshot in itemsList.children) {
//                    val item = itemSnapshot.getValue(
//                        Item::class.java
//                    )
//                    if (item != null) {
//                        items.add(item)
//                    }
//                }
//                itemsMutableLiveData.value = (items)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                itemsMutableLiveData.value = emptyList()
//            }
//        })
    }
    companion object {
        const val PATH_TO_ITEMS_LIST: String = "items_on_branches"
    }
}
