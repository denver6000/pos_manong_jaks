package com.denproj.posmanongjaks.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.hilt.qualifier.FirebaseImpl;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.repository.base.SaleRepository;
import com.denproj.posmanongjaks.util.OnFetchFailed;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SaleHistoryViewModel extends ViewModel {

    SaleRepository saleRepository;

    @Inject
    public SaleHistoryViewModel (@FirebaseImpl SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public LiveData<List<Sale>> getAllSalesRecord(String branchId, OnFetchFailed onFetchFailed) {
        return saleRepository.getAllRecordedSalesOnBranch(branchId, onFetchFailed);
    }



}
