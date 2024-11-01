package com.denproj.posmanongjaks.repository.firebaseImpl;

import com.denproj.posmanongjaks.model.Branch;
import com.denproj.posmanongjaks.repository.base.BranchRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

public class FirebaseBranchRepository implements BranchRepository {
    private static final String PATH_TO_BRANCHES = "branches";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    public CompletableFuture<Branch> getBranch(String branchId) {
        CompletableFuture<Branch> branchCompletableFuture = new CompletableFuture<>();
        firestore.collection(PATH_TO_BRANCHES).whereEqualTo("branch_id", branchId).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty()) {
                branchCompletableFuture.completeExceptionally(new Exception("No Branch Associated With this account."));
            } else {
                Branch branch = queryDocumentSnapshots.getDocuments().get(0).toObject(Branch.class);
                branchCompletableFuture.complete(branch);
            }
        });
        return branchCompletableFuture;
    }
}
