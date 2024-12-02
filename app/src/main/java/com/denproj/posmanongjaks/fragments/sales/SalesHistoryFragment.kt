package com.denproj.posmanongjaks.fragments.sales;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denproj.posmanongjaks.adapter.SalesRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentSalesHistoryBinding;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.util.OnFetchFailed;
import com.denproj.posmanongjaks.viewModel.HomeActivityViewmodel;
import com.denproj.posmanongjaks.viewModel.SaleHistoryViewModel;

import java.util.List;

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
                Toast.makeText(requireContext(), "These are live sale data.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "These are only cached sale data.", Toast.LENGTH_SHORT).show();
            }
            viewModel.getAllSalesRecord(session.getBranch().getBranch_id(), new OnFetchFailed() {
                @Override
                public void onFetchFailed(Exception e) {
                    Log.e("SalesHistoryFragment", e.getMessage());
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }).observe(getViewLifecycleOwner(), sales -> adapter.setSalesWithProducts(sales));
        });
        return binding.getRoot();
    }
}