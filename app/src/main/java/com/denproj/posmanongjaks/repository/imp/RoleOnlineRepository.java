package com.denproj.posmanongjaks.repository.imp;

import com.denproj.posmanongjaks.model.Role;
import com.denproj.posmanongjaks.repository.base.RoleRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

public class RoleOnlineRepository implements RoleRepository {
    public static final String PATH_TO_ROLES_LIST = "roles";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    @Override
    public CompletableFuture<Role> getRole(String roleId) {
        CompletableFuture<Role> roleCompletableFuture = new CompletableFuture<>();
        roleCompletableFuture.complete(new Role(roleId + "id", roleId));
//        firestore.collection(PATH_TO_ROLES_LIST)
//                .whereEqualTo("role_id", roleId)
//                .get().addOnSuccessListener(queryDocumentSnapshots -> {
//                    List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
//                    if (documentSnapshots.isEmpty()) {
//                        roleCompletableFuture.completeExceptionally(new Exception("No Roles Found With ID. Contact System Admin"));
//                        return;
//                    }
//
//                    DocumentSnapshot snapshot = documentSnapshots.get(0);
//                    if (snapshot.exists()) {
//                        Role role = snapshot.toObject(Role.class);
//                        roleCompletableFuture.complete(role);
//                    }
//                }).addOnFailureListener(roleCompletableFuture::completeExceptionally);
        return roleCompletableFuture;
    }
}
