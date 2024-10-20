package com.denproj.posmanongjaks.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.denproj.posmanongjaks.databinding.FragmentChangePriceBinding;

public class ChangePriceFragment extends DialogFragment {

//    SalesFragmentViewmodel viewmodel;
//
//    String branchId;
//    String itemId;
//
//    String productName;

    public ChangePriceFragment(String branchId, String itemId, String productName) {
//        this.branchId = branchId;
//        this.itemId = itemId;
//        this.productName = productName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentChangePriceBinding binding = FragmentChangePriceBinding.inflate(inflater);
//        viewmodel = new ViewModelProvider(requireActivity()).get(SalesFragmentViewmodel.class);
//
//        binding.setProductNameAndOldPrice(productName);

//        binding.saveChanges.setOnClickListener(view -> {
//            String newPrice = binding.newPrice.getText().toString();
//            viewmodel.changeProductPrice(this.itemId, this.branchId, newPrice, new OnDataReceived<Void>() {
//                @Override
//                public void onSuccess(Void result) {
//                    Toast.makeText(requireContext(), "Saved Price", Toast.LENGTH_SHORT).show();
//                    dismissNow();
//                }
//
//                @Override
//                public void onFail(Exception e) {
//                    Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
//                    dismissNow();
//                }
//            });
//        });

        return binding.getRoot();
    }
}