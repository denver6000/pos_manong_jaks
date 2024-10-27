package com.denproj.posmanongjaks.repository.base;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.util.OnDataReceived;

import java.util.List;

public interface AddOnsRepository {
    void getAddOnsRepository(String branchId, OnDataReceived<List<Item>> onDataReceived);
}
