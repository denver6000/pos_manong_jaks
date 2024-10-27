package com.denproj.posmanongjaks.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.hilt.qualifier.OfflineImpl;
import com.denproj.posmanongjaks.hilt.qualifier.OnlineImpl;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.repository.base.ItemRepository;
import com.denproj.posmanongjaks.repository.base.ProductRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    MutableLiveData<Boolean> booleanLiveData = new MutableLiveData<>();
    ItemRepository itemRepository;
    ItemRepository itemOfflineRepository;
    ProductRepository productRepository;
    ProductRepository productOfflineRepository;
    @Inject
    public MainViewModel(@OnlineImpl ProductRepository productRepository, @OfflineImpl ProductRepository productOfflineRepository, @OnlineImpl ItemRepository itemRepository, @OfflineImpl ItemRepository itemOfflineRepository) {

        this.productRepository = productRepository;
        this.productOfflineRepository = productOfflineRepository;
        this.itemOfflineRepository = itemOfflineRepository;
        this.itemRepository = itemRepository;
    }

    public CompletableFuture<Void> sync(String branchId){
        CompletableFuture<Void> syncCompletableFuture = new CompletableFuture<>();

        clearLocalCache().thenAccept(new Consumer<Void>() {
            @Override
            public void accept(Void unused) {
                fetchItemsAndInsert(branchId).thenAccept(unused1 -> {
                    fetchProductsAndInsert(branchId).thenAccept(syncCompletableFuture::complete);
                });
            }
        });

        return syncCompletableFuture;
    }

    private CompletableFuture<Void> fetchProductsAndInsert(String branchId) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        productRepository
                .fetchProductsFromBranch(branchId)
                .thenAcceptAsync(products -> {
                    productOfflineRepository
                            .insertProduct(branchId, products)
                            .thenAccept(completableFuture::complete);
                }).exceptionally(throwable -> {
                    completableFuture.completeExceptionally(throwable);
                    return null;
                });
        return completableFuture;
    }

    private CompletableFuture<Void> fetchItemsAndInsert(String branchId) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        itemRepository.fetchItemsFromBranch(branchId)
                .thenAccept(items -> itemOfflineRepository
                        .insertItem(items)
                        .thenAccept(completableFuture::complete))
                .exceptionally(throwable -> {
                    completableFuture.completeExceptionally(throwable);
                    return null;
                });
        return completableFuture;
    }

    private CompletableFuture<Void> clearLocalCache() {
        CompletableFuture<Void> clearCacheLocalCacheFuture = new CompletableFuture<>();
        productOfflineRepository.clearProductList().thenAccept(unused -> {
            itemOfflineRepository.clearItems().thenAccept(clearCacheLocalCacheFuture::complete);
        }).exceptionally(throwable -> {
            clearCacheLocalCacheFuture.completeExceptionally(throwable);
            return null;
        });

        return clearCacheLocalCacheFuture;
    }

    public MutableLiveData<Boolean> getBooleanLiveData() {
        return this.booleanLiveData;
    }

    public void setBooleanLiveData(MutableLiveData<Boolean> booleanLiveData) {
        this.booleanLiveData = booleanLiveData;
    }
}
