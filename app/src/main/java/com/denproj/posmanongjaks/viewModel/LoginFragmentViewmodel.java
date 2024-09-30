package com.denproj.posmanongjaks.viewModel;

import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.model.Role;
import com.denproj.posmanongjaks.model.User;
import com.denproj.posmanongjaks.repository.base.LoginRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.util.OnUpdateUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginFragmentViewmodel extends ViewModel {

    LoginRepository loginRepository;

    @Inject
    LoginFragmentViewmodel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public void loginUserAndFetchBranchId(String email, String password, OnUpdateUI<String> onUpdateUI) {
        loginRepository.loginUser(email, password, new OnDataReceived<String>() {
            @Override
            public void onSuccess(String result) {
                loginRepository.getUserBranchId(new OnDataReceived<User>() {
                    @Override
                    public void onSuccess(User user) {
                        loginRepository.getUserRole(user.getRole_id(), new OnDataReceived<Role>() {
                            @Override
                            public void onSuccess(Role result) {
                                onUpdateUI.onSuccess(user.getBranch_id());
                            }

                            @Override
                            public void onFail(Exception e) {
                                onUpdateUI.onFail(e);
                            }
                        });
                    }

                    @Override
                    public void onFail(Exception e) {
                        onUpdateUI.onFail(new Exception("Something went wrong in fetching your branch."));
                    }
                });
            }

            @Override
            public void onFail(Exception e) {
                onUpdateUI.onFail(new Exception("Something went wrong in logging in."));
            }
        });
    }
}
