package com.denproj.posmanongjaks.fragments.sales;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denproj.posmanongjaks.adapter.AddOnRecyclerViewAdapter;
import com.denproj.posmanongjaks.adapter.ProductsRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentSalesViewBinding;
import com.denproj.posmanongjaks.dialog.CheckOutDialogFragment;
import com.denproj.posmanongjaks.dialog.LoadingDialog;
import com.denproj.posmanongjaks.hilt.qualifier.OfflineImpl;
import com.denproj.posmanongjaks.hilt.qualifier.OnlineImpl;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.repository.base.AddOnsRepository;
import com.denproj.posmanongjaks.repository.base.ProductRepository;
import com.denproj.posmanongjaks.session.Session;
import com.denproj.posmanongjaks.util.OnUpdateUI;
import com.denproj.posmanongjaks.viewModel.HomeActivityViewmodel;
import com.denproj.posmanongjaks.viewModel.MainViewModel;
import com.denproj.posmanongjaks.viewModel.SalesFragmentViewmodel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint

public class SalesViewFragment extends Fragment {

    ProductsRecyclerViewAdapter productsRecyclerViewAdapter;
    FragmentSalesViewBinding binding;
    AddOnRecyclerViewAdapter addOnRecyclerViewAdapter;
    HomeActivityViewmodel homeActivityViewmodel;
    SalesFragmentViewmodel viewmodel;

    MainViewModel mainViewModel;

    LoadingDialog loadingDialog = new LoadingDialog();


    @Inject
    @OfflineImpl
    ProductRepository productOfflineRepository;

    @Inject
    @OnlineImpl
    ProductRepository productOnlineRepository;

    @Inject
    @OfflineImpl
    AddOnsRepository addOnOfflineRepository;

    @Inject
    @OnlineImpl
    AddOnsRepository addOnlineRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSalesViewBinding.inflate(inflater);

        this.viewmodel = new ViewModelProvider(requireActivity()).get(SalesFragmentViewmodel.class);
        this.homeActivityViewmodel = new ViewModelProvider(requireActivity()).get(HomeActivityViewmodel.class);
        this.mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        setupAdapters();
        showDialog();

        this.homeActivityViewmodel.sessionMutableLiveData.observe(getViewLifecycleOwner(), session -> {
            if (session.isConnectionReachable()) {
                this.viewmodel.setProductRepository(productOnlineRepository);
                this.viewmodel.setAddOnsRepository(addOnlineRepository);
            } else {
                this.viewmodel.setProductRepository(productOfflineRepository);
                this.viewmodel.setAddOnsRepository(addOnOfflineRepository);
            }
            String branchId = session.getBranch().getBranch_id();
            if (branchId.isEmpty()) {
                Toast.makeText(requireContext(), "No Branch Is Associated With this account.", Toast.LENGTH_LONG).show();
                return;
            }

            viewmodel.loadAddOns(branchId, new OnUpdateUI<List<Item>>() {
                @Override
                public void onSuccess(List<Item> result) {
                    setupProductRcv(branchId, result);
                    loadProductsOnBranch(branchId);

                    addOnRecyclerViewAdapter = new AddOnRecyclerViewAdapter(result);
                    binding.addOnList.setAdapter(addOnRecyclerViewAdapter);
                    Toast.makeText(requireContext(), result + "", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFail(Exception e) {
                    Log.e("SalesViewFragment", e.getMessage());
                }
            });

            setupViews(session);
        });


        return binding.getRoot();
    }

    public void getBranchId() {

    }

    public void setupViews(Session session) {
        binding.checkOutItems.setOnClickListener(view -> {
            new CheckOutDialogFragment(session, productsRecyclerViewAdapter.getSelectedProducts(), addOnRecyclerViewAdapter.getItemsMap(), new CheckOutDialogFragment.OnSaleFinished() {
                @Override
                public void onSuccess(Double change) {
                    Toast.makeText(requireContext(), "Sale Complete", Toast.LENGTH_SHORT).show();
                    clearOrders();
                    //showSaleCompleteDialog(change);
                }

                @Override
                public void onFail() {
                    Toast.makeText(requireContext(), "Sale Failed", Toast.LENGTH_SHORT).show();
                }
            }).show(getChildFragmentManager(), "");
        });
    }

    public void loadProductsOnBranch (String branchId) {
        viewmodel.loadProductsOfBranch(branchId, new OnUpdateUI<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                productsRecyclerViewAdapter.refreshAdapter(result);
                hideDialog();
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setupProductRcv(String branchId, List<Item> addOnsList) {
        this.productsRecyclerViewAdapter = new ProductsRecyclerViewAdapter(addOnsList, branchId);
        binding.menuRcv.setAdapter(productsRecyclerViewAdapter);
    }



    public void setupAdapters() {
        binding.menuRcv.setLayoutManager(new LinearLayoutManager(requireContext()));
        LinearLayoutManager manager = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
        binding.addOnList.setLayoutManager(manager);
    }

    public void showDialog() {
        loadingDialog.show(getChildFragmentManager(), "");
    }

    public void hideDialog() {
        loadingDialog.dismiss();
    }

    public void clearOrders () {
        productsRecyclerViewAdapter.clearSelectedProducts();
        addOnRecyclerViewAdapter.clearSelectedItems();
    }

    public void showSaleCompleteDialog(Double change) {
        AlertDialog.Builder saleCompleteDialogBuilder = new AlertDialog.Builder(requireContext());
        saleCompleteDialogBuilder.setTitle("Change.");
        saleCompleteDialogBuilder.setMessage(change.toString());

        saleCompleteDialogBuilder.setPositiveButton("Ok", (dialogInterface1, i1) -> {
            dialogInterface1.dismiss();
        });

        AlertDialog saleCompleteDialog = saleCompleteDialogBuilder.create();
        saleCompleteDialog.show();
    }


}