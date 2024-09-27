package com.denproj.posmanongjaks.repository.base;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.util.OnDataReceived;

import java.util.HashMap;
import java.util.List;

public interface ItemRepository {

    void fetchItemsFromGlobal(OnDataReceived<List<Item>> onDataReceived);
    void fetchItemsFromBranch(String branchId, OnDataReceived<List<Item>> onDataReceived);

    void saveSelectionToBranchList(String branchId, HashMap<Integer, Item> selectedItemsMap, OnDataReceived<Void> onComplete);


}
