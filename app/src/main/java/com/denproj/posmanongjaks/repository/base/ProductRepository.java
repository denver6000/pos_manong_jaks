package com.denproj.posmanongjaks.repository.base;

import androidx.lifecycle.MutableLiveData;

import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.util.OnFetchFailed;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import kotlin.NotImplementedError;

public interface ProductRepository {

    default MutableLiveData<List<Product>> observeProductList(String branchId, OnFetchFailed onFetchFailed) {
        throw new NotImplementedError();
    }

    CompletableFuture<List<Product>> fetchProductsFromBranch(String branchId);
    CompletableFuture<Void> insertProduct(String branchId, List<Product> products);

    default CompletableFuture<Void> clearProductList() {
        return null;
    }


    default void func3() {

    }

}
