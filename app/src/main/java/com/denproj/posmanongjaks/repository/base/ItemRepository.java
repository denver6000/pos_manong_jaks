package com.denproj.posmanongjaks.repository.base;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.util.OnDataReceived;

import java.util.HashMap;
import java.util.List;

public interface ItemRepository extends ImageRepository, RecipeRepository {

    void fetchItemsFromGlobal(OnDataReceived<List<Item>> onDataReceived);
    void fetchItemsFromBranch(OnDataReceived<List<Item>> onDataReceived);
    void saveSelectionToBranchList(HashMap<String, Item> selectedItemsMap, OnDataReceived<Void> onComplete);
    void insertItemsToBranch(List<Item> items, OnDataReceived<Void> onDataReceived);
}
