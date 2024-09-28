package com.denproj.posmanongjaks.repository.base;

import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.util.OnDataReceived;

import java.util.List;

public interface ProductRepository extends ImageRepository {

    void fetchProductsFromGlobal(OnDataReceived<List<Product>> onDataReceived);
    void fetchProductsFromBranch(String branchId, OnDataReceived<List<Product>> onDataReceived);

    void insertProduct(String branchId, Product product, OnDataReceived<Void> onDataReceived);

}
