package com.denproj.posmanongjaks.fragments.branch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.FragmentManageStocksBinding;

public class ManageStocksFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentManageStocksBinding binding = FragmentManageStocksBinding.inflate(inflater);




        return binding.getRoot();
    }
}