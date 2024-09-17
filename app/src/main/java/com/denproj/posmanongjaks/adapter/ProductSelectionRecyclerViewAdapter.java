package com.denproj.posmanongjaks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.ProductSelectionCardBinding;
import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.util.ImagePathBinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductSelectionRecyclerViewAdapter extends RecyclerView.Adapter<ProductSelectionRecyclerViewAdapter.ViewHolder> {

    private List<Product> productsList = new ArrayList<>();
    private HashMap<String, Product> selectedList = new HashMap<>();


    public ProductSelectionRecyclerViewAdapter () {

    }

    public void productsListRefreshed(List<Product> products) {
        productsList = products;
        notifyDataSetChanged();
    }

    public HashMap<String, Product> getSelectedList() {
        return selectedList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_selection_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductSelectionCardBinding binding = holder.binding;
        Product product = productsList.get(position);
        binding.setProductName(product.getProductName());
        binding.productCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                selectedList.put(product.getProductId(), product);
            } else {
                selectedList.remove(product.getProductId());
            }
        });

        ImagePathBinder.bindImagePathToImageView(product.pathToImage, binding.imageView2, binding.getRoot().getContext(), new ImagePathBinder.OnImageBound() {
            @Override
            public void onBound() {

            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ProductSelectionCardBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.binding = ProductSelectionCardBinding.bind(itemView);
        }
    }

}
