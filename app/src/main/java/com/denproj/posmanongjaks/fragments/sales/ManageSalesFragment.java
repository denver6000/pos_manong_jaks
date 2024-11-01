package com.denproj.posmanongjaks.fragments.sales;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.denproj.posmanongjaks.databinding.FragmentManageSalesBinding;
import com.denproj.posmanongjaks.viewModel.ManageSalesViewmodel;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ManageSalesFragment extends Fragment {


    FragmentManageSalesBinding binding;
    ManageSalesViewmodel manageSalesViewmodel;
    private SimpleDateFormat inputFormat = new SimpleDateFormat("MMMMM d, yyyy", Locale.ENGLISH);

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