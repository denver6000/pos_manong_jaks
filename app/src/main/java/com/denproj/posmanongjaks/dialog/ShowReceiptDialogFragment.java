package com.denproj.posmanongjaks.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.denproj.posmanongjaks.databinding.FragmentShowReceiptDialogBinding;


public class ShowReceiptDialogFragment extends DialogFragment {

    Bitmap receipt;

    public ShowReceiptDialogFragment(Bitmap receipt) {
        this.receipt = receipt;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FragmentShowReceiptDialogBinding binding = FragmentShowReceiptDialogBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        binding.receiptImage.setImageBitmap(receipt);
        builder.setView(binding.getRoot());
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }
}