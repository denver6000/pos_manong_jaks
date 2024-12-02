package com.denproj.posmanongjaks.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrintActivityViewmodel @Inject constructor() : ViewModel() {
    val stringToPrintLiveData: MutableLiveData<String> = MutableLiveData()

    fun setStringToPrint(stringToPrint: String) {
        stringToPrintLiveData.value = stringToPrint
    }
}
