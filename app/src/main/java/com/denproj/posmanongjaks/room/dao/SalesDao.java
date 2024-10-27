package com.denproj.posmanongjaks.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.model.SaleItem;
import com.denproj.posmanongjaks.model.SaleProduct;

import java.util.List;

@Dao
public interface SalesDao {

    @Insert
    Long insertSale(Sale sale);

    @Insert
    Long insertProduct(Product product);

    @Insert
    void insertSaleItem(SaleItem saleItem);

    @Insert
    void insertSaleProduct(SaleProduct saleProduct);

    @Query("SELECT * FROM SaleItem WHERE saleId = :saleId")
    LiveData<List<SaleItem>> getAllSaleItemWithSaleIdAsync(String saleId);
    @Query("SELECT * FROM SaleProduct WHERE sale_id = :saleId")
    LiveData<List<SaleProduct>> getAllSaleProductWithSaleIdAsync(String saleId);
    @Query("SELECT * FROM Sale WHERE Sale.branchId = :branchId")
    List<Sale> getAllRecordedSalesAsync(String branchId);

    @Query("SELECT * FROM SaleItem WHERE saleId = :saleId")
    List<SaleItem> getAllSaleItemWithSaleIdSync(String saleId);
    @Query("SELECT * FROM SaleProduct WHERE sale_id = :saleId")
    List<SaleProduct> getAllSaleProductWithSaleIdSync(String saleId);
    @Query("SELECT * FROM Sale")
    List<Sale> getAllRecordedSalesSync();


    @Query("DELETE FROM Sale")
    void clearSales();

}
