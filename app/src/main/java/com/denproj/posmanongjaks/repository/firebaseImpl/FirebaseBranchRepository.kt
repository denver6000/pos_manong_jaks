package com.denproj.posmanongjaks.repository.firebaseImpl

import com.denproj.posmanongjaks.model.Branch
import com.denproj.posmanongjaks.repository.base.BranchRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.concurrent.CompletableFuture

class FirebaseBranchRepository : BranchRepository {
    override var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun getBranch(branchId: String?): CompletableFuture<Branch?>? {
        val branchCompletableFuture = CompletableFuture<Branch?>()
        firestore.collection(PATH_TO_BRANCHES).whereEqualTo("branch_id", branchId).get()
            .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                if (queryDocumentSnapshots.isEmpty) {
                    branchCompletableFuture.completeExceptionally(Exception("No Branch Associated With this account."))
                } else {
                    val branch =
                        queryDocumentSnapshots.documents[0].toObject(
                            Branch::class.java
                        )
                    branchCompletableFuture.complete(branch)
                }
            }
        return branchCompletableFuture
    }

    companion object {
        const val PATH_TO_BRANCHES = "branches"
    }
}
