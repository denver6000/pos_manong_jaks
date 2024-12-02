package com.denproj.posmanongjaks.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PrintActivityViewmodel extends ViewModel {

    private MutableLiveData<String> stringToPrintLiveData = new MutableLiveData<>();

    @Inject
    public PrintActivityViewmodel() {
    }

    public MutableLiveData<String> getStringToPrintLiveData() {
        return stringToPrintLiveData;
    }

    public void setStringToPrint(String stringToPrint) {
        this.stringToPrintLiveData.setValue(stringToPrint);
    }
}
