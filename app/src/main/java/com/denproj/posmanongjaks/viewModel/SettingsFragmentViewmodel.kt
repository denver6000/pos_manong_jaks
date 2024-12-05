package com.denproj.posmanongjaks.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denproj.posmanongjaks.repository.impl.SessionRepository
import com.denproj.posmanongjaks.util.NetworkStatusMonitor
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsFragmentViewmodel @Inject constructor(val sessionRepository: SessionRepository) : ViewModel() {
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val mutableLiveData = MutableLiveData<Boolean>()
    val networkStatusMonitor = NetworkStatusMonitor()

    init {
        viewModelScope.launch {
            networkStatusMonitor.isConnected.collect {
                mutableLiveData.value = it
            }
        }
    }

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
            viewModelScope.launch {
                firebaseAuth.signOut()
                sessionRepository.clearSession()
                onLogOutSuccessful.onSuccess()
            }
        } else {
            viewModelScope.launch {
                sessionRepository.clearSession()
                onLogOutSuccessful.onSuccess()
            }
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
