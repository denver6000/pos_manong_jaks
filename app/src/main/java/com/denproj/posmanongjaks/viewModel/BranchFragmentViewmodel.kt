package com.denproj.posmanongjaks.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denproj.posmanongjaks.hilt.qualifier.FirestoreImpl
import com.denproj.posmanongjaks.model.Item
import com.denproj.posmanongjaks.repository.base.ItemRepository
import com.denproj.posmanongjaks.repository.impl.SessionRepository
import com.denproj.posmanongjaks.util.OnDataReceived
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class BranchFragmentViewmodel @Inject constructor(@FirestoreImpl var itemRepository: ItemRepository, val sessionRepository: SessionRepository) : ViewModel() {

    private val _stocks: MutableState<List<Item>> = mutableStateOf(emptyList())
    val stocks: State<List<Item>> = _stocks

    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _isRefreshing: MutableState<Boolean> = mutableStateOf(false)
    val isRefreshing = _isRefreshing

    init {
        getStocks(null)
    }

    fun getStocks(onFinished: (() -> Unit)?) {
        viewModelScope.launch {
            val session = sessionRepository.getSession()
            if (session != null) {
                itemRepository.observeItemsFromBranch(
                    session.branchId!!,
                    object : OnDataReceived<List<Item>> {
                        override fun onSuccess(result: List<Item>?) {
                            if (result != null) {
                                _stocks.value = result
                            }
                            _isLoading.value = false
                            onFinished?.invoke()
                        }

                        override fun onFail(e: Exception?) {
                            _isLoading.value = false
                            onFinished?.invoke()
                        }
                    })
            }
        }
    }

}


