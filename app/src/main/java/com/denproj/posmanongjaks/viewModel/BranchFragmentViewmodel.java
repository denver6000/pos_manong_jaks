package com.denproj.posmanongjaks.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.repository.base.ItemRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.util.OnUpdateUI;

import java.util.HashMap;
import java.util.List;

public class BranchFragmentViewmodel extends ViewModel {

    ItemRepository itemRepository;

    public BranchFragmentViewmodel() {
    }

    public ItemRepository getItemRepository() {
        return itemRepository;
    }

    public void setItemRepository(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void getRealtimeBranchStocks(OnUpdateUI<List<Item>> onUpdateUI) {
        itemRepository.fetchItemsFromBranch(new OnDataReceived<List<Item>>() {
            @Override
            public void onSuccess(List<Item> result) {
                onUpdateUI.onSuccess(result);
            }

            @Override
            public void onFail(Exception e) {
                onUpdateUI.onFail(new Exception("Something went wrong with fetching the items list."));
            }
        });
    }

}
