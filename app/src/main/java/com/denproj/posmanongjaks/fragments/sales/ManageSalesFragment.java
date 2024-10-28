package com.denproj.posmanongjaks.fragments.sales;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denproj.posmanongjaks.adapter.SalesRecyclerViewAdapter;
import com.denproj.posmanongjaks.databinding.FragmentManageSalesBinding;
import com.denproj.posmanongjaks.dialog.LoadingDialog;
import com.denproj.posmanongjaks.model.CompleteSaleInfo;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.viewModel.HomeActivityViewmodel;
import com.denproj.posmanongjaks.viewModel.ManageSalesViewmodel;

import java.text.SimpleDateFormat;
import java.util.HashMap;
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
            fetchSales(session.getBranch().getBranch_id());
            manageSalesViewmodel.getSalesLiveData().observe(getViewLifecycleOwner(), sales -> {
                adapter.setSalesWithProducts(sales);
                loadingDialog.dismiss();
            });
            if (session.isConnectionReachable()) {
                binding.synchronizeSalesToDb.setOnClickListener(view -> {
                    manageSalesViewmodel.syncAllSale(session.getBranch().getBranch_id(), new ManageSalesViewmodel.OnItemNotFoundInBranch() {
                        @Override
                        public void itemNotFound(Sale sale, List<Integer> itemIds) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
                            alertDialogBuilder.setTitle("Items Are Missing In Your Sale #" + sale.getSaleId() + ", sales not synced");
                            StringBuilder stringBuilder = new StringBuilder();

                            itemIds.forEach(integer -> {
                                stringBuilder.append("[").append(integer).append("]").append(", ");
                            });

                            stringBuilder.append(" are not found in your branch. ScreenShot this image and send it to your branch manager");
                            alertDialogBuilder.setMessage(stringBuilder.toString());
                            alertDialogBuilder.setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss());
                            alertDialogBuilder.create().show();
                        }

                        @Override
                        public void allItemsFound(CompleteSaleInfo completeSaleInfo, HashMap<Integer, Integer> itemsAndAmountToReduce) {
                            manageSalesViewmodel.processStockReduction(itemsAndAmountToReduce, session.getBranch().getBranch_id(), itemName -> {
                                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                                builder.setTitle("Error Occurred on Item " + itemName);
                                builder.setMessage("Contact your branch admin and check if item is present. Stock was not reduced for this item.");
                                builder.setPositiveButton("Okay", (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                });
                            }).thenAccept(unused -> {
                                manageSalesViewmodel.insertToDbAndRemoveLocally(completeSaleInfo);
                            }).thenAccept(unused -> {
                                Toast.makeText(requireContext(), "Sync Success", Toast.LENGTH_SHORT).show();
                            }).exceptionally(throwable -> {
                                Toast.makeText(requireContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                return null;
                            });
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).thenAcceptAsync(unused -> {
                    }).exceptionally(new Function<Throwable, Void>() {
                        @Override
                        public Void apply(Throwable throwable) {
                            Toast.makeText(requireContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            return null;
                        }
                    });
                });
            } else {
                Toast.makeText(requireContext(), "Please connect to an internet connection.", Toast.LENGTH_SHORT).show();
            }
        });


        return binding.getRoot();
    }
    
    public void fetchSales(String branchId) {
        manageSalesViewmodel.fetchSales(branchId).thenAcceptAsync(sales -> {
            manageSalesViewmodel.getSalesLiveData().setValue(sales);
        }, ContextCompat.getMainExecutor(requireContext())).exceptionally(throwable -> {
            Toast.makeText(requireContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            return null;
        });
    }
}