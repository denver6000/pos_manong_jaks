package com.denproj.posmanongjaks.viewModel;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.hilt.qualifier.FirebaseImpl;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.repository.base.AddOnsRepository;
import com.denproj.posmanongjaks.repository.base.ProductRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.util.OnFetchFailed;
import com.denproj.posmanongjaks.util.OnUpdateUI;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SalesFragmentViewmodel extends ViewModel {
    ProductRepository productRepository;
    AddOnsRepository addOnsRepository;

    @Inject
    public SalesFragmentViewmodel(
            @FirebaseImpl ProductRepository productRepository,
            @FirebaseImpl AddOnsRepository addOnsRepository) {
        this.productRepository = productRepository;
        this.addOnsRepository = addOnsRepository;
    }

    public void loadProductsOfBranch(String branchId, OnUpdateUI<List<Product>> listOnUpdateUI) {
        productRepository.fetchProductsFromBranch(branchId)
                .thenAccept(listOnUpdateUI::onSuccess)
                .exceptionally(throwable -> {
                    listOnUpdateUI.onFail(new Exception(throwable));
                    return null;
                });
    }

    public MutableLiveData<List<Product>> observeProductListOfBranch(String branchId, OnFetchFailed onFetchFailed) {
        return productRepository.observeProductList(branchId, onFetchFailed);
    }

    public MutableLiveData<List<Item>> observeAddOnsListOfBranch(String branchId, OnFetchFailed onFetchFailed) {
        return addOnsRepository.observeAddOnsList(branchId, onFetchFailed);
    }

    public void loadAddOns(String branchId, OnUpdateUI<List<Item>> onUpdateUI) {
        addOnsRepository.getAddOnsRepository(branchId, new OnDataReceived<List<Item>>() {
            @Override
            public void onSuccess(List<Item> result) {
                onUpdateUI.onSuccess(result);
            }

            @Override
            public void onFail(Exception e) {
                onUpdateUI.onFail(e);
            }
        });
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public AddOnsRepository getAddOnsRepository() {
        return addOnsRepository;
    }

    public void setAddOnsRepository(AddOnsRepository addOnsRepository) {
        this.addOnsRepository = addOnsRepository;
    }
}
