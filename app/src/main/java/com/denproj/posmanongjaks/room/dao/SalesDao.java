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
    LiveData<List<SaleItem>> getAllSaleItemWithSaleIdAsync(Integer saleId);
    @Query("SELECT * FROM SaleProduct WHERE saleId = :saleId")
    LiveData<List<SaleProduct>> getAllSaleProductWithSaleIdAsync(Integer saleId);
    @Query("SELECT * FROM Sale WHERE Sale.branchId = :branchId")
    List<Sale> getAllRecordedSalesAsync(String branchId);

    @Query("SELECT * FROM SaleItem WHERE saleId = :saleId")
    List<SaleItem> getAllSaleItemWithSaleIdSync(Integer saleId);
    @Query("SELECT * FROM SaleProduct WHERE saleId = :saleId")
    List<SaleProduct> getAllSaleProductWithSaleIdSync(Integer saleId);
    @Query("SELECT * FROM Sale")
    List<Sale> getAllRecordedSalesSync();
    @Query("DELETE FROM Sale")
    void clearSales();
    @Query("DELETE FROM Sale WHERE saleId = :saleId")
    void removeSaleWithSaleId(Integer saleId);
    @Query("DELETE FROM SaleProduct WHERE saleId = :saleId")
    void removeSaleProductSaleId(Integer saleId);
    @Query("DELETE FROM SaleItem WHERE saleId = :saleId")
    void removeSaleItemWithSaleId(Integer saleId);

}
