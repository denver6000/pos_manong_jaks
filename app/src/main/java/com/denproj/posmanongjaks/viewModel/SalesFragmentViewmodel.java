package com.denproj.posmanongjaks.viewModel;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.repository.base.AddOnsRepository;
import com.denproj.posmanongjaks.repository.base.ProductRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.util.OnUpdateUI;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class SalesFragmentViewmodel extends ViewModel {
    ProductRepository productRepository;
    AddOnsRepository addOnsRepository;

    public ObservableField<String> productName = new ObservableField<>("");
    public ObservableField<String> productPrice = new ObservableField<>("");

    public void loadProductsOfBranch(String branchId, OnUpdateUI<List<Product>> listOnUpdateUI) {
        productRepository.fetchProductsFromBranch(branchId)
                .thenAccept(listOnUpdateUI::onSuccess)
                .exceptionally(throwable -> {
                    listOnUpdateUI.onFail(new Exception(throwable));
                    return null;
                });
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
