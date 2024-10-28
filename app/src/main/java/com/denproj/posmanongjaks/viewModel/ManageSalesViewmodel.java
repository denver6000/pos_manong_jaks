package com.denproj.posmanongjaks.viewModel;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.hilt.qualifier.OfflineImpl;
import com.denproj.posmanongjaks.model.CompleteSaleInfo;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.model.SaleItem;
import com.denproj.posmanongjaks.model.SaleProduct;
import com.denproj.posmanongjaks.repository.base.SaleRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManageSalesViewmodel extends ViewModel {

    public static final String PATH_TO_ITEM_OF_BRANCHES = "items_on_branches";
    public static final String PATH_TO_SALES_RECORD = "sales_record";
    SaleRepository saleRepository;
    MutableLiveData<List<Sale>> salesLiveData = new MutableLiveData<>(new ArrayList<>());
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    // private MutableLiveData<Item>
    @Inject
    public ManageSalesViewmodel(@OfflineImpl SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public CompletableFuture<List<Sale>> fetchSales(String branchId) {
        return saleRepository.getAllRecordedSalesOnBranch(branchId);
    }

    public CompletableFuture<Void> clearSales() {
        return CompletableFuture.supplyAsync(() -> {
            saleRepository.clearSale();
            return null;
        });
    }

    public CompletableFuture<Void> syncAllSale(String branchId, OnItemNotFoundInBranch onItemNotFoundInBranch) {
        return CompletableFuture.supplyAsync(() -> {
            List<Sale> sales = null;

            try {
                sales = saleRepository.getAllRecordedSalesOnBranch(branchId).get();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (sales.isEmpty()) {
                throw new RuntimeException(new Exception("No Sales Record Found"));
            }

            for (Sale sale : sales) {
                List<SaleProduct> saleProducts = saleRepository.getAllSaleProductBySaleIdSync(sale.getSaleId());
                List<SaleItem> saleItems = saleRepository.getAllSaleItemBySaleIdSync(sale.getSaleId());
                CompleteSaleInfo completeSaleInfo = new CompleteSaleInfo(sale, saleItems, saleProducts);
                checkIfItemsToReduceExists(completeSaleInfo, branchId, onItemNotFoundInBranch);
            }
            return null;
        });
    }

    public CompletableFuture<Void> removeSaleById(Integer saleId) {
        return CompletableFuture.supplyAsync(() -> {
            saleRepository.removeLocalSaleById(saleId);
            return null;
        });
    }

    public CompletableFuture<Void> insertSale(Sale sale, List<SaleItem> saleItems, List<SaleProduct> saleProducts) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        sale.setSoldItems(saleItems);
        sale.setSoldProducts(saleProducts);
        firebaseDatabase.getReference("sales_record").child(sale.getSaleId() + "").setValue(sale).addOnSuccessListener(unused -> {
            completableFuture.complete(null);
        }).addOnFailureListener(completableFuture::completeExceptionally);
        return completableFuture;
    }

    public void checkIfItemsToReduceExists(CompleteSaleInfo completeSaleInfo, String branchId, OnItemNotFoundInBranch onItemNotFoundInBranch) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference itemsOnBranchList = firebaseDatabase.getReference("items_on_branches/" + branchId);
        if ((completeSaleInfo.getSaleItems() == null && completeSaleInfo.getSaleProducts() == null) || (completeSaleInfo.getSaleProducts().isEmpty() && completeSaleInfo.getSaleItems().isEmpty())) {
            onItemNotFoundInBranch.allItemsFound(completeSaleInfo, new HashMap<>());
            return;
        }
        itemsOnBranchList.get().addOnSuccessListener(branchItemList -> {
            List<Integer> missingItemsInBranch = new ArrayList<>();
            HashMap<Integer, Integer> itemIdAndAmountToRemove = new HashMap<>();
            for (SaleItem saleItem : completeSaleInfo.getSaleItems()) {
                if (branchItemList.hasChild(saleItem.getItem_id() + "")) {
                    itemIdAndAmountToRemove.put(saleItem.getItem_id(), saleItem.getAmount());
                } else {
                    missingItemsInBranch.add(saleItem.getItem_id());
                }
            }

            completeSaleInfo.getSaleProducts().forEach(saleProduct -> {
                DatabaseReference recipeOfProductList = firebaseDatabase.getReference(
                        "products_on_branches/"
                                + branchId + "/"
                                + saleProduct.getProduct_id()
                                + "/recipes");
                recipeOfProductList.get().addOnSuccessListener(recipes -> {
                    for (DataSnapshot recipeSnapshot : recipes.getChildren()) {
                        String itemKey = recipeSnapshot.getKey();
                        if (branchItemList.hasChild(itemKey)) {
                            Integer amount = recipeSnapshot.child("amount").getValue(Integer.class);
                            itemIdAndAmountToRemove.put(Integer.valueOf(itemKey), saleProduct.getAmount() * amount);
                        } else {
                            missingItemsInBranch.add(Integer.parseInt(itemKey));
                        }
                    }
                }).addOnSuccessListener(dataSnapshot -> {
                    if (!missingItemsInBranch.isEmpty()) {
                        onItemNotFoundInBranch.itemNotFound(completeSaleInfo.getSale(), missingItemsInBranch);
                    } else {
                        onItemNotFoundInBranch.allItemsFound(completeSaleInfo, itemIdAndAmountToRemove);
                    }
                });
            });
        }).addOnFailureListener(onItemNotFoundInBranch::onError);
    }

    public CompletableFuture<Void> reduceStock(int itemId, String branchId, int amountToReduce) {
        CompletableFuture<Void> reduceStockFuture = new CompletableFuture<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference("items_on_branches/" + branchId + "/" + itemId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot quantityRef = snapshot.child("item_quantity");
                if (quantityRef.exists()) {
                    Integer quantity = quantityRef.getValue(Integer.class);
                    if (quantity != null) {
                        quantityRef.getRef().setValue(quantity - amountToReduce);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Test", "Test");
            }
        });
        return reduceStockFuture;
    }

    public CompletableFuture<Void> insertToDbAndRemoveLocally(CompleteSaleInfo completeSaleInfo) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference salesNodeRef = firebaseDatabase.getReference(PATH_TO_SALES_RECORD + "/");
        Sale sale = completeSaleInfo.getSale();
        sale.setSoldItems(completeSaleInfo.getSaleItems());
        sale.setSoldProducts(completeSaleInfo.getSaleProducts());
        salesNodeRef
                .child(sale.getSaleId() + "")
                .setValue(sale)
                .addOnSuccessListener(unused -> {
                    removeSaleById(sale.getSaleId())
                            .thenAccept(completableFuture::complete)
                            .exceptionally(throwable -> {
                                completableFuture.completeExceptionally(throwable);
                                return null;
                            });
                }).addOnFailureListener(e -> {
                    completableFuture.completeExceptionally(new Exception(e));
                });
        return completableFuture;
    }


    public CompletableFuture<Void> processStockReduction(HashMap<Integer, Integer> itemsToRemoveAndAmount, String branchId, OnStockReductionError onStockReductionError) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference(PATH_TO_ITEM_OF_BRANCHES + "/" + branchId);
        ref.get().addOnSuccessListener(dataSnapshot -> {
            itemsToRemoveAndAmount.forEach((itemId, amountToReduce) -> {
                DataSnapshot item = dataSnapshot.child(itemId.toString());
                DataSnapshot itemQuantitySnapshot = item.child("item_quantity");
                Integer amount = itemQuantitySnapshot.getValue(Integer.class);
                if (amount != null) {
                    itemQuantitySnapshot.getRef().setValue(amount - amountToReduce);
                } else {
                    onStockReductionError.errorOnItem(item.child("item_name").getValue(String.class));
                }
            });
            completableFuture.complete(null);
        }).addOnFailureListener(completableFuture::completeExceptionally);
        return completableFuture;
    }

    public LiveData<List<SaleProduct>> getSoldProductsAsync(Integer saleId) {
        return saleRepository.getAllProductsWithSaleId(saleId);
    }

    public LiveData<List<SaleItem>> getSoldAddOnsAsync(Integer saleId) {
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

    public interface OnItemNotFoundInBranch {
        void itemNotFound(Sale sale, List<Integer> itemIds);

        void allItemsFound(CompleteSaleInfo completeSaleInfo, HashMap<Integer, Integer> itemsAndAmountToReduce);

        void onError(Exception e);
    }

    public interface OnStockReductionError {
        void errorOnItem(String itemName);
    }

}
