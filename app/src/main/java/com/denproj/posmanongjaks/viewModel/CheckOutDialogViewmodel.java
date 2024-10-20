package com.denproj.posmanongjaks.viewModel;

import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.hilt.qualifier.OfflineImpl;
import com.denproj.posmanongjaks.model.AddOn;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.repository.base.SaleRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CheckOutDialogViewmodel extends ViewModel {


    SaleRepository salesRepository;

    @Inject
    public CheckOutDialogViewmodel(@OfflineImpl SaleRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public void sell(String branchId, HashMap<String, AddOn> selectedItemsToSell, HashMap<Item, Integer> addOns, int year, int month, int day, Double total, Double payAmount, OnDataReceived<Void> onDataReceived) {
        salesRepository.insertSaleRecord(branchId, selectedItemsToSell, addOns, year, month, day, total, payAmount, onDataReceived);
    }

}
