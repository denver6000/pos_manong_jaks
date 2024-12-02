package com.denproj.posmanongjaks.viewModel

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsFragmentViewmodel @Inject constructor() : ViewModel() {
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun resetPassword(
        isConnectionReachable: Boolean,
        onPasswordResetFinished: OnPasswordResetFinished
    ) {
        val firebaseUser = firebaseAuth.currentUser
        if (isConnectionReachable && isUserSignedIn && firebaseUser!!.email != null) {
            val email = firebaseUser.email
            firebaseAuth.sendPasswordResetEmail(email!!)
                .addOnCompleteListener { task: Task<Void?> ->
                    if (task.isSuccessful) {
                        firebaseAuth.signOut()
                    }
                }
            firebaseAuth.addAuthStateListener { firebaseAuth: FirebaseAuth ->
                if (firebaseAuth.currentUser == null) {
                    onPasswordResetFinished.onSuccess()
                }
            }
            return
        }

        onPasswordResetFinished.onFail()
    }

    fun logout(onLogOutSuccessful: OnLogOutSuccessful) {
        if (isUserSignedIn) {
            firebaseAuth.signOut()
            onLogOutSuccessful.onSuccess()
        } else {
            onLogOutSuccessful.onFail(Exception("User already signed out."))
        }
    }


    private val isUserSignedIn: Boolean
        get() = firebaseAuth.currentUser != null

    interface OnPasswordResetFinished {
        fun onSuccess()
        fun onFail()
    }

    interface OnLogOutSuccessful {
        fun onSuccess()
        fun onFail(e: Exception?)
    }


    interface UserTask {
        fun success()
        fun failed(e: Exception?)
    }
}
