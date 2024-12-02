package com.denproj.posmanongjaks.repository.firebaseImpl

import com.denproj.posmanongjaks.model.User
import com.denproj.posmanongjaks.repository.base.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.concurrent.CompletableFuture

class FirebaseUserRepository : UserRepository {
    var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override val userByUid: CompletableFuture<User?>
        get() {
            val firebaseUser = firebaseAuth.currentUser
            val completableFuture =
                CompletableFuture<User?>()
            val uid = firebaseUser!!.uid
            firestore.collection(PATH_TO_USER)
                .whereEqualTo("user_id", uid).get()
                .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                    if (queryDocumentSnapshots.documents.isEmpty()) {
                        completableFuture.completeExceptionally(Exception("User Not Registered as A staff or admin."))
                    } else {
                        completableFuture.complete(
                            queryDocumentSnapshots.documents[0].toObject(
                                User::class.java
                            )
                        )
                    }
                }
            return completableFuture
        }

    companion object {
        const val PATH_TO_USER: String = "users"
    }
}
