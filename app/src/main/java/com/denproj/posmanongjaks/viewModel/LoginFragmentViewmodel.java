package com.denproj.posmanongjaks.viewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginFragmentViewmodel extends ViewModel {

    @Inject
    LoginFragmentViewmodel() {

    }

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

    public void fetchBranchIdOfUser(OnDataReceived<String> onDataReceived) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null && !uid.isEmpty()) {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("users").whereEqualTo("user_id", uid)
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            onDataReceived.onSuccess(task
                                    .getResult()
                                    .getDocuments()
                                    .get(0)
                                    .getData()
                                    .get("branch_id").toString());
                        } else {
                            onDataReceived.onFail(task.getException());
                        }
                    });

        }
    }

}
