package com.denproj.posmanongjaks.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.denproj.posmanongjaks.model.Item;

import java.util.List;

@Dao
public interface ItemsDao {

    @Insert
    void insertItems(Item... item);


    @Query("SELECT * FROM Item")
    List<Item> getAllItems();

}
