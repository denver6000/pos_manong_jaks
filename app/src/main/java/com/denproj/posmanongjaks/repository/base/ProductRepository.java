package com.denproj.posmanongjaks.repository.base;

import com.denproj.posmanongjaks.model.Product;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProductRepository {

    CompletableFuture<List<Product>> fetchProductsFromBranch(String branchId);
    CompletableFuture<Void> insertProduct(String branchId, List<Product> products);

    default CompletableFuture<Void> clearProductList() {
        return null;
    }


    default void func3() {

    }

}
