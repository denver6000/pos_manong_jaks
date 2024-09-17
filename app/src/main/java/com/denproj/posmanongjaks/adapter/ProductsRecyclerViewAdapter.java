package com.denproj.posmanongjaks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.ProductCardBinding;
import com.denproj.posmanongjaks.dialog.ChangePriceFragment;
import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.util.ImagePathBinder;

import java.util.ArrayList;
import java.util.List;

public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder> {

    List<Product> productList = new ArrayList<>();
    String branchId;

    public ProductsRecyclerViewAdapter(String branchId) {
        this.branchId = branchId;
    }

    public void refreshAdapter(List<Product> newProductList) {
        productList = newProductList;
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
        binding.setProductName(product.getProductName());
        binding.setProductPrice(product.getPrice() + "");
        FragmentActivity activity = (FragmentActivity) binding.getRoot().getContext();

        ImagePathBinder.bindImagePathToImageView(product.pathToImage, binding.productImage, binding.getRoot().getContext(), new ImagePathBinder.OnImageBound() {
            @Override
            public void onBound() {

            }

            @Override
            public void onError(Exception e) {

            }
        });


        binding.changePriceRedirect.setOnClickListener(view -> {
            new ChangePriceFragment(this.branchId, product.getProductId(), product.getProductName()).show(activity.getSupportFragmentManager(), "");
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ProductCardBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.binding = ProductCardBinding.bind(itemView);
        }


    }

}
