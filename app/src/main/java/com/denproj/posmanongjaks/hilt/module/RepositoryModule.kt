package com.denproj.posmanongjaks.hilt.module;

import com.denproj.posmanongjaks.hilt.qualifier.FirebaseImpl;
import com.denproj.posmanongjaks.repository.base.AddOnsRepository;
import com.denproj.posmanongjaks.repository.base.BranchRepository;
import com.denproj.posmanongjaks.repository.base.ItemRepository;
import com.denproj.posmanongjaks.repository.base.ProductRepository;
import com.denproj.posmanongjaks.repository.base.RoleRepository;
import com.denproj.posmanongjaks.repository.base.SaleRepository;
import com.denproj.posmanongjaks.repository.base.UserRepository;
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseAddonRepository;
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseBranchRepository;
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseItemRepository;
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseProductRepository;
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseRoleRepository;
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseSaleRepository;
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseUserRepository;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RepositoryModule {

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
