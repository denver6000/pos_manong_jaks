package com.denproj.posmanongjaks.repository.imp;

import static com.denproj.posmanongjaks.repository.imp.LoginRepositoryImpl.BRANCH_ID_FIELD;
import static com.denproj.posmanongjaks.repository.imp.LoginRepositoryImpl.PATH_TO_BRANCH_LIST;

import com.denproj.posmanongjaks.model.Branch;
import com.denproj.posmanongjaks.repository.base.BranchRepository;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.CompletableFuture;

public class BranchOnlineRepositoryImpl implements BranchRepository {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    @Override
    public CompletableFuture<Branch> getBranch(String branchId) {
        CompletableFuture<Branch> completableFuture = new CompletableFuture<>();
        firestore.collection(PATH_TO_BRANCH_LIST).whereEqualTo(BRANCH_ID_FIELD, branchId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshots = task.getResult();
                DocumentSnapshot documentSnapshot = snapshots.getDocuments().get(0);
                if (documentSnapshot.exists()) {
                    Branch branch = documentSnapshot.toObject(Branch.class);
                    completableFuture.complete(branch);
                } else {
                    completableFuture.completeExceptionally(new Exception("Branch Does Not Exist."));
                }
            }
        });
        return completableFuture;
    }
}
