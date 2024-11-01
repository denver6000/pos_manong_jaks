package com.denproj.posmanongjaks.repository.imp;


import com.denproj.posmanongjaks.model.User;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import junit.framework.TestCase;

import org.junit.runner.RunWith;

public class LoginRepositoryImplTest extends TestCase {

    private FirebaseFirestore firestore;
    private String uid;

    public void testGetUser() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null && !uid.isEmpty()) {
            this.firestore.collection("users").whereEqualTo("user_id", uid)
                    .get().addOnCompleteListener(task -> {
                        OnDataReceived<Object> onBranchIdFetched = null;
                        if (task.isSuccessful()) {
                            User user = task.getResult().getDocuments().get(0).toObject(User.class);
                            if (user == null) {
                                onBranchIdFetched.onFail(new Exception("User not found"));
                            } else {
                                onBranchIdFetched.onSuccess(user);
                            }
                        } else {
                            onBranchIdFetched.onFail(task.getException());
                        }
                    });
        }
    }

    public void setUp() throws Exception {
        this.uid = "fghdfghdn 5dntdyn";
        this.firestore = FirebaseFirestore.getInstance();
        super.setUp();
    }

    public void testGetUserByUID() {



    }
}