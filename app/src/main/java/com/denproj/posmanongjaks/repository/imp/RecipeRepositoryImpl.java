package com.denproj.posmanongjaks.repository.imp;

import androidx.annotation.Nullable;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.repository.base.ItemRepository;
import com.denproj.posmanongjaks.repository.base.RecipeRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class RecipeRepositoryImpl implements RecipeRepository {


    @Override
    public void insertRecipeToBranch(String branchId, String itemId, OnDataReceived<Void> onDataReceived) {

    }
}
