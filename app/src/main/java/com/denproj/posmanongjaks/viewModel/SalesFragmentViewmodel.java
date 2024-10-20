package com.denproj.posmanongjaks.viewModel;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.hilt.qualifier.DynamicImpl;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.repository.base.AddOnsRepository;
import com.denproj.posmanongjaks.repository.base.ProductRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.util.OnUpdateUI;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

public class SalesFragmentViewmodel extends ViewModel {
    ProductRepository productRepository;
    AddOnsRepository addOnsRepository;

    public ObservableField<String> productName = new ObservableField<>("");
    public ObservableField<String> productPrice = new ObservableField<>("");



    public void loadProductsOfBranch(OnUpdateUI<List<Product>> listOnUpdateUI) {
        productRepository.fetchProductsFromBranch(new OnDataReceived<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                Log.d("Test", result.size() + "");
                listOnUpdateUI.onSuccess(result);
            }

            @Override
            public void onFail(Exception e) {
                listOnUpdateUI.onFail(e);
            }
        });
    }


    public void loadAddOns(OnUpdateUI<List<Item>> onUpdateUI) {
        addOnsRepository.getAddOnsRepository(new OnDataReceived<List<Item>>() {
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
