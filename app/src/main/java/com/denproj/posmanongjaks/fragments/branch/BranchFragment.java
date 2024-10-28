package com.denproj.posmanongjaks.fragments.branch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.denproj.posmanongjaks.adapter.ItemsRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentBranchBinding;
import com.denproj.posmanongjaks.dialog.LoadingDialog;
import com.denproj.posmanongjaks.hilt.qualifier.OfflineImpl;
import com.denproj.posmanongjaks.hilt.qualifier.OnlineImpl;
import com.denproj.posmanongjaks.model.Branch;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.repository.base.ItemRepository;
import com.denproj.posmanongjaks.util.OnFetchFailed;
import com.denproj.posmanongjaks.util.OnUpdateUI;
import com.denproj.posmanongjaks.viewModel.BranchFragmentViewmodel;
import com.denproj.posmanongjaks.viewModel.HomeActivityViewmodel;
import com.denproj.posmanongjaks.viewModel.MainViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BranchFragment extends Fragment {

    HomeActivityViewmodel homeActivityViewmodel;
    BranchFragmentViewmodel branchFragmentViewmodel;
    MainViewModel mainViewModel;
    ItemsRecyclerViewAdapter adapter = new ItemsRecyclerViewAdapter();

    LoadingDialog loadingDialog;
    FragmentBranchBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBranchBinding.inflate(inflater);
        this.branchFragmentViewmodel = new ViewModelProvider(requireActivity()).get(BranchFragmentViewmodel.class);
        this.homeActivityViewmodel = new ViewModelProvider(requireActivity()).get(HomeActivityViewmodel.class);
        this.mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        homeActivityViewmodel.sessionMutableLiveData.observe(getViewLifecycleOwner(), session -> {
            Branch branch = session.getBranch();
            if (session.getRole() == null || session.getBranch() == null) {
                this.loadingDialog.dismiss();
                Toast.makeText(requireContext(), "Invalid branch.", Toast.LENGTH_SHORT).show();
                return;
            }

            setupViews();
            getRealtimeBranchStocks(branch.getBranch_id());
        });

        this.loadingDialog = new LoadingDialog();
        this.loadingDialog.show(getChildFragmentManager(), "");
        return binding.getRoot();
    }


    public void getRealtimeBranchStocks(String branchId) {
        this.branchFragmentViewmodel.observeRealtimeBranchStocks(branchId, e -> {
            Toast.makeText(requireContext(), "An error occurred " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }).observe(getViewLifecycleOwner(), result -> {
            adapter.onDataSetChanged(result);
            loadingDialog.dismiss();
            Toast.makeText(requireContext(), result.size() + " size", Toast.LENGTH_SHORT).show();
        });
    }

    public void setupViews() {
        binding.stocksRcv.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.stocksRcv.setAdapter(adapter);
    }



}