package com.denproj.posmanongjaks.repository.base

import com.denproj.posmanongjaks.model.User
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseUserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import java.lang.Thread.State
import java.util.concurrent.CompletableFuture
import kotlin.jvm.Throws

interface UserRepository {
    val userByUid: CompletableFuture<User?>?

    @Throws(Exception::class)
    suspend fun getUserFlow(firebaseUser: FirebaseUser): User {
        val firestore = FirebaseFirestore.getInstance()
        val uid = firebaseUser.uid
        val usersMatch = firestore.collection(FirebaseUserRepository.PATH_TO_USER).whereEqualTo("user_id", uid)
            .get().await()
        if (usersMatch.documents.isEmpty()) {
            throw (Exception("User Not Registered as A staff or admin."))
        }
        val user = usersMatch.documents[0].toObject(User::class.java) ?: throw (Exception("User Credential is Invalid. Contact System Admin"))
        return user
    }
}
