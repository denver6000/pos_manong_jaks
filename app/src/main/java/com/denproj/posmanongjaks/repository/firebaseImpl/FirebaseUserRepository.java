package com.denproj.posmanongjaks.repository.firebaseImpl;

import com.denproj.posmanongjaks.model.User;
import com.denproj.posmanongjaks.repository.base.UserRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.CompletableFuture;

public class FirebaseUserRepository implements UserRepository {
    private static final String PATH_TO_USER = "users";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    public CompletableFuture<User> getUserByUid() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        CompletableFuture<User> completableFuture = new CompletableFuture<>();
        String uid = firebaseUser.getUid();
        firestore.collection(PATH_TO_USER).whereEqualTo("user_id", uid).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                completableFuture.completeExceptionally(new Exception("User Not Registered as A staff or admin."));
            } else {
                completableFuture.complete(queryDocumentSnapshots.getDocuments().get(0).toObject(User.class));
            }
        });
        return completableFuture;
    }
}
