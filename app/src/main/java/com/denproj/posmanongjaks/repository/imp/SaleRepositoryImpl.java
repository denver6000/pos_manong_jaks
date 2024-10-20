package com.denproj.posmanongjaks.repository.imp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.denproj.posmanongjaks.model.AddOn;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.model.SaleItem;
import com.denproj.posmanongjaks.model.SaleProduct;
import com.denproj.posmanongjaks.repository.base.SaleRepository;
import com.denproj.posmanongjaks.room.view.SalesWithProduct;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SaleRepositoryImpl implements SaleRepository {

    public static final String SALES_RECORD_PATH = "sales_record_t_2";

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    @Override
    public void insertSaleRecord(String branchId, HashMap<String, AddOn> selectedItemToSel, HashMap<Item, Integer> addOns, int year, int month, int day, Double total, Double amountToBePaid, OnDataReceived<Void> onDataReceived) {

    }

    @Override
    public void getSaleRecord(int productId, OnDataReceived<Sale> sale) {

    }

    @Override
    public List<SalesWithProduct> getAllSalesRecord() {

        return null;
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
    public LiveData<List<Sale>> getAllRecordedSales() {
        DatabaseReference databaseReference = firebaseDatabase.getReference(SALES_RECORD_PATH);
        MutableLiveData<List<Sale>> liveSaleData = new MutableLiveData<>();
        List<Sale> saleList = new ArrayList<>();
        databaseReference.get().addOnSuccessListener(sales -> {
            for (DataSnapshot child : sales.getChildren()) {
                Sale sale = child.getValue(Sale.class);
                saleList.add(sale);
            }
            liveSaleData.setValue(saleList);
        });
        return liveSaleData;
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
