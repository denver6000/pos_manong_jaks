package com.denproj.posmanongjaks.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Product;

import java.util.List;

@Dao
public interface ProductsDao {

    @Insert
    void insertProducts(Product... product);

    @Query("SELECT * FROM Product")
    List<Product> getAllProducts();

    @Query("SELECT * FROM Item WHERE ads_on = TRUE")
    List<Item> getAllAdOns();

    @Query("DELETE FROM Product")
    void clearProducts();

}
