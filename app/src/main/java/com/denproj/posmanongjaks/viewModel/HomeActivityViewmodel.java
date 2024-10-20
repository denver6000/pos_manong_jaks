package com.denproj.posmanongjaks.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeActivityViewmodel extends ViewModel {

    public MutableLiveData<String> branchIdLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isConnectionReachableLiveData = new MutableLiveData<>();

    public MutableLiveData<String> getBranchIdLiveData() {
        return branchIdLiveData;
    }

    public MutableLiveData<Boolean> getIsConnectionReachableLiveData() {
        return isConnectionReachableLiveData;
    }

}
