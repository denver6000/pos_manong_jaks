package com.denproj.posmanongjaks.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.widget.Toast;

import com.denproj.posmanongjaks.adapter.RecipeViewerRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentAddProductBinding;
import com.denproj.posmanongjaks.model.Recipe;
import com.denproj.posmanongjaks.util.OnDialogFinished;
import com.denproj.posmanongjaks.util.OnUpdateUI;
import com.denproj.posmanongjaks.viewModel.SalesFragmentViewmodel;

import java.util.HashMap;


public class AddProductFragment extends DialogFragment {


    private OnDialogFinished<Void> onDialogFinished;
    String branchId;
    private RecipeViewerRecyclerViewAdapter adapter = new RecipeViewerRecyclerViewAdapter();
    SalesFragmentViewmodel viewmodel;
    Uri selectedUri;

    public AddProductFragment(OnDialogFinished<Void> onDialogFinished, String branchId) {
        this.onDialogFinished = onDialogFinished;
        this.branchId = branchId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FragmentAddProductBinding binding = FragmentAddProductBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        viewmodel = new ViewModelProvider(requireActivity()).get(SalesFragmentViewmodel.class);
        binding.setViewModel(viewmodel);
        binding.chosenRecipesList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.chosenRecipesList.setAdapter(adapter);
        binding.chooseProductRecipe.setOnClickListener(view -> {
            new ChooseRecipeFragment(new OnDialogFinished<HashMap<String, Recipe>>() {
                @Override
                public void onDialogFinishedSuccessfully(HashMap<String, Recipe> result) {
                    adapter.setSelectedRecipes(result);
                }

                @Override
                public void onDialogFailed(@Nullable Exception e) {
                    Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

            }).show(getParentFragmentManager(), "");
        });

        builder.setPositiveButton("Insert Product", (dialogInterface, i) -> {
            if (adapter.getSelectedRecipes().isEmpty()) {
                Toast.makeText(requireContext(), "No Recipe was Selected", Toast.LENGTH_SHORT).show();
            } else {
                viewmodel.addProductToGlobalAndBranch(branchId, adapter.getSelectedRecipes(), new OnUpdateUI<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        dismissNow();
                    }

                    @Override
                    public void onFail(Exception e) {
                        Log.d("AddProductFragment", e.getMessage());
                        dismissNow();
                    }
                });
            }
        });

        ActivityResultLauncher<Intent> pickMedia = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), o -> {
            if (o.getResultCode() == Activity.RESULT_OK && o.getData() != null) {
                selectedUri = o.getData().getData();
                viewmodel.uriMutableLiveData.setValue(selectedUri);
                binding.productImagePreview.setImageURI(selectedUri);
            }
        });

        binding.productImagePreview.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*.jpg");
            pickMedia.launch(intent);
        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dismissNow();
        });





        builder.setView(binding.getRoot());
        return builder.show();
    }




}