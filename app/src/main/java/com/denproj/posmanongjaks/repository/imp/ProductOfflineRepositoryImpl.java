package com.denproj.posmanongjaks.repository.imp;

import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.repository.base.ProductRepository;
import com.denproj.posmanongjaks.room.dao.ProductsDao;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.inject.Inject;


public class ProductOfflineRepositoryImpl implements ProductRepository {

    ProductsDao productsDao;

    @Inject
    public ProductOfflineRepositoryImpl(ProductsDao productsDao) {
        this.productsDao = productsDao;
    }

    @Override
    public CompletableFuture<List<Product>> fetchProductsFromBranch(String branchId) {
        return CompletableFuture.supplyAsync(() -> productsDao.getAllProducts());
    }

    @Override
    public CompletableFuture<Void> insertProduct(String branchId, List<Product> products) {
        return CompletableFuture.supplyAsync(() -> {
            products.forEach(product -> productsDao.insertProducts(product));
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> clearProductList() {
        return CompletableFuture.supplyAsync(() -> {
            productsDao.clearProducts();
            return null;
        });
    }
}
