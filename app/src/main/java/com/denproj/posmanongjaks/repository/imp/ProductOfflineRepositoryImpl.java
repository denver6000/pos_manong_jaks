package com.denproj.posmanongjaks.repository.imp;

import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.model.Recipe;
import com.denproj.posmanongjaks.repository.base.ProductRepository;
import com.denproj.posmanongjaks.room.dao.ProductsDao;
import com.denproj.posmanongjaks.util.AsyncRunner;
import com.denproj.posmanongjaks.util.OnDataReceived;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;


public class ProductOfflineRepositoryImpl implements ProductRepository {

    ProductsDao productsDao;

    @Inject
    public ProductOfflineRepositoryImpl(ProductsDao productsDao) {
        this.productsDao = productsDao;
    }

    @Override
    public void fetchProductsFromGlobal(OnDataReceived<List<Product>> onDataReceived) {

    }

    @Override
    public void fetchProductsFromBranch(OnDataReceived<List<Product>> onDataReceived) {
        AsyncRunner.runAsync(new AsyncRunner.Runner<List<Product>>() {
            @Override
            public List<Product> onBackground() throws Exception {
                return productsDao.getAllProducts();
            }

            @Override
            public void onFinished(List<Product> result) {

            }

            @Override
            public void onUI(List<Product> result) {
                onDataReceived.onSuccess(result);
            }

            @Override
            public void onError(Exception e) {
                onDataReceived.onFail(e);
            }
        });
    }

    @Override
    public void insertProductToBranch(Product product, OnDataReceived<Void> onDataReceived) {
        AsyncRunner.runAsync(new AsyncRunner.Runner<Void>() {
            @Override
            public Void onBackground() {
                productsDao.insertProducts(product);
                return null;
            }

            @Override
            public void onFinished(Void result) {

            }

            @Override
            public void onUI(Void result) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public void insertProductToGlobalList(Product product, OnDataReceived<Void> onDataReceived) throws Exception {

    }

    @Override
    public void loadRecipeIntoBranchStocks(HashMap<String, Recipe> recipes, OnDataReceived<Void> onDataReceived) throws Exception {

    }

    @Override
    public void insertRecipeToBranch(String branchId, String itemId, OnDataReceived<Void> onDataReceived) {

    }

//    @Override
//    public void insertImage(Uri uri, String imageName, OnDataReceived<String> onDataReceived) {
//
//    }

    @Override
    public void getImage(String path, OnDataReceived<String> onDataReceived) throws Exception {

    }
}
