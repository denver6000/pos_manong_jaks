package com.denproj.posmanongjaks.hilt.module;


import android.content.Context;

import com.denproj.posmanongjaks.room.AppDatabase;
import com.denproj.posmanongjaks.room.dao.ProductsDao;

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
    ProductsDao provideProductsDao(AppDatabase appDatabase) {
        return appDatabase.getProductsDao();
    }
}
