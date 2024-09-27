package com.denproj.posmanongjaks.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denproj.posmanongjaks.adapter.ItemSelectionRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentSelectItemBinding;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.util.OnUpdateUI;
import com.denproj.posmanongjaks.viewModel.BranchFragmentViewmodel;

import java.util.List;


public class SelectItemFragment extends DialogFragment {
    BranchFragmentViewmodel viewmodel;
    ItemSelectionRecyclerViewAdapter adapter;
    String branchId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public SelectItemFragment(String branchId) {
        this.branchId = branchId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FragmentSelectItemBinding binding = FragmentSelectItemBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        viewmodel = new ViewModelProvider(requireParentFragment()).get(BranchFragmentViewmodel.class);
        viewmodel.loadGlobalItemList(new OnUpdateUI<List<Item>>() {
            @Override
            public void onSuccess(List<Item> result) {
                binding.globalItemList.setLayoutManager(new LinearLayoutManager(requireContext()));
                adapter = new ItemSelectionRecyclerViewAdapter(result);
                binding.globalItemList.setAdapter(adapter);
                Log.d("List", result.get(0).getItem_name() + "");
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setPositiveButton("Save Selection", (dialogInterface, i) -> {
            viewmodel.saveSelectionToBranchList(branchId, adapter.getSelectedItems(), new OnUpdateUI<Void>() {
                @Override
                public void onSuccess(Void result) {
                    dismissNow();
                }

                @Override
                public void onFail(Exception e) {
                    Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }});
        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dismiss();
        });

        builder.setView(binding.getRoot());
        return builder.show();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        viewmodel.listMutableLiveData.setValue(adapter.getSelectedItems());
    }
}