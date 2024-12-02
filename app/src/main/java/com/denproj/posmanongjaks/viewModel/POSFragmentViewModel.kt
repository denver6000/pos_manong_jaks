package com.denproj.posmanongjaks.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denproj.posmanongjaks.model.Item
import com.denproj.posmanongjaks.model.Product
import com.denproj.posmanongjaks.repository.impl.SessionRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class POSFragmentViewModel @Inject constructor(val sessionRepository: SessionRepository) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _productsList: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    val productList: StateFlow<List<Product>> = _productsList

    private val _addOnsList: MutableStateFlow<List<Item>> = MutableStateFlow(emptyList())
    val addOnsList: StateFlow<List<Item>> = _addOnsList

    init {
        viewModelScope.launch {
            val session = sessionRepository.getSession()
            if (session?.branchId != null) {
                observeProducts(session.branchId)
                observeAddOns(session.branchId)
            }
        }
    }

    private fun observeProducts(branchId: String) {
        val productsCollection = firestore
            .collection("branch_menus")
            .document(branchId)
            .collection("menus")

        productsCollection.addSnapshotListener { snapshot, error ->
            if (snapshot != null || error == null) {
                _productsList.update { (snapshot!!.toObjects(Product::class.java)) }
            }
        }
    }

    private fun observeAddOns(branchId: String) {
        val addOnsCollection = firestore
            .collection("branch_items")
            .document(branchId)
            .collection("items")

        addOnsCollection.addSnapshotListener { value, error ->
            if (error != null && value?.documents != null) {
                val productsList = value.toObjects(Item::class.java)
                _addOnsList.update { productsList.filter { it.isAds_on } }
            }
        }
    }



}