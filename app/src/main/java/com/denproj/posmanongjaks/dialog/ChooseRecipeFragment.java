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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.adapter.ChooseRecipeRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentChooseRecipeBinding;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Recipe;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.util.OnDialogFinished;
import com.denproj.posmanongjaks.viewModel.SalesFragmentViewmodel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class ChooseRecipeFragment extends DialogFragment {

    OnDialogFinished<HashMap<Integer, Recipe>> onDialogFinished;
    ChooseRecipeRecyclerViewAdapter adapter;

    public ChooseRecipeFragment(OnDialogFinished<HashMap<Integer, Recipe>> onDialogFinished) {
        this.onDialogFinished = onDialogFinished;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FragmentChooseRecipeBinding binding = FragmentChooseRecipeBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        binding.recipeLists.setLayoutManager(new LinearLayoutManager(requireActivity()));
        loadGlobalList(new OnDataReceived<List<Item>>() {
            @Override
            public void onSuccess(List<Item> result) {
                adapter = new ChooseRecipeRecyclerViewAdapter(result);
                binding.recipeLists.setAdapter(adapter);
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(requireContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                Log.e("Error", e.getMessage());
            }
        });


        builder.setPositiveButton("Save Recipe", (dialogInterface, i) -> {
            this.dismissNow();
            onDialogFinished.onDialogFinishedSuccessfully(adapter.getSelectedRecipes());
        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            this.dismissNow();
            onDialogFinished.onDialogFailed(null);
        });

        builder.setView(binding.getRoot());
        return builder.show();
    }

    public void loadGlobalList(OnDataReceived<List<Item>> onDataReceived) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection(SalesFragmentViewmodel.PATH_TO_GLOBAL_ITEM_LIST)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Item> itemsList = new ArrayList<>();
                        task.getResult().forEach(queryDocumentSnapshot -> {
                            itemsList.add(queryDocumentSnapshot.toObject(Item.class));
                        });
                        onDataReceived.onSuccess(itemsList);
                    } else {
                        onDataReceived.onFail(task.getException());
                    }
                });

    }
}