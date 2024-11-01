package com.denproj.posmanongjaks.viewModel;

import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.util.AsyncRunner;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsFragmentViewmodel extends ViewModel {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Inject
    public SettingsFragmentViewmodel() {
    }

    public void resetPassword(boolean isConnectionReachable, OnPasswordResetFinished onPasswordResetFinished) {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (isConnectionReachable && isUserSignedIn() && firebaseUser.getEmail() != null) {
            String email = firebaseUser.getEmail();
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    firebaseAuth.signOut();
                }
            });
            firebaseAuth.addAuthStateListener(firebaseAuth -> {
                if (firebaseAuth.getCurrentUser() == null) {
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
