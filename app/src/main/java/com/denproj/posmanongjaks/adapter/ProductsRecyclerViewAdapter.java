package com.denproj.posmanongjaks.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.recyclerview.widget.RecyclerView;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.ProductCardBinding;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.model.ProductWrapper;
import com.denproj.posmanongjaks.util.ImagePathBinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder> {

    List<Product> productList = new ArrayList<>();
    private HashMap<Long, ProductWrapper> selectedProducts = new HashMap<>();


    public void refreshAdapter(List<Product> newProductList) {
        this.productList.clear();
        productList.addAll(newProductList);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        ProductCardBinding binding = holder.binding;
        binding.setSaleAmount(String.valueOf(1));

        binding.incrementProductAmount.setOnClickListener(view -> {
            selectedProducts.putIfAbsent(product.getProduct_id(), new ProductWrapper(1, product));
        });

        binding.setProductName(product.getProduct_name());
        binding.setProductPrice(product.getProduct_price() + "");
        ImagePathBinder.bindImagePathToImageView(product.getProduct_image_path(), binding.productImage, binding.getRoot().getContext(), new ImagePathBinder.OnImageBound() {
            @Override
            public void onBound() {

            }

            @Override
            public void onError(Exception e) {
                Log.e("ProductsRCV", e.getMessage());
            }
        });
    }

    public HashMap<Long, ProductWrapper> getSelectedProducts() {
        return selectedProducts;
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public ListPopupWindow showListPopUpWindow(Context context, ArrayAdapter<Item> arrayAdapter) {
        ListPopupWindow popupWindow = new ListPopupWindow(context);
        popupWindow.setAdapter(arrayAdapter);
        return popupWindow;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ProductCardBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.binding = ProductCardBinding.bind(itemView);
        }
    }

    public void clearSelectedProducts() {
        this.selectedProducts.clear();
    }

}
