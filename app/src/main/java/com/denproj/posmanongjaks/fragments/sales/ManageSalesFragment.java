package com.denproj.posmanongjaks.fragments.sales;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denproj.posmanongjaks.adapter.SalesRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentManageSalesBinding;
import com.denproj.posmanongjaks.dialog.LoadingDialog;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.viewModel.HomeActivityViewmodel;
import com.denproj.posmanongjaks.viewModel.ManageSalesViewmodel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;

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
        HomeActivityViewmodel homeActivityViewmodel = new ViewModelProvider(requireActivity()).get(HomeActivityViewmodel.class);

        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.show(getChildFragmentManager(), "");

        homeActivityViewmodel.sessionMutableLiveData.observe(getViewLifecycleOwner(), session -> {
            manageSalesViewmodel.fetchSales(session.getBranch().getBranch_id()).thenAcceptAsync(sales -> {
                manageSalesViewmodel.getSalesLiveData().setValue(sales);
            }, ContextCompat.getMainExecutor(requireContext())).exceptionally(throwable -> {
                Toast.makeText(requireContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                return null;
            });
            manageSalesViewmodel.getSalesLiveData().observe(getViewLifecycleOwner(), sales -> {
                adapter.setSalesWithProducts(sales);

                loadingDialog.dismiss();
            });
            if (session.isConnectionReachable()) {
                binding.synchronizeSalesToDb.setOnClickListener(view -> {
                    manageSalesViewmodel.synchronizeSales(session.getBranch().getBranch_id(), new OnDataReceived<Void>() {
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
            } else {
                Toast.makeText(requireContext(), "Please connect to an internet connection.", Toast.LENGTH_SHORT).show();
            }
        });


        return binding.getRoot();
    }
}