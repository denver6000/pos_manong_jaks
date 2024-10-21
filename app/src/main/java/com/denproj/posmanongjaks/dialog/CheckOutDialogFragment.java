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
import com.denproj.posmanongjaks.adapter.CheckOutRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentCheckOutDialogBinding;
import com.denproj.posmanongjaks.model.AddOn;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.session.SessionManager;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.viewModel.CheckOutDialogViewmodel;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class CheckOutDialogFragment extends DialogFragment {

    private final HashMap<String, AddOn> selectedItems;
    private final HashMap<Item, Integer> selectedAddOns;
    private CheckOutDialogViewmodel viewmodel;
    private FragmentCheckOutDialogBinding binding;
    private CheckOutRecyclerViewAdapter productsAdapter;
    private AddOnCheckOutRecyclerViewAdapter addOnsAdapter;
    private OnSaleFinished onSaleFinished;

    public CheckOutDialogFragment(HashMap<String, AddOn> selectedItems, HashMap<Item, Integer> selectedAddOns, OnSaleFinished onSaleFinished) {
        this.selectedItems = selectedItems;
        this.selectedAddOns = selectedAddOns;
        this.onSaleFinished = onSaleFinished;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        viewmodel = new ViewModelProvider(requireActivity()).get(CheckOutDialogViewmodel.class);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());

        this.binding = FragmentCheckOutDialogBinding.inflate(getLayoutInflater());
        this.productsAdapter = new CheckOutRecyclerViewAdapter(selectedItems, total -> {
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
            Double total = binding.getTotalAmount();
            Float payedAmount = Float.parseFloat(binding.getPayedAmount());

            if (selectedItems.isEmpty() && selectedAddOns.isEmpty()) {
                Toast.makeText(requireContext(), "No Items in Checkout, Please Select Some", Toast.LENGTH_SHORT).show();
                return;
            }

            if (payedAmount.isNaN() || payedAmount.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Please enter payed amount.", Toast.LENGTH_SHORT).show();
                return;
            }

            Double change = payedAmount - total;
            String branchId = SessionManager.getInstance().getBranchId();


            Calendar calendar = GregorianCalendar.getInstance();
            int month = calendar.get(GregorianCalendar.MONTH);
            int year = calendar.get(GregorianCalendar.YEAR);
            int dayOfMonth = calendar.get(GregorianCalendar.DAY_OF_MONTH);

            viewmodel.sell(branchId, productsAdapter.getSelectedItems(), addOnsAdapter.getSelectedAddOns(), year, month, dayOfMonth, total, change, new OnDataReceived<Void>() {
                @Override
                public void onSuccess(Void result) {
                    onSaleFinished.onSuccess(change);
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


}