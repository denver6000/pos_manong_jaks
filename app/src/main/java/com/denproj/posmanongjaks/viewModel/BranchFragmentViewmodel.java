package com.denproj.posmanongjaks.viewModel;

import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.repository.base.ItemRepository;
import com.denproj.posmanongjaks.util.OnUpdateUI;

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

    public void getRealtimeBranchStocks(String branchId, OnUpdateUI<List<Item>> onUpdateUI) {
        itemRepository.fetchItemsFromBranch(branchId).thenAccept(onUpdateUI::onSuccess).exceptionally(throwable -> {
            onUpdateUI.onFail(new Exception(throwable));
            return null;
        });
    }

}
