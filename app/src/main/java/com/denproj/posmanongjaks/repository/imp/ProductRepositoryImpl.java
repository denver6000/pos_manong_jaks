package com.denproj.posmanongjaks.repository.imp;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.model.Recipe;
import com.denproj.posmanongjaks.repository.base.ImageRepository;
import com.denproj.posmanongjaks.repository.base.ProductRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ProductRepositoryImpl implements ProductRepository {

    public static final String PATH_TO_BRANCH_ITEMS = "items_on_branches";
    public static final String PATH_TO_BRANCH_PRODUCTS = "products_on_branches";
    public static final String PATH_TO_GLOBAL_PRODUCT_LIST = "products_list";
    public static final String PATH_TO_GLOBAL_ITEM_LIST = "items_list";
    public static final String PRODUCT_IMAGE_PATH = "product_images";
    private static final String TAG = "ProductRepositoryImpl";

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
        realtimeDatabase.getReference(PATH_TO_BRANCH_PRODUCTS + "/" + branchId)
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    List<Product> products = new ArrayList<>();
                    dataSnapshot.getChildren().forEach(dataSnapshot1 -> {
                        products.add(dataSnapshot1.getValue(Product.class));
                    });
                    onProductListReceived.onSuccess(products);
                }).addOnFailureListener(onProductListReceived::onFail);

    }

    @Override
    public void insertProductToBranch(String branchId, Product product, OnDataReceived<Void> onDataReceived) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(PATH_TO_BRANCH_PRODUCTS + "/" + branchId).child(product.getProduct_id() + "");
        ref.setValue(product).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                onDataReceived.onSuccess(task.getResult());

            } else {
                onDataReceived.onFail(task.getException());
            }
        });
    }

    @Override
    public void insertProductToGlobalList(Product product, OnDataReceived<Void> onDataReceived) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(PATH_TO_GLOBAL_PRODUCT_LIST).add(product).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                onDataReceived.onSuccess(null);
            } else {
                onDataReceived.onFail(task.getException());
            }
        });
    }

    @Override
    public void loadRecipeIntoBranchStocks(String branchId, HashMap<String, Recipe> recipes, OnDataReceived<Void> onDataReceived) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference(PATH_TO_BRANCH_ITEMS + "/" + branchId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChildren()) {
                    recipes.forEach((s, recipe) -> {
                        insertRecipeToBranch(branchId, s, null);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onDataReceived.onFail(error.toException());
            }
        });
    }

    @Override
    public void insertImage(Uri uri, String imageFileName, OnDataReceived<String> onDataReceived) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storage.getReference(PRODUCT_IMAGE_PATH).child(imageFileName).putFile(uri).addOnCompleteListener(task -> {
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

    @Override
    public void insertRecipeToBranch(String branchId, String itemId, OnDataReceived<Void> onDataReceived) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference globalItemListRef = firebaseDatabase.getReference(PATH_TO_BRANCH_ITEMS+ "/" + branchId);
        firestore.collection(PATH_TO_GLOBAL_ITEM_LIST).whereEqualTo("item_id", Integer.valueOf(itemId)).addSnapshotListener((value, error) -> {
            Item item = value.getDocuments().get(0).toObject(Item.class);
            globalItemListRef.child(itemId).setValue(item).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, item.getItem_name() + " inserted");
                } else {
                    Log.e(TAG, task.getException().getMessage());
                }
            });
        });
    }
}
