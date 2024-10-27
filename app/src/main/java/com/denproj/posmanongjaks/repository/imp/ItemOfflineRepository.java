package com.denproj.posmanongjaks.repository.imp;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.repository.base.ItemRepository;
import com.denproj.posmanongjaks.room.dao.ItemsDao;
import com.denproj.posmanongjaks.util.OnDataReceived;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.inject.Inject;

import kotlin.NotImplementedError;

public class ItemOfflineRepository implements ItemRepository {

    ItemsDao itemsDao;

    @Inject
    public ItemOfflineRepository(ItemsDao itemsDao) {
        this.itemsDao = itemsDao;
    }

    @Override
    public void fetchItemsFromGlobal(OnDataReceived<List<Item>> onDataReceived) {

    }

    @Override
    public CompletableFuture<List<Item>> fetchItemsFromBranch(String branchId) {
        return CompletableFuture.supplyAsync(() -> itemsDao.getAllItems());
    }

    @Override
    public void saveSelectionToBranchList(String branchId, HashMap<String, Item> selectedItemsMap, OnDataReceived<Void> onComplete) {
        throw new NotImplementedError();
    }

    @Override
    public CompletableFuture<Void> insertItem(List<Item> items) {
        return CompletableFuture.supplyAsync(() -> {
            items.forEach(item -> itemsDao.insertItems(item));
            return null;
        });
    }

    @Override
    public void insertRecipeToBranch(String branchId, String itemId, OnDataReceived<Void> onDataReceived) {
        throw new NotImplementedError();
    }

    @Override
    public CompletableFuture<Void> clearItems() {
        return CompletableFuture.supplyAsync(() -> {
            itemsDao.clearItems();
            return null;
        });
    }


}
