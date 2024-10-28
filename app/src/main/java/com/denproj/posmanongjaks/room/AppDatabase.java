package com.denproj.posmanongjaks.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.denproj.posmanongjaks.model.Branch;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.model.Role;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.model.SaleItem;
import com.denproj.posmanongjaks.model.SaleProduct;
import com.denproj.posmanongjaks.model.SavedLoginCredentials;
import com.denproj.posmanongjaks.model.User;
import com.denproj.posmanongjaks.room.dao.ItemsDao;
import com.denproj.posmanongjaks.room.dao.ProductsDao;
import com.denproj.posmanongjaks.room.dao.SalesDao;
import com.denproj.posmanongjaks.room.dao.UserDao;
import com.denproj.posmanongjaks.room.dao.UserInfoDao;

@Database(entities = {Item.class,
        Product.class,
        Sale.class,
        SaleItem.class,
        SaleProduct.class,
        SavedLoginCredentials.class,
        User.class,
        Role.class,
        Branch.class}, exportSchema = false, version = 17)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase database = null;

    public static AppDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, AppDatabase.class, "AppDatabase")
                    .fallbackToDestructiveMigration().build();
        }
        return database;
    }

    public abstract SalesDao getSalesDao();

    public abstract ProductsDao getProductsDao();

    public abstract ItemsDao getItemsDao();

    public abstract UserDao getUserDao();

    public abstract UserInfoDao getUserInfoDao();
}
