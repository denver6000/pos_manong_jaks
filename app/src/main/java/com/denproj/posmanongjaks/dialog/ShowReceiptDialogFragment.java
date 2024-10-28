package com.denproj.posmanongjaks.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.FragmentShowReceiptDialogBinding;
import com.denproj.posmanongjaks.model.CompleteSaleInfo;


public class ShowReceiptDialogFragment extends DialogFragment {

    CompleteSaleInfo completeSaleInfo;

    public ShowReceiptDialogFragment(CompleteSaleInfo completeSaleInfo) {
        this.completeSaleInfo = completeSaleInfo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FragmentShowReceiptDialogBinding binding = FragmentShowReceiptDialogBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        NavController navController = Navigation.findNavController(requireActivity(), R.id.homeFragmentContainerView);




        builder.setView(binding.getRoot());
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }
}