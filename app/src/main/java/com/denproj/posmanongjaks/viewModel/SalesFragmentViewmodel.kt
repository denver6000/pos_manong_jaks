package com.denproj.posmanongjaks.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denproj.posmanongjaks.hilt.qualifier.FirebaseImpl
import com.denproj.posmanongjaks.hilt.qualifier.FirestoreImpl
import com.denproj.posmanongjaks.model.Item
import com.denproj.posmanongjaks.model.Product
import com.denproj.posmanongjaks.repository.base.AddOnsRepository
import com.denproj.posmanongjaks.repository.base.ProductRepository
import com.denproj.posmanongjaks.repository.impl.SessionRepository
import com.denproj.posmanongjaks.util.OnDataReceived
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalesFragmentViewmodel @Inject constructor(
    @FirestoreImpl var productRepository: ProductRepository,
    @FirestoreImpl var addOnsRepository: AddOnsRepository,
    var sessionRepository: SessionRepository
) : ViewModel() {

    private val _items: MutableLiveData<List<Item?>?> = MutableLiveData(emptyList())
    val items: LiveData<List<Item?>?> = _items

    private val _products: MutableLiveData<List<Product>> = MutableLiveData(null)
    val products: LiveData<List<Product>> = _products

    private val _product: MutableLiveData<Product> = MutableLiveData(null)
    val product: LiveData<Product> = _product

    private val _errors: MutableLiveData<Exception> = MutableLiveData(null)
    val errors: LiveData<Exception> = _errors


    init {
        viewModelScope.launch {
            var sessionSimple = sessionRepository.getSession()
            if (sessionSimple != null) {
                addOnsRepository.observeAddOnsList(sessionSimple.branchId!!, object : OnDataReceived<List<Item?>?> {
                    override fun onSuccess(data: List<Item?>?) {
                        if (data != null) {
                            _items.postValue(data)
                        }
                    }

                    override fun onFail(e: Exception?) {
                        _errors.postValue(e ?: Exception("An error occurred when loading add ons."))
                    }
                })
                productRepository.observeProductList(sessionSimple.branchId, object : OnDataReceived<List<Product>?> {
                    override fun onSuccess(data: List<Product>?) {
                        _products.postValue(data ?: emptyList())
                    }

                    override fun onFail(e: Exception?) {
                        _errors.postValue(e ?: Exception("An error occurred when loading products ons."))
                    }
                })
            }
        }
    }
}
