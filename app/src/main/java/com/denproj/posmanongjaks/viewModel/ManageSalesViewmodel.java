package com.denproj.posmanongjaks.viewModel;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.hilt.qualifier.OfflineImpl;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.model.SaleItem;
import com.denproj.posmanongjaks.model.SaleProduct;
import com.denproj.posmanongjaks.repository.base.SaleRepository;
import com.denproj.posmanongjaks.session.SessionManager;
import com.denproj.posmanongjaks.util.AsyncRunner;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManageSalesViewmodel extends ViewModel {
    public static final String PATH_TO_SALES_RECORD = "sales_record_t_2";
    SaleRepository saleRepository;
    MutableLiveData<List<Sale>> salesLiveData = new MutableLiveData<>(new ArrayList<>());
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    @Inject
    public ManageSalesViewmodel(@OfflineImpl SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public LiveData<List<Sale>> fetchSales() {
        return saleRepository.getAllRecordedSales();
    }

    public void synchronizeSales(OnDataReceived<Void> onDataReceived) {
        String branchId = SessionManager.getInstance().getBranchId();
        List<Sale> sales = salesLiveData.getValue();
        if (sales == null || sales.isEmpty()) {
            onDataReceived.onFail(new Exception("No Sales Record"));
            return;
        }


        AsyncRunner.runAsync(new AsyncRunner.Runner<Void>() {
            @Override
            public Void onBackground() throws Exception {
                List<Sale> sales = salesLiveData.getValue();
                if (sales == null || sales.isEmpty()) {
                    throw new Exception("Sale is empty");
                }

                sales.forEach(sale -> {
                    List<SaleProduct> saleProducts = saleRepository.getAllSaleProductBySaleIdSync(sale.getSaleId());
                    List<SaleItem> saleItems = saleRepository.getAllSaleItemBySaleIdSync(sale.getSaleId());

                    if (saleProducts != null && !saleProducts.isEmpty()) {
                        saleProducts.forEach(saleProduct -> {
                            getRecipe(branchId, saleProduct.getProduct_id());
                        });
                    }

                    if (saleItems != null && !saleItems.isEmpty()) {
                        saleItems.forEach(saleItem -> {
                            reduceStock(saleItem.getItem_id(), branchId, saleItem.getAmount());
                        });
                    }

                    insertSaleToDatabase(sale, saleProducts, saleItems);
                });



                return null;
            }

            @Override
            public void onFinished(Void result) {

            }

            @Override
            public void onUI(Void result) {
                onDataReceived.onSuccess(result);
            }

            @Override
            public void onError(Exception e) {

                onDataReceived.onFail(e);
            }
        });
    }

    public void insertSaleToDatabase(Sale sale, List<SaleProduct> saleProducts, List<SaleItem> saleItems) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(PATH_TO_SALES_RECORD);
        DatabaseReference ref = databaseReference.child(sale.getSaleId());
        ref.setValue(sale);
        ref.child("date").setValue(formatDateString(sale.getYear(), sale.getMonth(), sale.getDay()));
        ref.child("sold_products").setValue(saleProducts);
        ref.child("sold_items").setValue(saleItems);
    }

    private void getRecipe(String branchId, Integer productId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("products_on_branches/"+branchId+"/"+productId+"/recipes").get().addOnSuccessListener(dataSnapshot -> {
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                String itemKey = child.getKey();
                Integer amount = child.child("amount").getValue(Integer.class);
                reduceStock(Integer.valueOf(itemKey), branchId, amount);
            }
        });
    }

    private void reduceStock(int itemId, String branchId, int amountToReduce) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference("items_on_branches/"+branchId+"/"+itemId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot quantityRef = snapshot.child("item_quantity");
                Integer quantity = quantityRef.getValue(Integer.class);
                quantityRef.getRef().setValue(quantity - amountToReduce);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public LiveData<List<SaleProduct>> getSoldProductsAsync(String saleId) {
        return saleRepository.getAllProductsWithSaleId(saleId);
    }

    public LiveData<List<SaleItem>> getSoldAddOnsAsync(String saleId) {
        return saleRepository.getAllAddOnsWithSaleId(saleId);
    }

    public MutableLiveData<List<Sale>> getSalesLiveData() {
        return salesLiveData;
    }
    public void setSalesLiveData(MutableLiveData<List<Sale>> salesLiveData) {
        this.salesLiveData = salesLiveData;
    }

    private String formatDateString(int year, int month, int day) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(year, month, day);
        String formatted = simpleDateFormat.format(calendar.getTime());
        calendar.clear();
        return formatted;
    }
}
