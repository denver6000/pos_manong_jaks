package com.denproj.posmanongjaks.repository.imp;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.denproj.posmanongjaks.model.Branch;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.repository.base.ItemRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import kotlin.NotImplementedError;

public class ItemRepositoryImpl implements ItemRepository {
    public static final String GLOBAL_PATH_TO_ITEM_LIST = "items_list";


    @Override
    public CompletableFuture<List<Item>> fetchItemsFromBranch(String branchId) {
        CompletableFuture<List<Item>> listCompletableFuture = new CompletableFuture<>();
        FirebaseDatabase realtimeDatabase = FirebaseDatabase.getInstance();
        realtimeDatabase.getReference("/items_on_branches/"+branchId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Item> items = new ArrayList<>();
                snapshot.getChildren().forEach(dataSnapshot -> {
                    Item item = dataSnapshot.getValue(Item.class);
                    items.add(item);
                });
                listCompletableFuture.complete(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listCompletableFuture.completeExceptionally(error.toException());
            }
        });
        return listCompletableFuture;
    }

    @Override
    public CompletableFuture<Void> insertItem(List<Item> items) {
        throw new NotImplementedError();
    }

    @Override
    public void insertRecipeToBranch(String branchId, String itemId, OnDataReceived<Void> onDataReceived) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference globalItemListRef = firebaseDatabase.getReference(ItemRepositoryImpl.GLOBAL_PATH_TO_ITEM_LIST + "/" + branchId);
        firestore.collection(ItemRepositoryImpl.GLOBAL_PATH_TO_ITEM_LIST).document(itemId).addSnapshotListener((value, error) -> {
            Item item = value.toObject(Item.class);
            globalItemListRef.child(itemId).setValue(item);
        });
    }


}
