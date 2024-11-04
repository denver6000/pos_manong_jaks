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

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.adapter.RecipeRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentPopUpRecipeDialogBinding;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Recipe;
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseItemRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PopUpRecipeDialog extends DialogFragment {



    private HashMap<String, Recipe> productRecipe;
    private String branchid;
    private FragmentPopUpRecipeDialogBinding binding;



    public PopUpRecipeDialog(HashMap<String, Recipe> productRecipe, String branchid) {
        this.productRecipe = productRecipe;
        this.branchid = branchid;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        this.binding = FragmentPopUpRecipeDialogBinding.inflate(getLayoutInflater());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
        alertDialogBuilder.setView(binding.getRoot());
        binding.recipeAdapter.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadItemListFromRecipe(new OnDataReceived<List<Item>>() {
            @Override
            public void onSuccess(List<Item> result) {
                binding.recipeAdapter.setAdapter(new RecipeRecyclerViewAdapter(result));
            }

            @Override
            public void onFail(Exception e) {

            }
        });
        alertDialogBuilder.setTitle("Stocks");

        alertDialogBuilder.setPositiveButton("Okay", (dialogInterface, i) -> dialogInterface.dismiss());

        return alertDialogBuilder.create();
    }

    public void loadItemListFromRecipe(OnDataReceived<List<Item>> onDataReceived) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase
                .getReference(FirebaseItemRepository.PATH_TO_ITEMS_LIST + "/" + branchid)
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    List<Item> items = new ArrayList<>();
                    for (String itemKey : PopUpRecipeDialog.this.productRecipe.keySet()) {
                        Item item = dataSnapshot.child(itemKey).getValue(Item.class);
                        items.add(item);
                    }
                    onDataReceived.onSuccess(items);
                });
    }
}