package com.denproj.posmanongjaks.repository.imp;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.repository.base.ItemRepository;
import com.denproj.posmanongjaks.room.dao.ItemsDao;
import com.denproj.posmanongjaks.util.AsyncRunner;
import com.denproj.posmanongjaks.util.OnDataReceived;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class ItemOfflineRepository implements ItemRepository {

    ItemsDao itemsDao;

    @Inject
    public ItemOfflineRepository(ItemsDao itemsDao) {
        this.itemsDao = itemsDao;
    }

    @Override
    public void getImage(String path, OnDataReceived<String> onDataReceived) {

    }

    @Override
    public void fetchItemsFromGlobal(OnDataReceived<List<Item>> onDataReceived) {

    }

    @Override
    public void fetchItemsFromBranch(OnDataReceived<List<Item>> onDataReceived) {
        AsyncRunner.runAsync(new AsyncRunner.Runner<List<Item>>() {
            @Override
            public List<Item> onBackground() {
                return itemsDao.getAllItems();
            }

            @Override
            public void onFinished(List<Item> result) {
                onDataReceived.onSuccess(result);
            }

            @Override
            public void onUI(List<Item> result) {

            }

            @Override
            public void onError(Exception e) {
                onDataReceived.onFail(e);
            }
        });
    }

    @Override
    public void saveSelectionToBranchList(HashMap<String, Item> selectedItemsMap, OnDataReceived<Void> onComplete) {

    }

    @Override
    public void insertItemsToBranch(List<Item> items, OnDataReceived<Void> onDataReceived) {
        AsyncRunner.runAsync(new AsyncRunner.Runner<Void>() {
            @Override
            public Void onBackground() {
                items.forEach(item -> {
                    itemsDao.insertItems(item);
                });
                return null;
            }

            @Override
            public void onFinished(Void result) {

            }

            @Override
            public void onUI(Void result) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public void insertRecipeToBranch(String branchId, String itemId, OnDataReceived<Void> onDataReceived) {

    }
}
