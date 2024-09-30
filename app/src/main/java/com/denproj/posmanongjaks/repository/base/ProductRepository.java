package com.denproj.posmanongjaks.repository.base;

import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.model.Recipe;
import com.denproj.posmanongjaks.util.OnDataReceived;

import java.util.HashMap;
import java.util.List;

public interface ProductRepository extends ImageRepository, RecipeRepository{

    void fetchProductsFromGlobal(OnDataReceived<List<Product>> onDataReceived);
    void fetchProductsFromBranch(String branchId, OnDataReceived<List<Product>> onDataReceived);
    void insertProductToBranch(String branchId, Product product, OnDataReceived<Void> onDataReceived);
    void insertProductToGlobalList(Product product, OnDataReceived<Void> onDataReceived);
    void loadRecipeIntoBranchStocks(String branchId, HashMap<String, Recipe> recipes, OnDataReceived<Void> onDataReceived);
}
