package com.denproj.posmanongjaks.repository.base;

import androidx.lifecycle.MutableLiveData;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.util.OnFetchFailed;

import java.util.List;

import kotlin.NotImplementedError;

public interface ItemRepository{

    default MutableLiveData<List<Item>> observeItemsFromBranch(String branchId, OnFetchFailed onFetchFailed) {
        throw new NotImplementedError();
    }
}
