package com.denproj.posmanongjaks.hilt.module;


import android.content.Context;

import com.denproj.posmanongjaks.room.AppDatabase;
import com.denproj.posmanongjaks.room.dao.ItemsDao;
import com.denproj.posmanongjaks.room.dao.ProductsDao;
import com.denproj.posmanongjaks.room.dao.SalesDao;
import com.denproj.posmanongjaks.room.dao.UserDao;
import com.denproj.posmanongjaks.room.dao.UserInfoDao;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RoomModule {

    @Provides
    AppDatabase provideAppDatabase(@ApplicationContext Context context) {
        return AppDatabase.getInstance(context);
    }

    @Provides
    SalesDao provideSalesDao(AppDatabase appDatabase) {
        return appDatabase.getSalesDao();
    }

    @Provides
    ProductsDao provideProductsDao(AppDatabase appDatabase){
        return appDatabase.getProductsDao();
    }

    @Provides
    ItemsDao provideItemsDao(AppDatabase appDatabase) {return appDatabase.getItemsDao();}

    @Provides
    UserDao provideUserDao(AppDatabase appDatabase) {
        return appDatabase.getUserDao();
    }

    @Provides
    UserInfoDao provideUserInfoDao(AppDatabase appDatabase) {
        return appDatabase.getUserInfoDao();
    }



}
