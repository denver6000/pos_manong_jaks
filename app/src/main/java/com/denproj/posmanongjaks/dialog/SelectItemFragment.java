package com.denproj.posmanongjaks.dialog;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denproj.posmanongjaks.adapter.ItemSelectionRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentSelectItemBinding;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.viewModel.BranchFragmentViewmodel;

import java.util.List;


public class SelectItemFragment extends DialogFragment {
    BranchFragmentViewmodel viewmodel;
    ItemSelectionRecyclerViewAdapter adapter;
    String branchId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public SelectItemFragment(String branchId) {
        this.branchId = branchId;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSelectItemBinding binding = FragmentSelectItemBinding.inflate(getLayoutInflater());
        viewmodel = new ViewModelProvider(requireParentFragment()).get(BranchFragmentViewmodel.class);
        viewmodel.loadGlobalItemList(new OnDataReceived<List<Item>>() {
            @Override
            public void onSuccess(List<Item> result) {
                binding.globalItemList.setLayoutManager(new LinearLayoutManager(requireContext()));
                adapter = new ItemSelectionRecyclerViewAdapter(result);
                binding.globalItemList.setAdapter(adapter);
            }

            @Override
            public void onFail(Exception e) {

            }
        });

        binding.saveSelectedBtn.setOnClickListener(view -> {
            viewmodel.saveSelectionToBranchList(branchId, adapter.getSelectedItems(), new OnDataReceived<Void>() {
                @Override
                public void onSuccess(Void result) {
                    dismissNow();
                }

                @Override
                public void onFail(Exception e) {
                    Log.d("Error", e.getMessage());
                }
            });
        });


        return binding.getRoot();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        viewmodel.listMutableLiveData.setValue(adapter.getSelectedItems());
    }
}