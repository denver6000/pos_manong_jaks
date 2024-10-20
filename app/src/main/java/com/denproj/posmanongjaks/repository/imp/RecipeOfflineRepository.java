package com.denproj.posmanongjaks.repository.imp;

import com.denproj.posmanongjaks.repository.base.RecipeRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;

public class RecipeOfflineRepository implements RecipeRepository {
    @Override
    public void insertRecipeToBranch(String branchId, String itemId, OnDataReceived<Void> onDataReceived) throws Exception {

    }
}
