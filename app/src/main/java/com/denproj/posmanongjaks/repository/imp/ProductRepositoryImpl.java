package com.denproj.posmanongjaks.repository.imp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.model.Recipe;
import com.denproj.posmanongjaks.repository.base.ProductRepository;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import kotlin.NotImplementedError;

public class ProductRepositoryImpl implements ProductRepository {

    public static final String PATH_TO_BRANCH_PRODUCTS = "products_on_branches";
    public static final String PATH_TO_GLOBAL_ITEM_LIST = "items_list";

    @Override
    public CompletableFuture<List<Product>> fetchProductsFromBranch(String branchId) {
        CompletableFuture<List<Product>> fetchProductsCompletableFuture = new CompletableFuture<>();
        FirebaseDatabase realtimeDatabase = FirebaseDatabase.getInstance();
        realtimeDatabase.getReference(PATH_TO_BRANCH_PRODUCTS + "/" + branchId)
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    List<Product> products = new ArrayList<>();
                    dataSnapshot.getChildren().forEach(dataSnapshot1 -> {
                        products.add(dataSnapshot1.getValue(Product.class));
                    });
                    fetchProductsCompletableFuture.complete(products);
                }).addOnFailureListener(fetchProductsCompletableFuture::completeExceptionally);

        return fetchProductsCompletableFuture;

    }

    @Override
    public CompletableFuture<Void> insertProduct(String branchId, List<Product> products) {
        throw new NotImplementedError();
    }
}
