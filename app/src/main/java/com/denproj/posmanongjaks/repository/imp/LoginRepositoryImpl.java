package com.denproj.posmanongjaks.repository.imp;

import com.denproj.posmanongjaks.model.Role;
import com.denproj.posmanongjaks.model.User;
import com.denproj.posmanongjaks.repository.base.LoginRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginRepositoryImpl implements LoginRepository {

    public static final String PATH_TO_ROLES_LIST = "roles";

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
    public void getUserBranchId(OnDataReceived<User> onBranchIdFetched) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null && !uid.isEmpty()) {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("users").whereEqualTo("user_id", uid)
                    .get().addOnCompleteListener(task -> {
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

    @Override
    public void getUserRole(String roleId, OnDataReceived<Role> onUserRoleFetched) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore
                .collection(PATH_TO_ROLES_LIST)
                .whereEqualTo("role_id", roleId).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Role role = task.getResult().getDocuments().get(0).toObject(Role.class);
                        if (role == null) {
                            onUserRoleFetched.onFail(new Exception("Role not found"));
                        } else {
                            onUserRoleFetched.onSuccess(role);
                        }
                    }
                });
    }
}
