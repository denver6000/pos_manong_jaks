package com.denproj.posmanongjaks.viewModel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.model.Role;
import com.denproj.posmanongjaks.model.SavedLoginCredentials;
import com.denproj.posmanongjaks.model.User;
import com.denproj.posmanongjaks.repository.base.LoginRepository;
import com.denproj.posmanongjaks.repository.base.SavedLoginRepository;
import com.denproj.posmanongjaks.session.SessionManager;
import com.denproj.posmanongjaks.util.AsyncRunner;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.util.OnUpdateUI;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginFragmentViewmodel extends ViewModel {

    LoginRepository loginRepository;
    SavedLoginRepository savedLoginRepository;

    @Inject
    LoginFragmentViewmodel(LoginRepository loginRepository, SavedLoginRepository savedLoginRepository) {
        this.loginRepository = loginRepository;
        this.savedLoginRepository = savedLoginRepository;
    }

    public void attemptLogin(IsSavedLoginPresent isSavedLoginPresent) {
        AsyncRunner.runAsync(new AsyncRunner.Runner<SavedLoginCredentials>() {
            @Override
            public SavedLoginCredentials onBackground() throws Exception {

                SavedLoginCredentials savedLoginCredentials = savedLoginRepository.getSavedLogin();
                if (savedLoginCredentials != null) {
                    SessionManager.getInstance(savedLoginCredentials.getBranchId());
                } else {
                    throw new Exception("No Saved Login");
                }
                return savedLoginCredentials;
            }

            @Override
            public void onFinished(SavedLoginCredentials result) {

            }

            @Override
            public void onUI(SavedLoginCredentials result) {
                if (result != null) {
                    isSavedLoginPresent.onHasSaved(result);
                } else {
                    isSavedLoginPresent.onNoSavedLogin();
                }
            }

            @Override
            public void onError(Exception e) {
                isSavedLoginPresent.onNoSavedLogin();
            }
        });
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

    public void saveLogin(String email,  String password, String user_id, String branchId) {
        AsyncRunner.runAsync(new AsyncRunner.Runner<Void>() {
            @Override
            public Void onBackground() throws Exception {
                savedLoginRepository.saveLoginCredentials(new SavedLoginCredentials(email, password, branchId, user_id));
                return null;
            }

            @Override
            public void onFinished(Void result) {

            }

            @Override
            public void onUI(Void result) {

            }

            @Override
            public void onError(Exception e) {
                Log.e("LoginInVM", e.getLocalizedMessage());
            }
        });
    }

    public interface IsSavedLoginPresent {
        void onHasSaved(SavedLoginCredentials branchId);
        void onNoSavedLogin();
    }
}
