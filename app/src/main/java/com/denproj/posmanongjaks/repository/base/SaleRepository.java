package com.denproj.posmanongjaks.repository.base;

import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.util.OnDataReceived;

public interface SaleRepository {
    void insertSaleRecord(int amount, Product product, int year, int month, int day, OnDataReceived<Void> onDataReceived);
    void getSaleRecord(int productId, OnDataReceived<Sale> sale);
}
