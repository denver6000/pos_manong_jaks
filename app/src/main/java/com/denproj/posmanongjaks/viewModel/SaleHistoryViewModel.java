package com.denproj.posmanongjaks.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.hilt.qualifier.OnlineImpl;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.repository.base.SaleRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SaleHistoryViewModel extends ViewModel {

    SaleRepository saleRepository;

    @Inject
    public SaleHistoryViewModel (@OnlineImpl SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public CompletableFuture<List<Sale>> getAllSalesRecord(String branchId) {
        return saleRepository.getAllRecordedSalesOnBranch(branchId);
    }


}
