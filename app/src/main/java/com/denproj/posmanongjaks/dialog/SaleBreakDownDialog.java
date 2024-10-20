package com.denproj.posmanongjaks.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.denproj.posmanongjaks.databinding.FragmentSalesbreakdownDialogBinding;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.model.SaleItem;
import com.denproj.posmanongjaks.model.SaleProduct;
import com.denproj.posmanongjaks.viewModel.ManageSalesViewmodel;

public class SaleBreakDownDialog extends DialogFragment {

    Sale sale;
    ManageSalesViewmodel manageSalesViewmodel;

    public SaleBreakDownDialog(Sale sale) {
        this.sale = sale;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        manageSalesViewmodel = new ViewModelProvider(requireActivity()).get(ManageSalesViewmodel.class);
        FragmentSalesbreakdownDialogBinding binding = FragmentSalesbreakdownDialogBinding.inflate(getLayoutInflater());
        builder.setView(binding.getRoot());

        manageSalesViewmodel.getSoldAddOnsAsync(sale.getSaleId()).observe(this, saleItems -> {
            ArrayAdapter<SaleItem> saleItemArrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, saleItems);
            binding.soldAddons.setAdapter(saleItemArrayAdapter);
        });

        manageSalesViewmodel.getSoldProductsAsync(sale.getSaleId()).observe(this, saleProducts -> {
            ArrayAdapter<SaleProduct> saleItemArrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, saleProducts);
            binding.soldProducts.setAdapter(saleItemArrayAdapter);
        });

        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }
}
