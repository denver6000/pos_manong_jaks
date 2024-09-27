package com.denproj.posmanongjaks.repository.imp;

import com.denproj.posmanongjaks.repository.base.LoginRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginRepositoryImpl implements LoginRepository {
    @Override
    public void loginUser(String email, String password, OnDataReceived<String> onUserLoggedIn) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                onUserLoggedIn.onSuccess(FirebaseAuth.getInstance().getUid());
            } else {
                onUserLoggedIn.onFail(task.getException());
            }
        });
    }

    @Override
    public void getUserBranchId(OnDataReceived<String> onBranchIdFetched) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null && !uid.isEmpty()) {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("users").whereEqualTo("user_id", uid)
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            onBranchIdFetched.onSuccess(task
                                    .getResult()
                                    .getDocuments()
                                    .get(0)
                                    .getData()
                                    .get("branch_id").toString());
                        } else {
                            onBranchIdFetched.onFail(task.getException());
                        }
                    });

        }
    }
}
