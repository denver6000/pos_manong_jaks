package com.denproj.posmanongjaks.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.adapter.RecipeViewerRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentAddProductBinding;
import com.denproj.posmanongjaks.model.Recipe;
import com.denproj.posmanongjaks.util.OnDialogFinished;

import java.util.HashMap;


public class AddProductFragment extends DialogFragment {


    private OnDialogFinished<Void> onDialogFinished;
    private RecipeViewerRecyclerViewAdapter adapter;

    public AddProductFragment(OnDialogFinished<Void> onDialogFinished) {
        this.onDialogFinished = onDialogFinished;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FragmentAddProductBinding binding = FragmentAddProductBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        binding.chosenRecipesList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.chooseProductRecipe.setOnClickListener(view -> {
            new ChooseRecipeFragment(new OnDialogFinished<HashMap<Integer, Recipe>>() {
                @Override
                public void onDialogFinishedSuccessfully(HashMap<Integer, Recipe> result) {
                    adapter = new RecipeViewerRecyclerViewAdapter(result);
                    binding.chosenRecipesList.setAdapter(adapter);
                }

                @Override
                public void onDialogFailed(@Nullable Exception e) {
                    Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

            }).show(getParentFragmentManager(), "");
        });

        builder.setPositiveButton("Insert Product", (dialogInterface, i) -> {

        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dismissNow();
        });



        builder.setView(binding.getRoot());
        return builder.show();
    }
}