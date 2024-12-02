package com.denproj.posmanongjaks.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denproj.posmanongjaks.repository.impl.SessionRepository
import com.denproj.posmanongjaks.session.Session
import com.denproj.posmanongjaks.session.SessionSimple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeActivityViewmodel @Inject constructor(
    var sessionRepository: SessionRepository
) :
    ViewModel() {
    private var sessionMutableStateFlow: MutableStateFlow<SessionSimple?> = MutableStateFlow(null)
    var sessionStateFlow: StateFlow<SessionSimple?> = sessionMutableStateFlow



    fun saveSession(session: SessionSimple) {
        viewModelScope.launch {
            sessionRepository.saveSession(session)
        }
    }

    fun updateLiveSession (session: SessionSimple) {
        sessionMutableStateFlow.value = session
    }
}
