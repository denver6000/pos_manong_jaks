package com.denproj.posmanongjaks.viewModel;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.util.OnLoginSuccessful;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginFragmentViewmodel extends ViewModel {

    public ObservableField<String> observableEmail = new ObservableField<>("");
    public ObservableField<String> observablePassword = new ObservableField<>("");

    @Inject
    public LoginFragmentViewmodel() {

    }





    public void firebaseLogin(IsFirebaseAuthCachePresent isFirebaseAuthCachePresent) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            isFirebaseAuthCachePresent.cacheAbsent();
        } else {
            String uid = firebaseAuth.getCurrentUser().getUid();
            isFirebaseAuthCachePresent.cachePresent();
        }
    }

    public void emailPasswordLogin(OnLoginSuccessful onLoginSuccessful) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String email = observableEmail.get();
        String password = observablePassword.get();
        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            onLoginSuccessful.onLoginFailed(new Exception("Empty Fields"));
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                onLoginSuccessful.onLoginSuccess();
            }).addOnFailureListener(onLoginSuccessful::onLoginFailed);
        }
    }

    public interface IsFirebaseAuthCachePresent {
        void cachePresent();
        void cacheAbsent();
    }
}
