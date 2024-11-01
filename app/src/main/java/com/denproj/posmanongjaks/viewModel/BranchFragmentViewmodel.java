package com.denproj.posmanongjaks.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.hilt.qualifier.FirebaseImpl;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.repository.base.ItemRepository;
import com.denproj.posmanongjaks.util.OnFetchFailed;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class BranchFragmentViewmodel extends ViewModel {

    ItemRepository itemRepository;

    @Inject
    public BranchFragmentViewmodel(
            @FirebaseImpl ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public MutableLiveData<List<Item>> observeRealtimeBranchStocks(String branchId, OnFetchFailed onFetchFailed) {
        return itemRepository.observeItemsFromBranch(branchId, onFetchFailed);
    }

}
