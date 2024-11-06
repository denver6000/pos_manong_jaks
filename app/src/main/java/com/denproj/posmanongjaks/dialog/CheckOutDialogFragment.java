package com.denproj.posmanongjaks.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denproj.posmanongjaks.adapter.AddOnCheckOutRecyclerViewAdapter;
import com.denproj.posmanongjaks.adapter.ProductCheckOutRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentCheckOutDialogBinding;
import com.denproj.posmanongjaks.model.Branch;
import com.denproj.posmanongjaks.model.CompleteSaleInfo;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.ProductWrapper;
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseSaleRepository;
import com.denproj.posmanongjaks.viewModel.CheckOutDialogViewmodel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class CheckOutDialogFragment extends DialogFragment {

    private final HashMap<Long, ProductWrapper> selectedItems;
    private final HashMap<Item, Integer> selectedAddOns;
    private CheckOutDialogViewmodel viewmodel;
    private FragmentCheckOutDialogBinding binding;
    private ProductCheckOutRecyclerViewAdapter productsAdapter;
    private AddOnCheckOutRecyclerViewAdapter addOnsAdapter;
    private final OnSaleFinished onSaleFinished;

    private final Branch branch;

    public CheckOutDialogFragment(Branch branch, HashMap<Long, ProductWrapper> selectedItems, HashMap<Item, Integer> selectedAddOns, OnSaleFinished onSaleFinished) {
        this.selectedItems = selectedItems;
        this.selectedAddOns = selectedAddOns;
        this.onSaleFinished = onSaleFinished;

        this.branch = branch;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        viewmodel = new ViewModelProvider(requireActivity()).get(CheckOutDialogViewmodel.class);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());

        this.binding = FragmentCheckOutDialogBinding.inflate(getLayoutInflater());

        this.productsAdapter = new ProductCheckOutRecyclerViewAdapter(selectedItems, new ArrayList<>(selectedItems.keySet()), total -> {
            Double sumTotal = addOnsAdapter.getTotal() + productsAdapter.getTotalPrice();
            binding.setTotalAmount(sumTotal);
        });

        this.addOnsAdapter = new AddOnCheckOutRecyclerViewAdapter(selectedAddOns, total -> {
            Double sumTotal = addOnsAdapter.getTotal() + productsAdapter.getTotalPrice();
            binding.setTotalAmount(sumTotal);
        });

        binding.productsTobeSold.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.productsTobeSold.setAdapter(productsAdapter);

        binding.selectedAddOns.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.selectedAddOns.setAdapter(addOnsAdapter);

        dialogBuilder.setPositiveButton("Checkout", (dialogInterface, i) -> {
            
            if (addOnsAdapter.getSelectedAddOns().isEmpty() && productsAdapter.getSelectedProducts().isEmpty()) {
                Toast.makeText(requireContext(), "No Selected Items.", Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
                return;
            }

            Double total = binding.getTotalAmount();
            String paidAmountStr = binding.getPayedAmount();
            if (paidAmountStr == null || paidAmountStr.isEmpty() || Float.parseFloat(paidAmountStr) < total) {
                Toast.makeText(requireContext(), "Invalid Paid Amount.", Toast.LENGTH_SHORT).show();
                return;
            }

            Float paidAmount = Float.parseFloat(binding.getPayedAmount());
            ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Manila"));

            int month = zonedDateTime.getMonthValue();
            int year = zonedDateTime.getYear();
            int dayOfMonth = zonedDateTime.getDayOfMonth();

            viewmodel.sell(branch.getBranch_id(), productsAdapter.getSelectedProducts(), addOnsAdapter.getSelectedAddOns(), year, month, dayOfMonth, total, paidAmount.doubleValue(), new FirebaseSaleRepository.OnSaleStatus() {
                @Override
                public void success(CompleteSaleInfo completeSaleInfo) {
                    onSaleFinished.onSuccess(completeSaleInfo);
                }

                @Override
                public void failed(Exception fatalException, HashMap<String, String> itemNameAndErrorMessage) {
                    onSaleFinished.onFail(fatalException, itemNameAndErrorMessage);
                }
            });
        });

        dialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            this.dismiss();
        });

        binding.setTotalAmount(productsAdapter.getTotalPrice());
        dialogBuilder.setView(this.binding.getRoot());

        return dialogBuilder.create();
    }



    public interface OnTotalAmountChanged {
        void onChanged(Double total);
    }

    public interface OnSaleFinished {
        void onSuccess(CompleteSaleInfo completeSaleInfo);
        void onFail(Exception e, HashMap<String, String> itemNameAndErrors);
    }

    public interface ProceedToCheckOut {

    }
}