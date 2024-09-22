package com.denproj.posmanongjaks.fragments.branch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.adapter.ItemsRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentBranchBinding;
import com.denproj.posmanongjaks.dialog.AddNewItemFragment;
import com.denproj.posmanongjaks.dialog.SelectItemFragment;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.viewModel.BranchFragmentViewmodel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class BranchFragment extends Fragment {

    BranchFragmentViewmodel viewmodel;
    ItemsRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentBranchBinding binding = FragmentBranchBinding.inflate(inflater);
        this.viewmodel = new ViewModelProvider(requireActivity()).get(BranchFragmentViewmodel.class);

        viewmodel.branchIdLiveData.observe(getViewLifecycleOwner(), branchId -> {
            if (branchId.isEmpty()) {
                return;
            }

            binding.stocksRcv.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.stocksRcv.setAdapter(adapter);
            binding.selectItemsBtn.setOnClickListener(view -> {
                SelectItemFragment itemFragment = new SelectItemFragment(branchId);
                itemFragment.show(getChildFragmentManager(), "");
            });
            binding.addNewItem.setOnClickListener(view -> {
                AddNewItemFragment addNewItemFragment = new AddNewItemFragment();
                addNewItemFragment.show(getChildFragmentManager(), "");
            });

            adapter = new ItemsRecyclerViewAdapter(itemId -> {

            });

            viewmodel.getRealtimeBranchStocks(branchId, new OnDataReceived<List<Item>>() {
                @Override
                public void onSuccess(List<Item> result) { adapter.onDataSetChanged(result);}

                @Override
                public void onFail(Exception e) {
                    Log.e("Error", e.getMessage());
                }
            });
        });

        return binding.getRoot();
    }


}