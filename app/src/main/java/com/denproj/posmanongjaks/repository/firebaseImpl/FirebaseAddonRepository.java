package com.denproj.posmanongjaks.repository.firebaseImpl;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.repository.base.AddOnsRepository;
import com.denproj.posmanongjaks.util.OnFetchFailed;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseAddonRepository implements AddOnsRepository {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public static final String PATH_TO_ITEMS_ON_BRANCHES = "items_on_branches";

    @Override
    public MutableLiveData<List<Item>> observeAddOnsList(String branchId, OnFetchFailed onFetchFailed) {
        MutableLiveData<List<Item>> mutableLiveData = new MutableLiveData<>();
        DatabaseReference databaseReference = firebaseDatabase.getReference( PATH_TO_ITEMS_ON_BRANCHES + "/" + branchId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot items) {
                List<Item> addOns = new ArrayList<>();
                for (DataSnapshot itemSnapshot : items.getChildren()) {
                    Boolean isAddOn = itemSnapshot.child("ads_on").getValue(Boolean.class);
                    if (isAddOn != null && isAddOn) {
                        Item item = itemSnapshot.getValue(Item.class);
                        addOns.add(item);
                    }
                }
                mutableLiveData.setValue(addOns);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onFetchFailed.onFetchFailed(error.toException());
            }
        });
        return mutableLiveData;
    }
}
