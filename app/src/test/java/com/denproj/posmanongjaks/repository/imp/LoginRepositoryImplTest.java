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

        firestore
                .collection(LoginRepositoryImpl.PATH_TO_USER_LIST)
                .whereEqualTo(LoginRepositoryImpl.FIRESTORE_FIELD_ROLE, LoginRepositoryImpl.APP_ROLE)
                .whereEqualTo(LoginRepositoryImpl.UID_FIELD, uid)
                .get().addOnCompleteListener(task -> {

                    if (task.isCanceled()) {
                        fail("Request did not go through");
                        //onUserReceived.onFail(new Exception("Something went wrong"));
                        return;
                    }

                    QuerySnapshot matches = task.getResult();

                    if (!matches.isEmpty()) {
                        assertFalse("No Matches", matches.isEmpty());
                        //onUserReceived.onFail(new Exception("Account is not registered as an Employee or Admin, Please contact system admins."));
                        return;
                    }

                    if (matches.size() > 1) {

                        assertTrue("Case worked for multi entry", matches.size() > 1);
                        //onUserReceived.onFail(new Exception("Your UID is registered with multiple entries. Please contact system admin to resolve"));
                        return;
                    }

                    DocumentSnapshot user = matches.getDocuments().get(0);

                    String role = user.get(LoginRepositoryImpl.FIRESTORE_FIELD_ROLE, String.class);
                    if (role == null || !role.equals(LoginRepositoryImpl.APP_ROLE)) {
                        assertTrue("Prevented Admin Structure of BranchID Property", true);
                        //onUserReceived.onFail(new Exception("User is not a staff."));
                        return;
                    }

                    try {
                        String branchId = user.get("branch_id", String.class);
                        if (branchId == null || branchId.isEmpty()) {
                            throw new Exception("");
                        }
                    } catch (Exception e) {
                        assertTrue("Prevented Admin Structure of BranchID Property", true);
                        //onUserReceived.onFail(e);
                        return;
                    }

                    User userObj = user.toObject(User.class);
                    fail("Reached UserObj creation with invalid user credentials");
                    //onUserReceived.onSuccess(userObj);
                });

    }
}