package com.denproj.posmanongjaks.viewModel;

import static com.denproj.posmanongjaks.repository.imp.ProductRepositoryImpl.PATH_TO_BRANCH_ITEMS;
import static com.denproj.posmanongjaks.repository.imp.ProductRepositoryImpl.PATH_TO_GLOBAL_ITEM_LIST;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.model.Recipe;
import com.denproj.posmanongjaks.repository.base.ProductRepository;
import com.denproj.posmanongjaks.repository.base.RecipeRepository;
import com.denproj.posmanongjaks.repository.base.SaleRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.util.OnUpdateUI;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SalesFragmentViewmodel extends ViewModel {


    ProductRepository productRepository;
    RecipeRepository recipeRepository;

    SaleRepository saleRepository;

    public ObservableField<String> productName = new ObservableField<>("");
    public ObservableField<String> productPrice = new ObservableField<>("");
    public MutableLiveData<Uri> uriMutableLiveData = new MutableLiveData<>();

    @Inject
    public SalesFragmentViewmodel(ProductRepository productRepository, RecipeRepository recipeRepository, SaleRepository saleRepository) {
        this.productRepository = productRepository;
        this.recipeRepository = recipeRepository;
        this.saleRepository = saleRepository;
    }

    public void loadGlobalList(OnUpdateUI<List<Product>> onUpdateUI) {
        productRepository.fetchProductsFromGlobal(new OnDataReceived<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                onUpdateUI.onSuccess(result);
            }

            @Override
            public void onFail(Exception e) {
                onUpdateUI.onFail(e);
            }
        });
    }

    public void loadProductsOfBranch(String branchId, OnUpdateUI<List<Product>> listOnUpdateUI) {
        productRepository.fetchProductsFromBranch(branchId, new OnDataReceived<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                listOnUpdateUI.onSuccess(result);
            }

            @Override
            public void onFail(Exception e) {
                listOnUpdateUI.onFail(e);
            }
        });
    }

    public void saveSelectedProductToBranch(String branchId, HashMap<String, Product> selectedItems) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(PATH_TO_BRANCH_ITEMS+"/"+branchId);
        selectedItems.values().forEach(s -> s.getRecipes().forEach((s1, recipe) -> {
            ref.child(s1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        // TODO Insert Items To Branch Stocks
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }));
    }

    public void changeProductPrice (String itemId, String branchId, String newPrice, OnDataReceived<Void> onPriceChanged) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database
                .getReference("products_list/" +branchId+"/"+itemId)
                .child("price")
                .setValue(Double.valueOf(newPrice))
                .addOnSuccessListener(onPriceChanged::onSuccess)
                .addOnFailureListener(onPriceChanged::onFail);
    }

    public void addProductToGlobalAndBranch(String branchId, HashMap<String, Recipe> recipes, OnUpdateUI<Void> onUpdateUI) {
        Product product = new Product();
        product.setProduct_id(generateRandomSixDigitId());
        product.setProduct_name(productName.get());
        product.setProduct_price(Float.parseFloat(productPrice.get()));
        product.setRecipes(recipes);
        productRepository.insertImage(uriMutableLiveData.getValue(), generateImageName(), new OnDataReceived<String>() {
            @Override
            public void onSuccess(String result) {
                product.setProduct_image_path(result);
                productRepository.insertProductToBranch(branchId, product, new OnDataReceived<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        productRepository.loadRecipeIntoBranchStocks(branchId, product.getRecipes(), null);
                        onUpdateUI.onSuccess(result);
                        productRepository.insertProductToGlobalList(product, new OnDataReceived<Void>() {
                            @Override
                            public void onSuccess(Void result) {
                                onUpdateUI.onSuccess(result);
                            }

                            @Override
                            public void onFail(Exception e) {
                                onUpdateUI.onFail(e);
                            }
                        });
                    }

                    @Override
                    public void onFail(Exception e) {
                        onUpdateUI.onFail(e);
                    }
                });
            }
            @Override
            public void onFail(Exception e) {
                onUpdateUI.onFail(e);
            }
        });

    }

    public void sell(HashMap<Product, Integer> selectedProductToSell) {
        Calendar calendar = GregorianCalendar.getInstance();
        selectedProductToSell.forEach((product, amount) -> {

            int year = calendar.get(GregorianCalendar.YEAR);
            int month = calendar.get(GregorianCalendar.MONTH);
            int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
            saleRepository.insertSaleRecord(amount, product, year, month, day, null);
        });
    }

    private String generateImageName() {
        return System.currentTimeMillis() + "-" + productName.get() + ".jpg";
    }

    public int generateRandomSixDigitId() {
        return new Random().nextInt(999999);
    }
}
