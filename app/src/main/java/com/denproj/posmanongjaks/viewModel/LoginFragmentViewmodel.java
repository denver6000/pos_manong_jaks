package com.denproj.posmanongjaks.viewModel;

import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.hilt.qualifier.OnlineImpl;
import com.denproj.posmanongjaks.model.SavedLoginCredentials;
import com.denproj.posmanongjaks.repository.base.BranchRepository;
import com.denproj.posmanongjaks.repository.base.LoginRepository;
import com.denproj.posmanongjaks.repository.base.RoleRepository;
import com.denproj.posmanongjaks.repository.base.SavedLoginRepository;
import com.denproj.posmanongjaks.session.Session;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.util.OnUpdateUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginFragmentViewmodel extends ViewModel {

    LoginRepository loginRepository;
    SavedLoginRepository savedLoginRepository;
    RoleRepository roleOnlineRepository;
    BranchRepository branchOnlineRepository;

    @Inject
    public LoginFragmentViewmodel(LoginRepository loginRepository, SavedLoginRepository savedLoginRepository, @OnlineImpl RoleRepository roleOnlineRepository, @OnlineImpl BranchRepository branchOnlineRepository) {
        this.loginRepository = loginRepository;
        this.savedLoginRepository = savedLoginRepository;
        this.roleOnlineRepository = roleOnlineRepository;
        this.branchOnlineRepository = branchOnlineRepository;
    }

    public CompletableFuture<Session> attemptLogin() {
        return savedLoginRepository.getSavedInfoToSession();
    }

    public Boolean isUserSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public CompletableFuture<Void> clearLocalDb() {
        return savedLoginRepository.clearUserCredentials();
    }

    public void loginUserAndGetSession(String email, String password, OnUpdateUI<Session> onUpdateUI) {
        loginRepository.loginUser(email, password, new OnDataReceived<Session>() {
            @Override
            public void onSuccess(Session result) {
                onUpdateUI.onSuccess(result);
            }

            @Override
            public void onFail(Exception e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    onUpdateUI.onFail(new Exception("Wrong email or password."));
                    return;
                }

                onUpdateUI.onFail(e);
            }
        });
    }

    public CompletableFuture<Void> saveLogin(Session session) {
        return savedLoginRepository.saveSessionToLocal(session);
    }


    public interface IsSavedLoginPresent {
        void onHasSaved(SavedLoginCredentials branchId);
        void onNoSavedLogin();
    }
}
