package com.denproj.posmanongjaks.repository.firebaseImpl;

import com.denproj.posmanongjaks.model.Role;
import com.denproj.posmanongjaks.repository.base.RoleRepository;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

public class FirebaseRoleRepository implements RoleRepository {
    private static final String PATH_TO_ROLES = "roles";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    public CompletableFuture<Role> getRole(String roleName) {
        CompletableFuture<Role> completableFuture = new CompletableFuture<>();
        completableFuture.complete(new Role(roleName + "_id", roleName));
        return completableFuture;
    }
}

