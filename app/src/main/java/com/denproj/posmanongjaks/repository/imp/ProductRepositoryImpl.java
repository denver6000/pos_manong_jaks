package com.denproj.posmanongjaks.repository.imp;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.repository.base.ImageRepository;
import com.denproj.posmanongjaks.repository.base.ProductRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {

    public static final String PATH_TO_GLOBAL_PRODUCT_LIST = "products_list";
    public static final String PATH_TO_GLOBAL_ITEM_LIST = "items_list";

    public static final String PRODUCT_IMAGE_PATH = "product_images";

    @Override
    public void fetchProductsFromGlobal(OnDataReceived<List<Product>> onGlobalListReceived) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(PATH_TO_GLOBAL_PRODUCT_LIST).addSnapshotListener((value, error) -> {
            if (error == null) {
                List<Product> products = value.toObjects(Product.class);
                onGlobalListReceived.onSuccess(products);
            }
        });
    }

    @Override
    public void fetchProductsFromBranch(String branchId, OnDataReceived<List<Product>> onProductListReceived) {
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

    @Override
    public void insertProduct(String branchId, Product product, OnDataReceived<Void> onDataReceived) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(PATH_TO_GLOBAL_PRODUCT_LIST + "/" + branchId).child(product.getProduct_id() + "");
        ref.setValue(product).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                onDataReceived.onSuccess(task.getResult());
            } else {
                onDataReceived.onFail(task.getException());
            }
        });
    }

    @Override
    public void insertImage(Uri uri, OnDataReceived<String> onDataReceived) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storage.getReference(PRODUCT_IMAGE_PATH).putFile(uri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                onDataReceived.onSuccess(task.getResult().getMetadata().getName());
            } else {
                onDataReceived.onFail(task.getException());
            }
        });
    }

    @Override
    public void getImage(String path, OnDataReceived<String> onDataReceived) {

    }



}
