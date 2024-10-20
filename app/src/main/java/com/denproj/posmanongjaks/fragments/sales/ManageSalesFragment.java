package com.denproj.posmanongjaks.fragments.sales;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denproj.posmanongjaks.adapter.SalesRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentManageSalesBinding;
import com.denproj.posmanongjaks.dialog.LoadingDialog;
import com.denproj.posmanongjaks.util.OnDataReceived;
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
        manageSalesViewmodel = new ViewModelProvider(requireActivity()).get(ManageSalesViewmodel.class);
        SalesRecyclerViewAdapter adapter = new SalesRecyclerViewAdapter(getChildFragmentManager());
        binding.salesToSync.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.salesToSync.setAdapter(adapter);

        LoadingDialog loadingDialog = new LoadingDialog();

        loadingDialog.show(getChildFragmentManager(), "");

        manageSalesViewmodel.getSalesLiveData().observe(getViewLifecycleOwner(), sales -> {
            adapter.setSalesWithProducts(sales);

            loadingDialog.dismiss();
        });


        binding.synchronizeSalesToDb.setOnClickListener(view -> {
            manageSalesViewmodel.synchronizeSales(new OnDataReceived<Void>() {
                @Override
                public void onSuccess(Void result) {
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFail(Exception e) {
                    Toast.makeText(requireContext(), "No Sales Record Present", Toast.LENGTH_SHORT).show();
                }
            });
        });


        manageSalesViewmodel.fetchSales().observe(getViewLifecycleOwner(), sales -> {
            manageSalesViewmodel.getSalesLiveData().setValue(sales);
        });


        return binding.getRoot();
    }
}