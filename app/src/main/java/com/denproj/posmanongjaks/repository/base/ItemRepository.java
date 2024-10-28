package com.denproj.posmanongjaks.repository.base;

import androidx.lifecycle.MutableLiveData;

import com.denproj.posmanongjaks.model.Branch;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.util.OnFetchFailed;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import kotlin.NotImplementedError;

public interface ItemRepository extends RecipeRepository {

    default MutableLiveData<List<Item>> observeItemsFromBranch(String branchId, OnFetchFailed onFetchFailed) {
        throw new NotImplementedError();
    }

    CompletableFuture<List<Item>> fetchItemsFromBranch(String branchId);
    CompletableFuture<Void> insertItem(List<Item> items);
    default CompletableFuture<Void> clearItems() {
        throw new NotImplementedError();
    }
}
