package com.denproj.posmanongjaks.repository.firebaseImpl

import com.denproj.posmanongjaks.model.Item
import com.denproj.posmanongjaks.repository.base.AddOnsRepository
import com.denproj.posmanongjaks.util.OnDataReceived
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseAddonRepository : AddOnsRepository {
    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    override suspend fun observeAddOnsList(
        branchId: String,
        onDataReceived: OnDataReceived<List<Item?>?>?
    ) {
        val databaseReference =
            firebaseDatabase.getReference(PATH_TO_ITEMS_ON_BRANCHES + "/" + branchId)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(items: DataSnapshot) {
                val addOns: MutableList<Item?> = ArrayList()
                for (itemSnapshot in items.children) {
                    val isAddOn = itemSnapshot.child("ads_on").getValue(
                        Boolean::class.java
                    )
                    if (isAddOn != null && isAddOn) {
                        val item = itemSnapshot.getValue(
                            Item::class.java
                        )
                        addOns.add(item)
                    }
                }
                onDataReceived?.onSuccess(addOns)
            }

            override fun onCancelled(error: DatabaseError) {
                onDataReceived?.onFail(error.toException())
            }
        })
    }

    companion object {
        const val PATH_TO_ITEMS_ON_BRANCHES: String = "items_on_branches"
    }
}
