package com.denproj.posmanongjaks.repository.base;

import androidx.lifecycle.LiveData;

import com.denproj.posmanongjaks.model.AddOn;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.model.SaleItem;
import com.denproj.posmanongjaks.model.SaleProduct;
import com.denproj.posmanongjaks.room.view.SalesWithProduct;
import com.denproj.posmanongjaks.util.OnDataReceived;

import java.util.HashMap;
import java.util.List;

public interface SaleRepository {
    void insertSaleRecord(String branchId, HashMap<String, AddOn> selectedItemToSel, HashMap<Item, Integer> addOns, int year, int month, int day, Double total, Double amountToBePaid, OnDataReceived<Void> onDataReceived);
    void getSaleRecord(int productId, OnDataReceived<Sale> sale);
    List<SalesWithProduct> getAllSalesRecord();
    LiveData<List<SaleItem>> getAllAddOnsWithSaleId(String saleId);
    LiveData<List<SaleProduct>> getAllProductsWithSaleId(String saleId);
    LiveData<List<Sale>> getAllRecordedSales();

    List<SaleItem> getAllSaleItemBySaleIdSync(String saleId);

    List<SaleProduct> getAllSaleProductBySaleIdSync(String saleId);

}
