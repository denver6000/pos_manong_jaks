package com.denproj.posmanongjaks.repository.firebaseImpl;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.denproj.posmanongjaks.model.CompleteSaleInfo;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.ProductWrapper;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.model.SaleItem;
import com.denproj.posmanongjaks.model.SaleProduct;
import com.denproj.posmanongjaks.repository.base.SaleRepository;
import com.denproj.posmanongjaks.util.OnFetchFailed;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class FirebaseSaleRepository implements SaleRepository {
    public static final String PATH_TO_SALE_RECORD = "sales_record";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    @Override
    public void processSale(String branchId, HashMap<Long, ProductWrapper> selectedProducts, HashMap<Item, Integer> selectedAddOns, int year, int month, int day, Double total, Double amountToBePaid, OnSaleStatus saleStatus) {
        validateSale(branchId, selectedProducts, selectedAddOns, new OnSaleValidation() {
            @Override
            public void canProceed(Map<String, Object> itemsToReduceStock) {
                reduceSaleStock(branchId, itemsToReduceStock, new StockReductionStatus() {
                    @Override
                    public void onSuccess() {
                        Sale saleRecord = new Sale();
                        saleRecord.setSaleId(Integer.valueOf(generateId()));
                        Double change = amountToBePaid - total;
                        saleRecord.setChange(change);
                        saleRecord.setTotal(total);
                        saleRecord.setMonth(month);
                        saleRecord.setDay(day);
                        saleRecord.setYear(year);
                        saleRecord.setPaidAmount(amountToBePaid);
                        saleRecord.setBranchId(branchId);


                        insertSale(saleRecord, selectedProducts, selectedAddOns, saleStatus);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        saleStatus.failed(e, new HashMap<>());
                    }
                });
            }

            @Override
            public void cannotProceed(HashMap<String, String> itemsWithErrors) {
                saleStatus.failed(null, itemsWithErrors);
            }
        });
    }

    public void validateSale(String branchId, HashMap<Long, ProductWrapper> selectedProducts, HashMap<Item, Integer> selectedAddOns, OnSaleValidation saleValidation) {
        HashMap<Integer, Integer> idAndAmountToReduce = new HashMap<>();
        HashMap<String, String> itemsFailed = new HashMap<>();
        Map<String, Object> batchRequest = new HashMap<>();

        selectedProducts.forEach((aLong, productWrapper) -> {
            productWrapper.getProduct().getRecipes().forEach((s, recipe) -> {
                int recipeItemKey = Integer.parseInt(s);
                idAndAmountToReduce.compute(recipeItemKey, (k, v) -> (v == null) ? (recipe.getAmount() * productWrapper.getAddOnAmount()) : v + recipe.getAmount());
            });
        });

        selectedAddOns.forEach((item, integer) -> {
            idAndAmountToReduce.compute(item.getItem_id(), (k, v) -> (v == null) ? integer : v + integer);
        });

        firebaseDatabase
                .getReference(FirebaseItemRepository.PATH_TO_ITEMS_LIST + "/" + branchId)
                .get()
                .addOnSuccessListener(items -> {
                    for (Map.Entry<Integer, Integer> idAndAmount : idAndAmountToReduce.entrySet()) {
                        Integer key = idAndAmount.getKey();
                        Integer amountToReduce = idAndAmount.getValue();

                        if (!items.hasChild(key.toString())) {
                            itemsFailed.put(key + "", " does not exist in your branch. Contact Branch Manager to configure.");
                            continue;
                        }
                        String itemName = items.child(key + "/item_name").getValue(String.class);

                        DataSnapshot itemQuantitySnapshot = items.child(key + "/item_quantity");
                        Integer itemQuantityVal = itemQuantitySnapshot.getValue(Integer.class);

                        if (!itemQuantitySnapshot.exists() || itemQuantityVal == null) {
                            itemsFailed.put(itemName, " [Item Quantity] is not configured correctly.");
                            continue;
                        }

                        if (itemQuantityVal < amountToReduce) {
                            int discrepancy = amountToReduce - itemQuantityVal;
                             itemsFailed.put(itemName, " Item Quantity (Stock) is not enough to fulfil order. " + discrepancy + " is needed.");
                            continue;
                        }

                        batchRequest.put(key + "/" + "item_quantity", itemQuantityVal - amountToReduce);
                    }

                    if (itemsFailed.isEmpty()) {
                        saleValidation.canProceed(batchRequest);
                    } else {
                        saleValidation.cannotProceed(itemsFailed);
                    }
                }).addOnFailureListener(e -> saleValidation.cannotProceed(new HashMap<>()));
    }

    public void reduceSaleStock(String branchId, Map<String, Object> pathAndAmountToReduce, StockReductionStatus stockReductionStatus) {
        DatabaseReference itemsRef = firebaseDatabase.getReference(FirebaseItemRepository.PATH_TO_ITEMS_LIST + "/" + branchId);
        itemsRef.updateChildren(pathAndAmountToReduce)
                .addOnFailureListener(stockReductionStatus::onFailed);
        stockReductionStatus.onSuccess();
    }

    public void insertSale(Sale sale, HashMap<Long, ProductWrapper> selectedProducts, HashMap<Item, Integer> selectedAddOns, OnSaleStatus onSaleStatus) {
        List<SaleItem> saleItems = new ArrayList<>();
        List<SaleProduct> saleProducts = new ArrayList<>();
        selectedProducts.forEach((aLong, productWrapper) -> {
            saleProducts.add(new SaleProduct(productWrapper.getProduct().getProduct_id(), productWrapper.getAddOnAmount().intValue(), sale.getSaleId(), productWrapper.getProduct().getProduct_name()));
        });
        selectedAddOns.forEach((item, integer) -> {
            saleItems.add(new SaleItem(item.getItem_id(), sale.getSaleId(), integer, item.getItem_name()));
        });
        sale.setSoldProducts(saleProducts);
        sale.setSoldItems(saleItems);
        onSaleStatus.success(new CompleteSaleInfo(sale, saleItems, saleProducts));
        firebaseDatabase.getReference(PATH_TO_SALE_RECORD + "/" + sale.getBranchId() + "/" + sale.getSaleId()).setValue(sale);
    }

    @Override
    public LiveData<List<SaleItem>> getAllAddOnsWithSaleId(Integer saleId) {
        return null;
    }

    @Override
    public LiveData<List<SaleProduct>> getAllProductsWithSaleId(Integer saleId) {
        return null;
    }
    @Override

    public LiveData<List<Sale>> getAllRecordedSalesOnBranch(String branchId, OnFetchFailed onFetchFailed) {
        MutableLiveData<List<Sale>> branchSalesRecords = new MutableLiveData<>();
        List<Sale> recordedSales = new ArrayList<>();
        firebaseDatabase.getReference(PATH_TO_SALE_RECORD + "/" + branchId).get().addOnSuccessListener(dataSnapshot -> {
            dataSnapshot.getChildren().forEach(saleSnapshot -> {
                Sale sale = saleSnapshot.getValue(Sale.class);
                recordedSales.add(sale);
            });
            branchSalesRecords.setValue(recordedSales);
        }).addOnFailureListener(onFetchFailed::onFetchFailed);
        return branchSalesRecords;
    }

    public String generateId() {
        Random random = new Random();
        int randomSixDigit = 100000 + random.nextInt(900000);
        return "10" + randomSixDigit;
    }

    public interface OnSaleValidation {
        void canProceed(Map<String, Object> itemsToReduceStock);
        void cannotProceed(HashMap<String, String> itemsWithErrors);
    }

    public interface OnSaleStatus {
        void success(CompleteSaleInfo completeSaleInfo);
        void failed(Exception fatalExceptions, HashMap<String, String> itemNameAndError);
    }

    public interface StockReductionStatus {
        void onSuccess();
        void onFailed(Exception e);
    }

}
