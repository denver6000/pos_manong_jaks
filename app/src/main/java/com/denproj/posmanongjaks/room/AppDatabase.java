package com.denproj.posmanongjaks.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.denproj.posmanongjaks.room.dao.ProductsDao;
import com.denproj.posmanongjaks.room.entity.Items;
import com.denproj.posmanongjaks.room.entity.Products;

@Database(entities = {Items.class, Products.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase database = null;

    public static AppDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, AppDatabase.class, "AppDatabase")
                    .fallbackToDestructiveMigration().build();
        }
        return database;
    }

    public abstract ProductsDao getProductsDao();

}
