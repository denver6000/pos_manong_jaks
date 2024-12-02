package com.denproj.posmanongjaks.repository.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.denproj.posmanongjaks.session.SessionSimple
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session");
class SessionRepository(private val context: Context) {
    val USER_ID_KEY = stringPreferencesKey("userId")
    val NAME_KEY = stringPreferencesKey("name")
    val BRANCH_ID_KEY = stringPreferencesKey("branchId")
    val BRANCH_NAME_KEY = stringPreferencesKey("branchName")
    val ROLE_NAME_KEY = stringPreferencesKey("roleName")

    val session: Flow<SessionSimple>
        get() = context.dataStore.data.map { preferences ->
            SessionSimple(
                userId = preferences[USER_ID_KEY] ?: "",
                name = preferences[NAME_KEY] ?: "",
                branchId = preferences[BRANCH_ID_KEY] ?: "",
                branchName = preferences[BRANCH_NAME_KEY] ?: "",
                roleName = preferences[ROLE_NAME_KEY] ?: ""
            )
        }


    suspend fun getSession(): SessionSimple? {
        val preferences = context.dataStore.data.firstOrNull()
        return if (preferences != null) {
            SessionSimple(
                userId = preferences[USER_ID_KEY] ?: "",
                name = preferences[NAME_KEY] ?: "",
                branchId = preferences[BRANCH_ID_KEY] ?: "",
                branchName = preferences[BRANCH_NAME_KEY] ?: "",
                roleName = preferences[ROLE_NAME_KEY] ?: ""
            )
        } else {
            null
        }
    }

    suspend fun saveSession(session: SessionSimple) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = session.userId!!
            preferences[NAME_KEY] = session.name!!
            preferences[BRANCH_ID_KEY] = session.branchId!!
            preferences[BRANCH_NAME_KEY] = session.branchName!!
            preferences[ROLE_NAME_KEY] = session.roleName!!
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}