package com.denproj.posmanongjaks.repository.base;

import androidx.lifecycle.LiveData;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.ProductWrapper;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.model.SaleItem;
import com.denproj.posmanongjaks.model.SaleProduct;
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseSaleRepository;
import com.denproj.posmanongjaks.util.OnFetchFailed;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SaleRepository {
    void processSale(String branchId, HashMap<Long, ProductWrapper> selectedItemToSel, HashMap<Item, Integer> addOns, int year, int month, int day, Double total, Double amountToBePaid, FirebaseSaleRepository.OnSaleStatus onSaleStatus);
    LiveData<List<SaleItem>> getAllAddOnsWithSaleId(Integer saleId);
    LiveData<List<SaleProduct>> getAllProductsWithSaleId(Integer saleId);
    LiveData<List<Sale>> getAllRecordedSalesOnBranch(String branchId, OnFetchFailed onFetchFailed);
}
