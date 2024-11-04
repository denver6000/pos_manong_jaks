package com.denproj.posmanongjaks.fragments.sales;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.adapter.AddOnRecyclerViewAdapter;
import com.denproj.posmanongjaks.adapter.ProductsRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentSalesViewBinding;
import com.denproj.posmanongjaks.dialog.CheckOutDialogFragment;
import com.denproj.posmanongjaks.dialog.LoadingDialog;
import com.denproj.posmanongjaks.model.Branch;
import com.denproj.posmanongjaks.model.CompleteSaleInfo;
import com.denproj.posmanongjaks.session.Session;
import com.denproj.posmanongjaks.viewModel.HomeActivityViewmodel;
import com.denproj.posmanongjaks.viewModel.MainViewModel;
import com.denproj.posmanongjaks.viewModel.SalesFragmentViewmodel;

import java.util.HashMap;

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
            String branchId = session.getBranch().getBranch_id();
            if (branchId.isEmpty()) {
                Toast.makeText(requireContext(), "No Branch Is Associated With this account.", Toast.LENGTH_LONG).show();
                return;
            }

            setupAddOnRcv();
            observeAddons(branchId);

            setupProductRcv(branchId);
            observeProducts(branchId);

            setupViews(session);
        });


        return binding.getRoot();
    }

    public void setupViews(Session session) {
        binding.checkOutItems.setOnClickListener(view -> {
            new CheckOutDialogFragment(session.getBranch(), productsRecyclerViewAdapter.getSelectedProducts(), addOnRecyclerViewAdapter.getItemsMap(), new CheckOutDialogFragment.OnSaleFinished() {
                @Override
                public void onSuccess(CompleteSaleInfo completeSaleInfo) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Print Receipt?").setMessage("Print a receipt");

                    builder.setPositiveButton("Print", (dialogInterface, i) -> {
                        NavController navController = Navigation.findNavController(requireActivity(), R.id.homeFragmentContainerView);
                        navController.navigate(SalesViewFragmentDirections.actionSalesViewToPrintActivity(formatReceipt(completeSaleInfo, session.getBranch(), session.getUser().getUser_id())));
                    });

                    builder.setNegativeButton("Close", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    });

                    builder.create().show();
                    clearOrders();
                }

                @Override
                public void onFail(Exception e, HashMap<String, String> itemNameAndErrors) {
                    if (e != null) {
                        showErrorDialog("A Fatal Error Occurred", e.getMessage());
                    } else if (!itemNameAndErrors.isEmpty()) {
                        itemNameAndErrors.forEach((itemName, errorMsg) -> {
                            showErrorDialog(itemName, errorMsg);
                        });
                    }
                }
            }).show(getChildFragmentManager(), "");
        });
    }

    public void observeProducts (String branchId) {
        this.viewmodel.observeProductListOfBranch(branchId, e -> {
            Toast.makeText(requireContext(), "Something went wrong " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }).observe(getViewLifecycleOwner(), products -> {
            productsRecyclerViewAdapter.refreshAdapter(products);
            hideDialog();
        });
    }

    public void observeAddons(String branchId) {
        this.viewmodel.observeAddOnsListOfBranch(branchId, e -> {
            Toast.makeText(requireContext(), "Something went wrong " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }).observe(getViewLifecycleOwner(), items -> {
            addOnRecyclerViewAdapter.refreshAdapter(items);
        });
    }

    public void setupProductRcv(String branchId) {
        this.productsRecyclerViewAdapter = new ProductsRecyclerViewAdapter(branchId, getChildFragmentManager());
        binding.menuRcv.setAdapter(productsRecyclerViewAdapter);
    }

    public void setupAddOnRcv() {
        this.addOnRecyclerViewAdapter = new AddOnRecyclerViewAdapter();
        this.binding.addOnList.setAdapter(addOnRecyclerViewAdapter);
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

    public String formatReceipt(CompleteSaleInfo completeSaleInfo, Branch branch, String uid) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[C]<font size='big'>Manong Jak's</font>\n");
        stringBuilder.append("[C]<font size='big'>Burger</font>");
        stringBuilder.append("\n[C]").append(branch.getBranch_name()).append("\n").append("[L]<b>Products</b>\n");

        completeSaleInfo.getSaleProducts().forEach(saleProduct -> {
            stringBuilder.append("[L]").append(saleProduct.getName()).append("[R]x").append(saleProduct.getAmount()).append("\n");
        });

        stringBuilder.append("[L]<b>Add Ons</b>\n");
        completeSaleInfo.getSaleItems().forEach(saleItem -> {
            stringBuilder.append("[L]").append(saleItem.getItemName()).append("[R]x").append(saleItem.getAmount()).append("\n");
        });

        stringBuilder.append("[L]\n");
        stringBuilder.append("[L]Total:").append("[R]").append(completeSaleInfo.getSale().getTotal()).append(" Pesos\n");
        stringBuilder.append("[L]Change:").append("[R]").append(completeSaleInfo.getSale().getChange()).append(" Pesos\n");
        stringBuilder.append("[L]Sale Id:").append("[R]").append(completeSaleInfo.getSale().getSaleId()).append("\n");
        stringBuilder.append("[L]Sold By:").append("[R]UID: ").append(uid).append("\n");
        stringBuilder.append("[C]App Generated Receipt\n");
        return stringBuilder.toString();
    }

    public void showErrorDialog(String title, String message) {
        AlertDialog.Builder errorDialog = new AlertDialog.Builder(requireContext());
        errorDialog
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Okay", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });
        errorDialog.create().show();
    }
}