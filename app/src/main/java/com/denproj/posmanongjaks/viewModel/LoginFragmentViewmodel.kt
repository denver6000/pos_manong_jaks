package com.denproj.posmanongjaks.viewModel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denproj.posmanongjaks.hilt.qualifier.FirebaseImpl
import com.denproj.posmanongjaks.repository.base.BranchRepository
import com.denproj.posmanongjaks.repository.base.UserRepository
import com.denproj.posmanongjaks.repository.impl.SessionRepository
import com.denproj.posmanongjaks.session.SessionSimple
import com.denproj.posmanongjaks.util.OnLoginSuccessful
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginFragmentViewmodel @Inject constructor(val sessionRepository: SessionRepository, @FirebaseImpl val userRepository: UserRepository, @FirebaseImpl val branchRepository: BranchRepository) : ViewModel() {
    @JvmField
    var observableEmail: ObservableField<String> = ObservableField("")
    @JvmField
    var observablePassword: ObservableField<String> = ObservableField("")

    fun firebaseLogin(isFirebaseAuthCachePresent: IsFirebaseAuthCachePresent) {
        val firebaseAuth = FirebaseAuth.getInstance()
        viewModelScope.launch {
            val simpleSession = sessionRepository.getSession()
            if (firebaseAuth.currentUser == null) {
                isFirebaseAuthCachePresent.cacheAbsent()
                sessionRepository.clearSession()
            } else if (simpleSession != null) {
                isFirebaseAuthCachePresent.cachePresent(simpleSession)
            }
        }
    }

    fun emailPasswordLogin(onLoginSuccessful: OnLoginSuccessful) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val email = observableEmail.get()
        val password = observablePassword.get()
        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            onLoginSuccessful.onLoginFailed(Exception("Empty Fields"))
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult: AuthResult ->
                    if (authResult.user != null) {
                        viewModelScope.launch {
                            try {
                                val user = userRepository.getUserFlow(authResult.user!!)
                                val sessionSimple = SessionSimple(userId = user.user_id, name = user.username, branchId = user.branches?.get(0), branchName = user.branch_location, roleName =  user.role)
                                sessionRepository.saveSession(sessionSimple)
                                onLoginSuccessful.onLoginSuccess(sessionSimple)
                            } catch (e: Exception) {
                                onLoginSuccessful.onLoginFailed(e)
                            }
                        }
                    }
                }.addOnFailureListener {
                    e: Exception? -> onLoginSuccessful.onLoginFailed(e)
                }
        }
    }

    interface IsFirebaseAuthCachePresent {
        fun cachePresent(sessionSimple: SessionSimple)
        fun cacheAbsent()
    }
}
