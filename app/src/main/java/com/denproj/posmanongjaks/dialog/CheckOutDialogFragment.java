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
import com.denproj.posmanongjaks.model.ProductWrapper;
import com.denproj.posmanongjaks.model.CompleteSaleInfo;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Role;
import com.denproj.posmanongjaks.model.User;
import com.denproj.posmanongjaks.session.Session;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.viewModel.CheckOutDialogViewmodel;

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

    private Branch branch;
    private Session session;

    public CheckOutDialogFragment(Session session, HashMap<Long, ProductWrapper> selectedItems, HashMap<Item, Integer> selectedAddOns, OnSaleFinished onSaleFinished) {
        this.selectedItems = selectedItems;
        this.selectedAddOns = selectedAddOns;
        this.onSaleFinished = onSaleFinished;


        this.branch = session.getBranch();
        this.session = session;
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

            Calendar calendar = GregorianCalendar.getInstance();
            int month = calendar.get(GregorianCalendar.MONTH);
            int year = calendar.get(GregorianCalendar.YEAR);
            int dayOfMonth = calendar.get(GregorianCalendar.DAY_OF_MONTH);

            viewmodel.sell(branch.getBranch_id(), productsAdapter.getSelectedProducts(), addOnsAdapter.getSelectedAddOns(), year, month, dayOfMonth, total, paidAmount.doubleValue(), new OnDataReceived<CompleteSaleInfo>() {
                @Override
                public void onSuccess(CompleteSaleInfo result) {
                    onSaleFinished.onSuccess(paidAmount.doubleValue() - total);
                    new ShowReceiptDialogFragment(result).show(getParentFragmentManager(), "");
                }

                @Override
                public void onFail(Exception e) {
                    onSaleFinished.onFail();
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
        void onSuccess(Double change);
        void onFail();
    }

    public interface OnPrintReceipt {
        void onPrintReceipt(String strToPrint);
    }


}