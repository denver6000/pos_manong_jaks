package com.denproj.posmanongjaks.repository.base;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.util.OnDataReceived;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import kotlin.NotImplementedError;

public interface ItemRepository extends RecipeRepository {

    void fetchItemsFromGlobal(OnDataReceived<List<Item>> onDataReceived);
    CompletableFuture<List<Item>> fetchItemsFromBranch(String branchId);
    void saveSelectionToBranchList(String branchId, HashMap<String, Item> selectedItemsMap, OnDataReceived<Void> onComplete);
    CompletableFuture<Void> insertItem(List<Item> items);
    default CompletableFuture<Void> clearItems() {
        throw new NotImplementedError();
    }
}
