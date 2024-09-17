package com.denproj.posmanongjaks.fragments.sales;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.adapter.ProductsRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentSalesViewBinding;
import com.denproj.posmanongjaks.dialog.SelectProductFragment;
import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.viewModel.BranchFragmentViewmodel;
import com.denproj.posmanongjaks.viewModel.SalesFragmentViewmodel;

import java.util.List;


public class SalesViewFragment extends Fragment {
    SalesFragmentViewmodel viewmodel;
    ProductsRecyclerViewAdapter adapter;
    FragmentSalesViewBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSalesViewBinding.inflate(inflater);
        this.viewmodel = new ViewModelProvider(requireActivity()).get(SalesFragmentViewmodel.class);

        binding.menuRcv.setLayoutManager(new LinearLayoutManager(requireContext()));


        BranchFragmentViewmodel branchFragmentViewmodel = new ViewModelProvider(requireActivity()).get(BranchFragmentViewmodel.class);

        branchFragmentViewmodel.branchIdLiveData.observe(getViewLifecycleOwner(), branchId -> {
            if (branchId.isEmpty()) {
                Toast.makeText(requireContext(), "No Branch Is Associated With this account.", Toast.LENGTH_LONG).show();
                return;
            }

            this.setUpPriceChangeListener(branchId);

            this.loadProductsOnBranch(branchId);

            binding.addItemsToBranch.setOnClickListener(view -> {
                SelectProductFragment dialog = new SelectProductFragment(branchId);
                dialog.show(getChildFragmentManager(), "");
            });

        });

        return binding.getRoot();
    }

    public void loadProductsOnBranch (String branchId) {
        viewmodel.loadProductsOfBranch(branchId, new OnDataReceived<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                Toast.makeText(requireContext(), "Result length " + result.size(), Toast.LENGTH_SHORT).show();
                adapter.refreshAdapter(result);
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

    public void setUpPriceChangeListener(String branchId) {
        adapter = new ProductsRecyclerViewAdapter(branchId);
        binding.menuRcv.setAdapter(adapter);
    }
}