package com.denproj.posmanongjaks.repository.base;

import androidx.lifecycle.MutableLiveData;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.util.OnFetchFailed;

import java.util.List;

import kotlin.NotImplementedError;

public interface AddOnsRepository {

    default MutableLiveData<List<Item>> observeAddOnsList(String branchId, OnFetchFailed onFetchFailed) {
        throw new NotImplementedError();
    }
}
