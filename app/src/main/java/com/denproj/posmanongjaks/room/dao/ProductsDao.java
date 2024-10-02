package com.denproj.posmanongjaks.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.model.Sale;

import java.util.List;

@Dao
public interface ProductsDao {


    @Query("UPDATE Sale SET saleAmount = saleAmount + :amount WHERE productId = :productId")
    void updateSaleAmountOfProduct(int amount, int productId);

    @Query("SELECT EXISTS(SELECT * FROM Sale WHERE productId = :productId)")
    Boolean checkIfProductIsSold(int productId);

    @Insert
    void insertSale(Sale sale);

    @Query("SELECT * FROM Sale WHERE Sale.productId = :productId")
    List<Sale> getSaleWithProductId(int productId);

    @Insert
    void insertProduct(Product product);

}
