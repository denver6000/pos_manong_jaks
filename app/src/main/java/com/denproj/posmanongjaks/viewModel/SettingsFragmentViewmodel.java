package com.denproj.posmanongjaks.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.repository.base.SavedLoginRepository;
import com.denproj.posmanongjaks.room.dao.UserDao;
import com.denproj.posmanongjaks.util.AsyncRunner;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragmentViewmodel extends ViewModel {

    UserDao userDao;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public SettingsFragmentViewmodel(UserDao userDao) {
        this.userDao = userDao;
    }

    public void resetPassword(boolean isConnectionReachable, OnPasswordResetFinished onPasswordResetFinished) {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (isConnectionReachable && isUserSignedIn() && firebaseUser.getEmail() != null) {
            String email = firebaseUser.getEmail();
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    onPasswordResetFinished.onSuccess();
                }
            });
            return;
        }
        onPasswordResetFinished.onFail();
    }

    public void clearSavedLoginAndLogout(OnLogOutSuccessful onLogOutSuccessful) {
        AsyncRunner.runAsync(new AsyncRunner.MiniRunner<Void>() {
            @Override
            public Void onBackground() throws Exception {
                userDao.deleteAllSavedLogin();
                return null;
            }

            @Override
            public void onUI(Void result) {
                deleteFirebaseUser(new OnDataReceived<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        onLogOutSuccessful.onSuccess();
                    }

                    @Override
                    public void onFail(Exception e) {
                        onLogOutSuccessful.onFail(e);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                onLogOutSuccessful.onFail(e);
            }
        });
    }

    private boolean isUserSignedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    private void deleteFirebaseUser(OnDataReceived<Void> onDataReceived) {
        if (isUserSignedIn()) {
            firebaseAuth.getCurrentUser().delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    onDataReceived.onSuccess(null);
                } else {
                    onDataReceived.onFail(task.getException());
                }
            });
        }
    }

    public interface OnPasswordResetFinished {
        void onSuccess();
        void onFail();
    }

    public interface OnLogOutSuccessful {
        void onSuccess();
        void onFail(Exception e);
    }

}