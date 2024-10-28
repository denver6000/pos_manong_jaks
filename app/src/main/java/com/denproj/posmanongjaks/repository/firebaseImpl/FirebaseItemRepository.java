package com.denproj.posmanongjaks.repository.firebaseImpl;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.denproj.posmanongjaks.model.Branch;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.repository.base.ItemRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.util.OnFetchFailed;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import kotlin.NotImplementedError;

public class FirebaseItemRepository implements ItemRepository {

    public static final String PATH_TO_ITEMS_LIST = "items_on_branches";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    public MutableLiveData<List<Item>> observeItemsFromBranch(String branchId, OnFetchFailed onFetchFailed) {
        MutableLiveData<List<Item>> branchMutableLiveData = new MutableLiveData<>();

        DatabaseReference itemsOnBranches = firebaseDatabase.getReference(PATH_TO_ITEMS_LIST + "/" + branchId);
        itemsOnBranches.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot itemsList) {
                List<Item> items = new ArrayList<>();
                for (DataSnapshot itemSnapshot : itemsList.getChildren()) {
                    Item item = itemSnapshot.getValue(Item.class);
                    items.add(item);
                }
                branchMutableLiveData.setValue(items);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onFetchFailed.onFetchFailed(error.toException());
            }
        });

        return branchMutableLiveData;
    }

    @Override
    public CompletableFuture<List<Item>> fetchItemsFromBranch(String branchId) {
        return null;
    }

    @Override
    public CompletableFuture<Void> insertItem(List<Item> items) {
        return null;
    }

    @Override
    public void insertRecipeToBranch(String branchId, String itemId, OnDataReceived<Void> onDataReceived) throws Exception {

    }


}
