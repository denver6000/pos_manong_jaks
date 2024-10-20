package com.denproj.posmanongjaks.repository.imp;

import androidx.annotation.NonNull;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.repository.base.AddOnsRepository;
import com.denproj.posmanongjaks.session.SessionManager;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AddOnsRepositoryImpl implements AddOnsRepository {
    public static String BRANCH_ITEMS_LIST = "items_on_branches";
    @Override
    public void getAddOnsRepository(OnDataReceived<List<Item>> onDataReceived) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String branchId = SessionManager.getInstance().getBranchId();
        DatabaseReference databaseReference = firebaseDatabase.getReference(BRANCH_ITEMS_LIST + "/" + branchId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Item> addOns = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    addOns.add(child.getValue(Item.class));
                }
                onDataReceived.onSuccess(addOns);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onDataReceived.onFail(error.toException());
            }
        });
//        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//        firestore.collection("items_list").whereEqualTo("ads_on", true).addSnapshotListener((value, error) -> {
//            List<Item> addOnsList = value.toObjects(Item.class);
//            try {
//                onDataReceived.onSuccess(addOnsList);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
    }
}
