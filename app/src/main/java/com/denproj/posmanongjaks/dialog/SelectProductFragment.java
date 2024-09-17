package com.denproj.posmanongjaks.dialog;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.adapter.ProductSelectionRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentSelectProductBinding;
import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.viewModel.SalesFragmentViewmodel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SelectProductFragment extends DialogFragment {

    SalesFragmentViewmodel viewmodel;
    ProductSelectionRecyclerViewAdapter adapter;
    private String branchId;

    public SelectProductFragment(String branchId) {
        this.branchId = branchId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSelectProductBinding binding = FragmentSelectProductBinding.inflate(getLayoutInflater());

        this.viewmodel = new ViewModelProvider(requireParentFragment()).get(SalesFragmentViewmodel.class);
        adapter = new ProductSelectionRecyclerViewAdapter();
        binding.productSelectionRcv.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.productSelectionRcv.setAdapter(adapter);
        viewmodel.loadGlobalList(new OnDataReceived<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                Log.d("Results", result.size() + "");
                adapter.productsListRefreshed(result);
            }

            @Override
            public void onFail(Exception e) {
                Log.d("Error", e.getMessage());
            }
        });


        binding.saveProductSelection.setOnClickListener(view -> {
            if (!adapter.getSelectedList().isEmpty()) {
                dismissNow();
                viewmodel.saveSelectedProductToBranch(this.branchId, adapter.getSelectedList());
            } else {
                Toast.makeText(requireContext(), "You have no selected products.", Toast.LENGTH_SHORT).show();
            }
        });


        return binding.getRoot();
    }
}