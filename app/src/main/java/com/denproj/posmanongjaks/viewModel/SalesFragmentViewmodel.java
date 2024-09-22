package com.denproj.posmanongjaks.viewModel;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SalesFragmentViewmodel extends ViewModel {
    public static final String PATH_TO_GLOBAL_LIST = "products_list";

    @Inject
    public SalesFragmentViewmodel () {

    }

    public void loadGlobalList(OnDataReceived<List<Product>> onGlobalListReceived) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(PATH_TO_GLOBAL_LIST).addSnapshotListener((value, error) -> {
            if (error == null) {
                List<Product> products = new ArrayList<>();
                value.getDocuments().forEach(documentSnapshot -> {
                    Log.d("Test", "Success");
                    Product product = documentSnapshot.toObject(Product.class);
                    Log.d("Product", product.getProduct_category());
//                    Product product = documentSnapshot.
//                    products.add(product);
//                    documentSnapshot.getDocumentReference("/recipes").addSnapshotListener((value1, error1) -> {
//                        value1.getData().keySet();
//                    });
                    Log.d("Key", "Test");
                });
                onGlobalListReceived.onSuccess(products);
            }
        });
    }

    public void loadProductsOfBranch(String branchId, OnDataReceived<List<Product>> onProductListReceived) {
        FirebaseDatabase realtimeDatabase = FirebaseDatabase.getInstance();
        realtimeDatabase.getReference(PATH_TO_GLOBAL_LIST + "/" + branchId).addValueEventListener(new ValueEventListener() {
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
        database.getReference(PATH_TO_GLOBAL_LIST).child(branchId).setValue(selectedItems);
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
