package com.denproj.posmanongjaks.repository.base;

import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.model.Recipe;
import com.denproj.posmanongjaks.util.OnDataReceived;

import java.util.HashMap;
import java.util.List;

public interface ProductRepository extends RecipeRepository, ImageRepository{

    void fetchProductsFromGlobal(OnDataReceived<List<Product>> onDataReceived);
    void fetchProductsFromBranch(OnDataReceived<List<Product>> onDataReceived);
    void insertProductToBranch(Product product, OnDataReceived<Void> onDataReceived);
    void insertProductToGlobalList(Product product, OnDataReceived<Void> onDataReceived) throws Exception;
    void loadRecipeIntoBranchStocks(HashMap<String, Recipe> recipes, OnDataReceived<Void> onDataReceived) throws Exception;
}
