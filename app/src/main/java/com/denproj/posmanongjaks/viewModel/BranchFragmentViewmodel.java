package com.denproj.posmanongjaks.viewModel;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.repository.base.ItemRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.util.OnUpdateUI;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class BranchFragmentViewmodel extends ViewModel {

    public MutableLiveData<HashMap<Integer, Item>> listMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> branchIdLiveData = new MutableLiveData<>("");

    ItemRepository itemRepository;

    @Inject
    public BranchFragmentViewmodel(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void loadGlobalItemList(OnUpdateUI<List<Item>> onUpdateUI) {
        this.itemRepository.fetchItemsFromGlobal(new OnDataReceived<List<Item>>() {
            @Override
            public void onSuccess(List<Item> result) {
                onUpdateUI.onSuccess(result);
            }

            @Override
            public void onFail(Exception e) {
                onUpdateUI.onFail(new Exception("Something went wrong with fetching items list."));
            }
        });

    }

    public void saveSelectionToBranchList(String branchId, HashMap<Integer, Item> selectedItemsMap, OnUpdateUI<Void> onUpdateUI) {
        itemRepository.saveSelectionToBranchList(branchId, selectedItemsMap, new OnDataReceived<Void>() {
            @Override
            public void onSuccess(Void result) {
                onUpdateUI.onSuccess(result);
            }

            @Override
            public void onFail(Exception e) {
                onUpdateUI.onFail(new Exception("Selections were not saved."));
            }
        });
    }

    public void getRealtimeBranchStocks(String branchId, OnUpdateUI<List<Item>> onUpdateUI) {
        itemRepository.fetchItemsFromBranch(branchId, new OnDataReceived<List<Item>>() {
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
