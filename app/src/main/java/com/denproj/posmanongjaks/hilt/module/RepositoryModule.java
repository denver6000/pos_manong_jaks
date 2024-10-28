package com.denproj.posmanongjaks.hilt.module;

import com.denproj.posmanongjaks.hilt.qualifier.FirebaseImpl;
import com.denproj.posmanongjaks.hilt.qualifier.OfflineImpl;
import com.denproj.posmanongjaks.hilt.qualifier.OnlineImpl;
import com.denproj.posmanongjaks.repository.base.AddOnsRepository;
import com.denproj.posmanongjaks.repository.base.BranchRepository;
import com.denproj.posmanongjaks.repository.base.ItemRepository;
import com.denproj.posmanongjaks.repository.base.LoginRepository;
import com.denproj.posmanongjaks.repository.base.ProductRepository;
import com.denproj.posmanongjaks.repository.base.RecipeRepository;
import com.denproj.posmanongjaks.repository.base.RoleRepository;
import com.denproj.posmanongjaks.repository.base.SaleRepository;
import com.denproj.posmanongjaks.repository.base.SavedLoginRepository;
import com.denproj.posmanongjaks.repository.base.UserRepository;
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseAddonRepository;
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseBranchRepository;
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseItemRepository;
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseProductRepository;
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseRoleRepository;
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseSaleRepository;
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseUserRepository;
import com.denproj.posmanongjaks.repository.imp.AddOnOfflineRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.AddOnsRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.BranchOnlineRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.ItemOfflineRepository;
import com.denproj.posmanongjaks.repository.imp.ItemRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.LoginRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.ProductOfflineRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.ProductRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.RecipeRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.RoleOfflineRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.RoleOnlineRepository;
import com.denproj.posmanongjaks.repository.imp.SaleRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.SalesOfflineRepositoryImpl;
import com.denproj.posmanongjaks.repository.imp.SavedLoginRepositoryImpl;
import com.denproj.posmanongjaks.room.AppDatabase;
import com.google.firebase.auth.FirebaseUser;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
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

    // Login Repos
    @Provides
    LoginRepository provideLoginRepository(@OnlineImpl RoleRepository roleRepository, @OnlineImpl BranchRepository branchRepository) {
        return new LoginRepositoryImpl(roleRepository, branchRepository);
    }

    @Provides
    SavedLoginRepository provideSavedLoginRepository (AppDatabase appDatabase) {
        return new SavedLoginRepositoryImpl(appDatabase.getUserInfoDao());
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

    //Branch Repository
    @Provides
    @OnlineImpl
    BranchRepository provideBranchOnlineRepository() {
        return new BranchOnlineRepositoryImpl();
    }

    @Provides
    @OfflineImpl
    BranchRepository provideBranchOfflineRepository() {
        return new BranchOnlineRepositoryImpl();
    }

    //Role Repo
    @Provides
    @OnlineImpl
    RoleRepository provideRoleRepository() {
        return new RoleOnlineRepository();
    }

    @Provides
    @OfflineImpl
    RoleRepository provideOfflineRoleRepository() {
        return new RoleOfflineRepositoryImpl();
    }

    //Firebase Imps

    @Provides
    @FirebaseImpl
    UserRepository provideFirebaseUserRepo() {
        return new FirebaseUserRepository();
    }

    @Provides
    @FirebaseImpl
    BranchRepository provideFirebaseBranchRepo () {
        return new FirebaseBranchRepository();
    }

    @Provides
    @FirebaseImpl
    RoleRepository provideFirebaseRoleRepo () {
        return new FirebaseRoleRepository();
    }

    @Provides
    @FirebaseImpl
    SaleRepository provideFirebaseSaleRepository () {
        return new FirebaseSaleRepository();
    }

    @Provides
    @FirebaseImpl
    ProductRepository provideFirebaseProductRepository () {
        return new FirebaseProductRepository();
    }

    @Provides
    @FirebaseImpl
    AddOnsRepository provideFirebaseAddOnRepository () {
        return new FirebaseAddonRepository();
    }

    @Provides
    @FirebaseImpl
    ItemRepository provideFirebaseItemRepository() {
        return new FirebaseItemRepository();
    }
}
