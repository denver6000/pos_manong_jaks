package com.denproj.posmanongjaks.fragments.sales;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denproj.posmanongjaks.adapter.SalesRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentSalesHistoryBinding;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.session.Session;
import com.denproj.posmanongjaks.viewModel.HomeActivityViewmodel;
import com.denproj.posmanongjaks.viewModel.SaleHistoryViewModel;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class SalesHistoryFragment extends Fragment {

    SaleHistoryViewModel viewModel;
    SalesRecyclerViewAdapter adapter;
    HomeActivityViewmodel homeActivityViewmodel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSalesHistoryBinding binding = FragmentSalesHistoryBinding.inflate(getLayoutInflater());
        this.viewModel = new ViewModelProvider(requireActivity()).get(SaleHistoryViewModel.class);
        this.homeActivityViewmodel = new ViewModelProvider(requireActivity()).get(HomeActivityViewmodel.class);
        adapter = new SalesRecyclerViewAdapter(getChildFragmentManager());
        binding.salesFromDatabase.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.salesFromDatabase.setAdapter(adapter);

        homeActivityViewmodel.sessionMutableLiveData.observe(getViewLifecycleOwner(), session -> {
            if (session.isConnectionReachable()) {
                viewModel.getAllSalesRecord(session.getBranch().getBranch_id())
                        .thenAcceptAsync(sales -> adapter.setSalesWithProducts(sales))
                        .exceptionally(throwable -> {
                            Toast.makeText(requireContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            return null;
                        });
            } else {
                Toast.makeText(requireContext(), "To view all sales history, please enable internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
        return binding.getRoot();
    }
}