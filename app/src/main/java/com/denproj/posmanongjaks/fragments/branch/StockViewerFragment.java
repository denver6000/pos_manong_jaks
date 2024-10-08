package com.denproj.posmanongjaks.fragments.branch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.FragmentStockViewerBinding;

public class StockViewerFragment extends Fragment {

    private FragmentStockViewerBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentStockViewerBinding.inflate(inflater);

        this.binding.addStockBatch.setOnClickListener(view -> {

        });

        return binding.getRoot();
    }
}