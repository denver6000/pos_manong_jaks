package com.denproj.posmanongjaks.repository.base;

import com.denproj.posmanongjaks.util.OnDataReceived;

public interface RecipeRepository {
    void insertRecipeToBranch(String branchId, String itemId, OnDataReceived<Void> onDataReceived) throws Exception;
}
