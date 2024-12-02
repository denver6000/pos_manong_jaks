package com.denproj.posmanongjaks.repository.base

import com.denproj.posmanongjaks.model.Branch
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseBranchRepository.Companion.PATH_TO_BRANCHES
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CompletableFuture
import kotlin.jvm.Throws

interface BranchRepository {
    val firestore: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    fun getBranch(branchId: String?): CompletableFuture<Branch?>?

    @Throws(Exception::class)
    suspend fun getBranchFlow(branchId: String): Branch? {
        val branchesResult = firestore.collection(PATH_TO_BRANCHES).whereEqualTo("branch_id", branchId).get()
            .await()
        if (branchesResult.isEmpty){
            return null
        }
        val branch = branchesResult.documents[0].toObject(Branch::class.java)
        return branch
    }
}
