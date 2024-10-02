package com.denproj.posmanongjaks.hilt.module;

import com.denproj.posmanongjaks.repository.base.ItemRepository;
import com.denproj.posmanongjaks.repository.base.LoginRepository;
import com.denproj.posmanongjaks.repository.base.ProductRepository;
import com.denproj.posmanongjaks.repository.base.RecipeRepository;
import com.denproj.posmanongjaks.repository.base.SaleRepository;
import com.denproj.posmanongjaks.repository.imp.ItemRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.LoginRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.ProductRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.RecipeRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.SalesRepositoryImpl;
import com.denproj.posmanongjaks.room.AppDatabase;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RepositoryModule {

    @Provides
    LoginRepository provideLoginRepository() {
        return new LoginRepositoryImpl();
    }

    @Provides
    ItemRepository provideItemRepository() {
        return new ItemRepositoryImpl();
    }

    @Provides
    ProductRepository provideProductRepository() {
        return new ProductRepositoryImpl();
    }

    @Provides
    RecipeRepository provideRecipeRepository() { return new RecipeRepositoryImpl(); }

    @Provides
    SaleRepository provideSaleRepository(AppDatabase appDatabase) {
        return new SalesRepositoryImpl(appDatabase.getProductsDao());
    }

}
