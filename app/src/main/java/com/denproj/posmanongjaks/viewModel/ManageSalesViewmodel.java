package com.denproj.posmanongjaks.viewModel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.hilt.qualifier.FirebaseImpl;
import com.denproj.posmanongjaks.model.SaleItem;
import com.denproj.posmanongjaks.model.SaleProduct;
import com.denproj.posmanongjaks.repository.base.SaleRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManageSalesViewmodel extends ViewModel {


    SaleRepository saleRepository;
    @Inject
    public ManageSalesViewmodel(@FirebaseImpl SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }


    public LiveData<List<SaleProduct>> getSoldProductsAsync(Integer saleId) {
        return saleRepository.getAllProductsWithSaleId(saleId);
    }

    public LiveData<List<SaleItem>> getSoldAddOnsAsync(Integer saleId) {
        return saleRepository.getAllAddOnsWithSaleId(saleId);
    }

}
