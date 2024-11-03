package com.denproj.posmanongjaks.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.denproj.posmanongjaks.databinding.FragmentLoadingDialogBinding;


public class LoadingDialog extends DialogFragment {

    private String message = "";
    private String title = "";

    public LoadingDialog(String message, String title) {
        this.message = message;
        this.title = title;
    }

    public LoadingDialog() {
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder loadingDialogBuilder = new AlertDialog.Builder(requireContext());
        FragmentLoadingDialogBinding binding = FragmentLoadingDialogBinding.inflate(getLayoutInflater());
        loadingDialogBuilder.setCancelable(false);

        AlertDialog dialog = loadingDialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());

        if (!message.isEmpty() && !title.isEmpty()) {
            dialog.setMessage(this.message);
            dialog.setTitle(this.title);
        }

        return dialog;
    }


}