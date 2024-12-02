package com.denproj.posmanongjaks.viewModel;

import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.hilt.qualifier.FirebaseImpl;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.ProductWrapper;
import com.denproj.posmanongjaks.repository.base.SaleRepository;
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseSaleRepository;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CheckOutDialogViewmodel extends ViewModel {
    SaleRepository salesRepository;

    @Inject
    public CheckOutDialogViewmodel(@FirebaseImpl SaleRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public void sell(String branchId, HashMap<Long, ProductWrapper> selectedItemsToSell, HashMap<Item, Integer> addOns, int year, int month, int day, Double total, Double payAmount, FirebaseSaleRepository.OnSaleStatus onSaleStatus) {
        salesRepository.processSale(branchId, selectedItemsToSell, addOns, year, month, day, total, payAmount, onSaleStatus);
    }

}
