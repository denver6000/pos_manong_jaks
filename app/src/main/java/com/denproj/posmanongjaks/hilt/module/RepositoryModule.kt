package com.denproj.posmanongjaks.hilt.module

import android.content.Context
import com.denproj.posmanongjaks.hilt.qualifier.FirebaseImpl
import com.denproj.posmanongjaks.hilt.qualifier.FirestoreImpl
import com.denproj.posmanongjaks.repository.base.AddOnsRepository
import com.denproj.posmanongjaks.repository.base.BranchRepository
import com.denproj.posmanongjaks.repository.base.ItemRepository
import com.denproj.posmanongjaks.repository.base.ProductRepository
import com.denproj.posmanongjaks.repository.base.RoleRepository
import com.denproj.posmanongjaks.repository.base.SaleRepository
import com.denproj.posmanongjaks.repository.base.StockReportRepository
import com.denproj.posmanongjaks.repository.base.UserRepository
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseAddonRepository
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseBranchRepository
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseItemRepository
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseProductRepository
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseRoleRepository
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseSaleRepository
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseUserRepository
import com.denproj.posmanongjaks.repository.firestoreImpl.FirestoreSaleRepository
import com.denproj.posmanongjaks.repository.firestoreImpl.FirestoreStockReportRepository
import com.denproj.posmanongjaks.repository.impl.SessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    //Firebase Imps
    @Provides
    @FirebaseImpl
    fun provideFirebaseUserRepo(): UserRepository {
        return FirebaseUserRepository()
    }

    @Provides
    @FirebaseImpl
    fun provideFirebaseBranchRepo(): BranchRepository {
        return FirebaseBranchRepository()
    }

    @Provides
    @FirebaseImpl
    fun provideFirebaseRoleRepo(): RoleRepository {
        return FirebaseRoleRepository()
    }

    @Provides
    @FirebaseImpl
    fun provideFirebaseSaleRepository(): SaleRepository {
        return FirebaseSaleRepository()
    }

    @Provides
    @FirebaseImpl
    fun provideFirebaseProductRepository(): ProductRepository {
        return FirebaseProductRepository()
    }

    @Provides
    @FirebaseImpl
    fun provideFirebaseAddOnRepository(): AddOnsRepository {
        return FirebaseAddonRepository()
    }

    @Provides
    @FirebaseImpl
    fun provideFirebaseItemRepository(): ItemRepository {
        return FirebaseItemRepository()
    }

    // Repository

    @Provides
    fun provideSessionRepository(@ApplicationContext context: Context): SessionRepository {
        return SessionRepository(context)
    }

    @Provides
    @FirestoreImpl
    fun provideFirestoreItemRepo(): ItemRepository {
        return com.denproj.posmanongjaks.repository.firestoreImpl.FirestoreItemRepository()
    }


    @Provides
    @FirestoreImpl
    fun provideFirestoreProductRepo(): ProductRepository {
        return com.denproj.posmanongjaks.repository.firestoreImpl.FirestoreProductRepository()

    }

    @Provides
    @FirestoreImpl
    fun provideAddOnsRepo(): AddOnsRepository {
        return com.denproj.posmanongjaks.repository.firestoreImpl.FirestoreAddOnRepository()
    }

    @Provides
    @FirestoreImpl
    fun provideFirestoreSaleRepo(): FirestoreSaleRepository {
        return FirestoreSaleRepository()
    }

    @Provides
    @FirestoreImpl
    fun provideFirestoreStockReportRepo(): StockReportRepository {
        return FirestoreStockReportRepository()
    }
}
