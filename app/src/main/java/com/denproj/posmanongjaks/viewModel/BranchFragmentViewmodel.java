package com.denproj.posmanongjaks.viewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class BranchFragmentViewmodel extends ViewModel {

    public MutableLiveData<HashMap<Integer, Item>> listMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> branchIdLiveData = new MutableLiveData<>("");

    @Inject
    public BranchFragmentViewmodel() {

    }

    public void loadGlobalItemList(OnDataReceived<List<Item>> onListReceived) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

         CollectionReference documentReference = firestore.collection("items_list");
         documentReference.get().addOnSuccessListener(queryDocumentSnapshots -> {
             List<Item> itemsList = new ArrayList<>();
             queryDocumentSnapshots.forEach(queryDocumentSnapshot -> {
                 itemsList.add(queryDocumentSnapshot.toObject(Item.class));
             });
             onListReceived.onSuccess(itemsList);
         });

    }

    public void saveSelectionToBranchList(String branchId, HashMap<Integer, Item> selectedItemsMap, OnDataReceived<Void> onComplete) {
        FirebaseDatabase realtimeDatabase = FirebaseDatabase.getInstance();
        DatabaseReference itemsOnBranchesRef = realtimeDatabase.getReference("items_on_branches");
        DatabaseReference branchChild = itemsOnBranchesRef.child(branchId);
        branchChild.setValue(selectedItemsMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                onComplete.onSuccess(null);
            } else {
                onComplete.onFail(task.getException());
            }
        });
    }

    public void getRealtimeBranchStocks(String branchId, OnDataReceived<List<Item>> onDataReceived) {
        FirebaseDatabase realtimeDatabase = FirebaseDatabase.getInstance();
        realtimeDatabase.getReference("/items_on_branches/"+branchId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Item> items = new ArrayList<>();
               snapshot.getChildren().forEach(dataSnapshot -> {
                   Item item = dataSnapshot.getValue(Item.class);
                   items.add(item);
//                   Log.d("Debug", item.getItemImage());
               });
               onDataReceived.onSuccess(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", error.getMessage());
            }
        });
    }

}
