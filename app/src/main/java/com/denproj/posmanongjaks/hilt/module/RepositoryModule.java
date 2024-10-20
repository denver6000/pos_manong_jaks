package com.denproj.posmanongjaks.hilt.module;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import com.denproj.posmanongjaks.hilt.qualifier.DynamicImpl;
import com.denproj.posmanongjaks.hilt.qualifier.OfflineImpl;
import com.denproj.posmanongjaks.hilt.qualifier.OnlineImpl;
import com.denproj.posmanongjaks.repository.base.AddOnsRepository;
import com.denproj.posmanongjaks.repository.base.ItemRepository;
import com.denproj.posmanongjaks.repository.base.LoginRepository;
import com.denproj.posmanongjaks.repository.base.ProductRepository;
import com.denproj.posmanongjaks.repository.base.RecipeRepository;
import com.denproj.posmanongjaks.repository.base.SaleRepository;
import com.denproj.posmanongjaks.repository.base.SavedLoginRepository;
import com.denproj.posmanongjaks.repository.imp.AddOnOfflineRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.AddOnsRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.ItemOfflineRepository;
import com.denproj.posmanongjaks.repository.imp.ItemRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.LoginRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.ProductOfflineRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.ProductRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.RecipeRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.SaleRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.SalesOfflineRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.SavedLoginRepositoryImpl;
import com.denproj.posmanongjaks.room.AppDatabase;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RepositoryModule {



    @Provides
    RecipeRepository provideRecipeRepository() { return new RecipeRepositoryImpl(); }

    @Provides
    SaleRepository provideSaleRepository(AppDatabase appDatabase) {
        return new SalesOfflineRepositoryImpl(appDatabase.getSalesDao());
    }

    @Provides
    @OnlineImpl
    AddOnsRepository provideAddOnsRepository() {
        return new AddOnsRepositoryImpl();
    }

    @Provides
    @OfflineImpl
    AddOnsRepository provideOfflineAddOnsRepository(AppDatabase appDatabase) {
        return new AddOnOfflineRepositoryImpl(appDatabase.getProductsDao());
    }

    // Product Online & Offline
    @Provides
    @OfflineImpl
    ProductRepository provideProductOfflineRepository (AppDatabase appDatabase) {
        return new ProductOfflineRepositoryImpl(appDatabase.getProductsDao());
    }

    @Provides
    @OnlineImpl
    ProductRepository provideProductOnlineRepository () {
        return new ProductRepositoryImpl();
    }

    // -----

    // Item Online & Offline
    @Provides
    @OnlineImpl
    ItemRepository provideItemRepository() {
        return new ItemRepositoryImpl();
    }

    @Provides
    @OfflineImpl
    ItemRepository provideItemOfflineRepository(AppDatabase appDatabase) {
        return new ItemOfflineRepository(appDatabase.getItemsDao());
    }
    // ----

//    @Provides
//    boolean provideConnectionState(@ApplicationContext Context context) {
//        ConnectivityManager connMgr =
//                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        boolean isWifiConn = false;
//        boolean isMobileConn = false;
//
//        for (Network network : connMgr.getAllNetworks()) {
//            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//                isWifiConn |= networkInfo.isConnected();
//            }
//            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
//                isMobileConn |= networkInfo.isConnected();
//            }
//        }
//
//        return isWifiConn || isMobileConn;
////
////        return networkInfo != null && networkInfo.isConnected();
//    }

//    @Provides
//    @DynamicImpl
//    ItemRepository providesItemDynamicRepository(boolean connectionState, AppDatabase appDatabase) {
//        if (!connectionState) {
//            return new ItemOfflineRepository(appDatabase.getItemsDao());
//        } else {
//            return new ItemRepositoryImpl();
//        }
//    }
//
//    @Provides
//    @DynamicImpl
//    ProductRepository providesProductDynamicRepository(boolean connectionState, AppDatabase appDatabase) {
//        if (!connectionState) {
//            return new ProductOfflineRepositoryImpl(appDatabase.getProductsDao());
//        } else {
//            return new ProductRepositoryImpl();
//        }
//    }


    // Login Repos

    @Provides
    LoginRepository provideLoginRepository() {
        return new LoginRepositoryImpl();
    }

    @Provides
    SavedLoginRepository provideSavedLoginRepository (AppDatabase appDatabase) {
        return new SavedLoginRepositoryImpl(appDatabase.getUserDao());
    }

    // Sale Repository



    @Provides
    @OnlineImpl
    SaleRepository provideOnlineSaleRepository () {
        return new SaleRepositoryImpl();
    }

    @Provides
    @OfflineImpl
    SaleRepository provideOOfflineSaleRepository (AppDatabase appDatabase) {
        return new SalesOfflineRepositoryImpl(appDatabase.getSalesDao());
    }

}
