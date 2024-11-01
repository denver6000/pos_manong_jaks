package com.denproj.posmanongjaks.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.hilt.qualifier.FirebaseImpl;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.repository.base.AddOnsRepository;
import com.denproj.posmanongjaks.repository.base.ProductRepository;
import com.denproj.posmanongjaks.util.OnFetchFailed;

import java.util.List;

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

    public MutableLiveData<List<Product>> observeProductListOfBranch(String branchId, OnFetchFailed onFetchFailed) {
        return productRepository.observeProductList(branchId, onFetchFailed);
    }

    public MutableLiveData<List<Item>> observeAddOnsListOfBranch(String branchId, OnFetchFailed onFetchFailed) {
        return addOnsRepository.observeAddOnsList(branchId, onFetchFailed);
    }
}
