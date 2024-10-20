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
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.repository.base.ItemRepository;
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
    ItemsRecyclerViewAdapter adapter = new ItemsRecyclerViewAdapter(itemId -> {

    });

    @OfflineImpl
    @Inject
    ItemRepository offlineItemRepository;

    @OnlineImpl
    @Inject
    ItemRepository onlineItemRepository;

    LoadingDialog loadingDialog;
    FragmentBranchBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBranchBinding.inflate(inflater);
        this.branchFragmentViewmodel = new ViewModelProvider(requireActivity()).get(BranchFragmentViewmodel.class);
        this.homeActivityViewmodel = new ViewModelProvider(requireActivity()).get(HomeActivityViewmodel.class);
        this.mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        homeActivityViewmodel.getIsConnectionReachableLiveData().observeForever(isConnectionAvailable -> {
            if (isConnectionAvailable) {
                branchFragmentViewmodel.setItemRepository(onlineItemRepository);
            } else {
                branchFragmentViewmodel.setItemRepository(offlineItemRepository);
            }
            getBranchIdFromMainViewModel();
        });

        this.loadingDialog = new LoadingDialog();
        this.loadingDialog.show(getChildFragmentManager(), "");
        return binding.getRoot();
    }

    public void getBranchIdFromMainViewModel() {
        this.homeActivityViewmodel.branchIdLiveData.observe(getViewLifecycleOwner(), branchId -> {
            if (branchId.isEmpty()) {
                return;
            }
            getRealtimeBranchStocks();
            setupViews(branchId);
        });
    }

    public void getRealtimeBranchStocks() {
        this.branchFragmentViewmodel.getRealtimeBranchStocks(new OnUpdateUI<List<Item>>() {
            @Override
            public void onSuccess(List<Item> result) {
                adapter.onDataSetChanged(result);
                loadingDialog.dismiss();
                Toast.makeText(requireContext(), result.size() + " size", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setupViews(String branchId) {
        binding.stocksRcv.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.stocksRcv.setAdapter(adapter);

//        binding.selectItemsBtn.setOnClickListener(view -> {
//            SelectItemFragment itemFragment = new SelectItemFragment(branchId);
//            itemFragment.show(getChildFragmentManager(), "");
//        });

//        binding.addNewItem.setOnClickListener(view -> {
//            AddNewItemFragment addNewItemFragment = new AddNewItemFragment();
//            addNewItemFragment.show(getChildFragmentManager(), "");
//        });
    }



}