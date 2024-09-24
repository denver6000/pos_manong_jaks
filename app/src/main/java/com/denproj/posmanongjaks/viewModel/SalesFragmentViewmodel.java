package com.denproj.posmanongjaks.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SalesFragmentViewmodel extends ViewModel {
    public static final String PATH_TO_GLOBAL_PRODUCT_LIST = "products_list";
    public static final String PATH_TO_GLOBAL_ITEM_LIST = "items_list";

    @Inject
    public SalesFragmentViewmodel () {

    }

    public void loadGlobalList(OnDataReceived<List<Product>> onGlobalListReceived) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(PATH_TO_GLOBAL_PRODUCT_LIST).addSnapshotListener((value, error) -> {
            if (error == null) {
                List<Product> products = value.toObjects(Product.class);
                onGlobalListReceived.onSuccess(products);
            }
        });
    }

    public void loadProductsOfBranch(String branchId, OnDataReceived<List<Product>> onProductListReceived) {
        FirebaseDatabase realtimeDatabase = FirebaseDatabase.getInstance();
        realtimeDatabase.getReference(PATH_TO_GLOBAL_PRODUCT_LIST + "/" + branchId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> products = new ArrayList<>();
                snapshot.getChildren().forEach(dataSnapshot -> {

                });
                onProductListReceived.onSuccess(products);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onProductListReceived.onFail(error.toException());
            }
        });
    }

    public void saveSelectedProductToBranch(String branchId, HashMap<String, Product> selectedItems) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(PATH_TO_GLOBAL_ITEM_LIST+"/"+branchId);
        selectedItems.values().forEach(s -> s.getRecipes().forEach((s1, recipe) -> {
            ref.child(s1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        //TODO Insert Items To Branch Stocks
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }));
    }

    public void changeProductPrice (String itemId, String branchId, String newPrice, OnDataReceived<Void> onPriceChanged) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database
                .getReference("products_list/" +branchId+"/"+itemId)
                .child("price")
                .setValue(Double.valueOf(newPrice))
                .addOnSuccessListener(onPriceChanged::onSuccess)
                .addOnFailureListener(onPriceChanged::onFail);
    }
}
