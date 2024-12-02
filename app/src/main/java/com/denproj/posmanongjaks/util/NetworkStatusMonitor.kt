package com.denproj.posmanongjaks.util

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow

class NetworkStatusMonitor {
    private val database = FirebaseDatabase.getInstance()

    private val _isConnected = MutableStateFlow(false)
    public val isConnected = _isConnected

    init {
        val connectedRef = database.getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _isConnected.value = snapshot.getValue(Boolean::class.java) ?: false
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

}