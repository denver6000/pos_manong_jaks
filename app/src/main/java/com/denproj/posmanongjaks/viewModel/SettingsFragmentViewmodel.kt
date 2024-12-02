package com.denproj.posmanongjaks.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.util.AsyncRunner;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
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

    public void logout(OnLogOutSuccessful onLogOutSuccessful) {
        if (isUserSignedIn()) {
            firebaseAuth.signOut();
            onLogOutSuccessful.onSuccess();
        } else {
            onLogOutSuccessful.onFail(new Exception("User already signed out."));
        }
    }


    private boolean isUserSignedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    public interface OnPasswordResetFinished {
        void onSuccess();
        void onFail();
    }

    public interface OnLogOutSuccessful {
        void onSuccess();
        void onFail(Exception e);
    }



    public interface UserTask{
        void success();
        void failed(Exception e);
    }


}
