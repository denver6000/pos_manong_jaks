package com.denproj.posmanongjaks.repository.base;

import androidx.lifecycle.LiveData;

import com.denproj.posmanongjaks.model.ProductWrapper;
import com.denproj.posmanongjaks.model.CompleteSaleInfo;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.model.SaleItem;
import com.denproj.posmanongjaks.model.SaleProduct;
import com.denproj.posmanongjaks.util.OnDataReceived;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import kotlin.NotImplementedError;

public interface SaleRepository {
    void insertSaleRecord(String branchId, HashMap<Long, ProductWrapper> selectedItemToSel, HashMap<Item, Integer> addOns, int year, int month, int day, Double total, Double amountToBePaid, OnDataReceived<CompleteSaleInfo> onDataReceived);
    LiveData<List<SaleItem>> getAllAddOnsWithSaleId(String saleId);
    LiveData<List<SaleProduct>> getAllProductsWithSaleId(String saleId);
    CompletableFuture<List<Sale>> getAllRecordedSalesOnBranch(String branchId);
    List<SaleItem> getAllSaleItemBySaleIdSync(String saleId);
    List<SaleProduct> getAllSaleProductBySaleIdSync(String saleId);
    default CompletableFuture<List<Sale>> getAllSalesRemote () {
        throw new NotImplementedError();
    }

}
