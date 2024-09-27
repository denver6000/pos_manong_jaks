package com.denproj.posmanongjaks.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.adapter.ProductSelectionRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentSelectProductBinding;
import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.util.OnUpdateUI;
import com.denproj.posmanongjaks.viewModel.SalesFragmentViewmodel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SelectProductFragment extends DialogFragment {

    SalesFragmentViewmodel viewmodel;
    ProductSelectionRecyclerViewAdapter adapter;
    private String branchId;

    public SelectProductFragment(String branchId) {
        this.branchId = branchId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FragmentSelectProductBinding binding = FragmentSelectProductBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        this.viewmodel = new ViewModelProvider(requireActivity()).get(SalesFragmentViewmodel.class);
        adapter = new ProductSelectionRecyclerViewAdapter();
        binding.productSelectionRcv.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.productSelectionRcv.setAdapter(adapter);

        viewmodel.loadGlobalList(new OnUpdateUI<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                Log.d("Results", result.size() + "");
                adapter.productsListRefreshed(result);
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(binding.getRoot());

        builder.setPositiveButton("Save Selection", (dialogInterface, i) -> {
            if (!adapter.getSelectedList().isEmpty()) {
                dismissNow();
                viewmodel.saveSelectedProductToBranch(this.branchId, adapter.getSelectedList());
            } else {
                Toast.makeText(requireContext(), "You have no selected products.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dismiss());

        return builder.show();
    }


}