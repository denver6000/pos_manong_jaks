package com.denproj.posmanongjaks.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.denproj.posmanongjaks.databinding.FragmentSelectProductBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SelectProductFragment extends DialogFragment {

//    SalesFragmentViewmodel viewmodel;
//    ProductSelectionRecyclerViewAdapter adapter;
    private String branchId;

    public SelectProductFragment(String branchId) {
        this.branchId = branchId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FragmentSelectProductBinding binding = FragmentSelectProductBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
//        this.viewmodel = new ViewModelProvider(requireActivity()).get(SalesFragmentViewmodel.class);
//        adapter = new ProductSelectionRecyclerViewAdapter();
//        binding.productSelectionRcv.setLayoutManager(new LinearLayoutManager(requireContext()));
//        binding.productSelectionRcv.setAdapter(adapter);

//        viewmodel.loadGlobalList(new OnUpdateUI<List<Product>>() {
//            @Override
//            public void onSuccess(List<Product> result) {
//                Log.d("Results", result.size() + "");
//                adapter.productsListRefreshed(result);
//            }
//
//            @Override
//            public void onFail(Exception e) {
//                Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

//        builder.setView(binding.getRoot());
//
//        builder.setPositiveButton("Save Selection", (dialogInterface, i) -> {
//            if (!adapter.getSelectedList().isEmpty()) {
//                dismissNow();
//                viewmodel.saveSelectedProductToBranch(adapter.getSelectedList());
//            } else {
//                Toast.makeText(requireContext(), "You have no selected products.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dismiss());

        return builder.show();
    }


}