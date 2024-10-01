package com.denproj.posmanongjaks.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.ProductCardBinding;
import com.denproj.posmanongjaks.dialog.ChangePriceFragment;
import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.model.Recipe;
import com.denproj.posmanongjaks.repository.imp.ProductRepositoryImpl;
import com.denproj.posmanongjaks.util.ImagePathBinder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
        binding.setProductName(product.getProduct_name());
        binding.setProductPrice(product.getProduct_price() + "");
        FragmentActivity activity = (FragmentActivity) binding.getRoot().getContext();
        Log.d("ProductsRcv", product.getProduct_image_path());
        ImagePathBinder.bindImagePathToImageView(ProductRepositoryImpl.PRODUCT_IMAGE_PATH + "/" + product.getProduct_image_path(), binding.productImage, binding.getRoot().getContext(), new ImagePathBinder.OnImageBound() {
            @Override
            public void onBound() {

            }

            @Override
            public void onError(Exception e) {

            }
        });

        List<Recipe> recipeList = new ArrayList<>(product.getRecipes().values());

        ArrayAdapter<Recipe> adapter = new ArrayAdapter<>(activity , android.R.layout.simple_list_item_2, recipeList);
        binding.recipeListView.setAdapter(adapter);

        binding.changePriceRedirect.setOnClickListener(view -> {
            new ChangePriceFragment(this.branchId, String.valueOf(product.getProduct_id()), product.getProduct_name()).show(activity.getSupportFragmentManager(), "");
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
