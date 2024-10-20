package com.denproj.posmanongjaks.fragments.sales;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denproj.posmanongjaks.adapter.SalesRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentSalesHistoryBinding;
import com.denproj.posmanongjaks.viewModel.SaleHistoryViewModel;

public class SalesHistoryFragment extends Fragment {

    SaleHistoryViewModel viewModel;
    SalesRecyclerViewAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSalesHistoryBinding binding = FragmentSalesHistoryBinding.inflate(getLayoutInflater());
        this.viewModel = new ViewModelProvider(requireActivity()).get(SaleHistoryViewModel.class);
        adapter = new SalesRecyclerViewAdapter(getChildFragmentManager());
        binding.salesFromDatabase.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.salesFromDatabase.setAdapter(adapter);
        viewModel.getAllSalesRecord().observe(getViewLifecycleOwner(), sales -> {
            adapter.setSalesWithProducts(sales);
        });


        return binding.getRoot();
    }
}