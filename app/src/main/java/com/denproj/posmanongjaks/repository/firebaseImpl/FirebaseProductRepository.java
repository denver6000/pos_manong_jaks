package com.denproj.posmanongjaks.repository.firebaseImpl;

import androidx.annotation.NonNull;

import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.repository.base.ProductRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FirebaseProductRepository implements ProductRepository {

    public static final String PATH_TO_BRANCH_PRODUCTS = "products_on_branches";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    public void observeProductList(@Nullable String branchId, @NotNull OnDataReceived<List<Product>> products) {
        DatabaseReference productsOnBranches = firebaseDatabase.getReference(PATH_TO_BRANCH_PRODUCTS + "/" + branchId);
        productsOnBranches.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot productsSnapshot) {
                List<Product> productsList = new ArrayList<>();
                for (DataSnapshot productSnapshot : productsSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productsList.add(product);
                }
                products.onSuccess(productsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                products.onFail(error.toException());
            }
        });
    }
}
