package com.denproj.posmanongjaks.fragments.sales;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.denproj.posmanongjaks.databinding.FragmentManageSalesBinding;

public class ManageSalesFragment extends Fragment {


    FragmentManageSalesBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManageSalesBinding.inflate(inflater);
        return binding.getRoot();
    }

}