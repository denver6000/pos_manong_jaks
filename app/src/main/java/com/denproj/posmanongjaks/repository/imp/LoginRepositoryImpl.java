package com.denproj.posmanongjaks.repository.imp;

import com.denproj.posmanongjaks.hilt.qualifier.OnlineImpl;
import com.denproj.posmanongjaks.model.User;
import com.denproj.posmanongjaks.repository.base.BranchRepository;
import com.denproj.posmanongjaks.repository.base.LoginRepository;
import com.denproj.posmanongjaks.repository.base.RoleRepository;
import com.denproj.posmanongjaks.session.Session;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import javax.inject.Inject;

public class LoginRepositoryImpl implements LoginRepository {

    RoleRepository roleRepository;
    BranchRepository branchRepository;


    @Inject
    public LoginRepositoryImpl(@OnlineImpl  RoleRepository roleRepository, @OnlineImpl BranchRepository branchRepository) {
        this.roleRepository = roleRepository;
        this.branchRepository = branchRepository;
    }

    public static final String PATH_TO_ROLES_LIST = "roles";
    public static final String PATH_TO_USER_LIST = "users";
    public static final String APP_ROLE = "Staff";
    public static final String UID_FIELD = "user_id";
    public static final String FIRESTORE_FIELD_ROLE = "role";
    public static final String PATH_TO_BRANCH_LIST = "branches";
    public static final String BRANCH_ID_FIELD = "branch_id";

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public CompletableFuture<Session> loginUser(String email, String password, OnDataReceived<Session> onUserLoggedIn) {
        CompletableFuture<Session> sessionCompletableFuture = new CompletableFuture<>();
        this.auth = FirebaseAuth.getInstance();
        this.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                LoginRepositoryImpl.this.getUserByUID(auth.getUid(), onUserLoggedIn);
            } else {
                onUserLoggedIn.onFail(task.getException());
            }
        });
        return sessionCompletableFuture;
    }



    private void getUserByUID(String uid, OnDataReceived<Session> onSessionDataReceived) {
        firestore
                .collection(PATH_TO_USER_LIST)
                .whereEqualTo(FIRESTORE_FIELD_ROLE, APP_ROLE)
                .whereEqualTo(UID_FIELD, uid)
                .get().addOnCompleteListener(task -> {

                    if (task.isCanceled()) {
                        onSessionDataReceived.onFail(new Exception("Something went wrong"));
                        return;
                    }

                    QuerySnapshot matches = task.getResult();

                    if (matches.isEmpty()) {
                        onSessionDataReceived.onFail(new Exception("Account is not registered as an Employee or Admin, Please contact system admins."));
                        return;
                    }

                    if (matches.size() > 1) {
                        onSessionDataReceived.onFail(new Exception("Your UID is registered with multiple entries. Please contact system admin to resolve"));
                        return;
                    }

                    DocumentSnapshot user = matches.getDocuments().get(0);

                    String role = user.get(FIRESTORE_FIELD_ROLE, String.class);
                    if (role == null || !role.equals(APP_ROLE)) {
                        onSessionDataReceived.onFail(new Exception("User is not a staff."));
                        return;
                    }

                    String branchId = null;
                    try {
                        List<String> branchIds = (List<String>) user.get("branches");
                        if (branchIds == null || branchIds.isEmpty()) {
                            throw new Exception("Malformed or no Registered Branch, Please Contact Server Admin.");
                        }
                        branchId = branchIds.get(0).toString();
                    } catch (Exception e) {
                        onSessionDataReceived.onFail(e);
                    }

                    if (branchId == null) {
                        onSessionDataReceived.onFail(new Exception("Malformed or no Registered Branch, Please Contact Server Admin."));
                    }



                    User userObj = user.toObject(User.class);
                    userObj.setBranch_id(branchId);
                    Session session = new Session();
                    session.setUser(userObj);
                    roleRepository.getRole(role).thenAccept(role1 -> {
                        session.setRole(role1);
                    }).thenAccept(unused -> {
                        branchRepository.getBranch(userObj.getBranch_id()).thenAccept(session::setBranch).exceptionally(new Function<Throwable, Void>() {
                            @Override
                            public Void apply(Throwable throwable) {
                                onSessionDataReceived.onFail(new Exception(throwable));
                                return null;
                            }
                        }).thenAccept(unused1 -> {
                            onSessionDataReceived.onSuccess(session);
                        });
                    }).exceptionally(throwable -> {
                        onSessionDataReceived.onFail(new Exception(throwable));
                        return null;
                    });
                });
    }
}
