package com.denproj.posmanongjaks.repository.imp;

import androidx.lifecycle.LiveData;

import com.denproj.posmanongjaks.model.ProductWrapper;
import com.denproj.posmanongjaks.model.CompleteSaleInfo;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.model.SaleItem;
import com.denproj.posmanongjaks.model.SaleProduct;
import com.denproj.posmanongjaks.repository.base.SaleRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import kotlin.NotImplementedError;

public class SaleRepositoryImpl implements SaleRepository {

    public static final String SALES_RECORD_PATH = "sales_record_t_2";

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    @Override
    public void insertSaleRecord(String branchId, HashMap<Long, ProductWrapper> selectedItemToSel, HashMap<Item, Integer> addOns, int year, int month, int day, Double total, Double amountToBePaid, OnDataReceived<CompleteSaleInfo> onDataReceived) {
        throw new NotImplementedError();
    }

    @Override
    public LiveData<List<SaleItem>> getAllAddOnsWithSaleId(String saleId) {
        return null;
    }

    @Override
    public LiveData<List<SaleProduct>> getAllProductsWithSaleId(String saleId) {
        return null;
    }

    @Override
    public CompletableFuture<List<Sale>> getAllRecordedSalesOnBranch(String branchId) {
        CompletableFuture<List<Sale>> completableFuture = new CompletableFuture<>();
        firebaseDatabase.getReference(SALES_RECORD_PATH + "/" + branchId).get().addOnSuccessListener(dataSnapshot -> {
            List<Sale> sales = new ArrayList<>();
            dataSnapshot.getChildren().forEach(dataSnapshot1 -> {
                sales.add(dataSnapshot1.getValue(Sale.class));
            });
            completableFuture.complete(sales);
        }).addOnFailureListener(completableFuture::completeExceptionally);
        return completableFuture;
    }

    @Override
    public List<SaleItem> getAllSaleItemBySaleIdSync(String saleId) {
        return null;
    }

    @Override
    public List<SaleProduct> getAllSaleProductBySaleIdSync(String saleId) {
        return null;
    }


}
