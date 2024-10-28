package com.denproj.posmanongjaks.repository.firebaseImpl;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.repository.base.ProductRepository;
import com.denproj.posmanongjaks.util.OnFetchFailed;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FirebaseProductRepository implements ProductRepository {

    public static final String PATH_TO_BRANCH_PRODUCTS = "products_on_branches";
    public static final String PATH_TO_GLOBAL_ITEM_LIST = "items_list";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    public MutableLiveData<List<Product>> observeProductList(String branchId, OnFetchFailed onFetchFailed) {
        MutableLiveData<List<Product>> mutableLiveData = new MutableLiveData<>();
        DatabaseReference productsOnBranches = firebaseDatabase.getReference(PATH_TO_BRANCH_PRODUCTS + "/" + branchId);
        productsOnBranches.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot productsSnapshot) {
                List<Product> productsList = new ArrayList<>();
                for (DataSnapshot productSnapshot : productsSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productsList.add(product);
                }
                mutableLiveData.setValue(productsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onFetchFailed.onFetchFailed(error.toException());
            }
        });
        return mutableLiveData;
    }

    @Override
    public CompletableFuture<List<Product>> fetchProductsFromBranch(String branchId) {

        return null;
    }

    @Override
    public CompletableFuture<Void> insertProduct(String branchId, List<Product> products) {

        return null;
    }
}
