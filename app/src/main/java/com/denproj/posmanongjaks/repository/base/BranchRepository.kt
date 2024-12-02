package com.denproj.posmanongjaks.repository.base;

import com.denproj.posmanongjaks.model.Branch;

import java.util.concurrent.CompletableFuture;

public interface BranchRepository {
    CompletableFuture<Branch> getBranch(String branchId);
}
