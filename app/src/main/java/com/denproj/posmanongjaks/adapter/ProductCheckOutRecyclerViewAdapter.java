package com.denproj.posmanongjaks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.ItemCheckoutCardLayoutBinding;
import com.denproj.posmanongjaks.dialog.CheckOutDialogFragment;
import com.denproj.posmanongjaks.model.ProductWrapper;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductCheckOutRecyclerViewAdapter extends RecyclerView.Adapter<ProductCheckOutRecyclerViewAdapter.ViewHolder> {
    HashMap<Long, ProductWrapper> selectedProducts;
    ArrayList<Long> productArrayList;
    CheckOutDialogFragment.OnTotalAmountChanged onTotalAmountChanged;


    public ProductCheckOutRecyclerViewAdapter(HashMap<Long, ProductWrapper> selectedItems, ArrayList<Long> productArrayList, CheckOutDialogFragment.OnTotalAmountChanged totalAmountChanged) {
        this.selectedProducts = selectedItems;
        this.onTotalAmountChanged = totalAmountChanged;
        this.productArrayList = productArrayList;
    }

    public void removeItem(int position) {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkout_card_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Long key = productArrayList.get(position);
        ProductWrapper productWrapper = selectedProducts.get(key);
        ItemCheckoutCardLayoutBinding binding = holder.binding;
        binding.setProductName(productWrapper.getProduct().getProduct_name());

        if (productWrapper != null) {

            binding.incrementAmount.setOnClickListener(view -> {

                incrementAmountAndUpdateTotalPrice(productWrapper, binding);
            });

            binding.decrementAmount.setOnClickListener(view -> {

                decrementAmountAndUpdateTotalPrice(holder.getAdapterPosition(), productWrapper, binding);
            });

            incrementAmountAndUpdateTotalPrice(productWrapper, binding);
        }
    }

    public double getTotalPrice () {

        double total = 0f;
        for (int i = 0; i < selectedProducts.size(); i++) {
            Long key = productArrayList.get(i);
            ProductWrapper productWrapper = selectedProducts.get(key);
            Integer amount = productWrapper.getAddOnAmount();
            total += productWrapper.getProduct().getProduct_price() * amount;
        }
        return total;
    }

    public HashMap<Long, ProductWrapper> getSelectedProducts() {
        return selectedProducts;
    }

    public void setSelectedProducts(HashMap<Long, ProductWrapper> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    public ArrayList<Long> getProductArrayList() {
        return productArrayList;
    }

    public void setProductArrayList(ArrayList<Long> productArrayList) {
        this.productArrayList = productArrayList;
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCheckoutCardLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCheckoutCardLayoutBinding.bind(itemView);
        }
    }

    public void incrementAmountAndUpdateTotalPrice(ProductWrapper productWrapper, ItemCheckoutCardLayoutBinding binding) {
        productWrapper.setAddOnAmount(productWrapper.getAddOnAmount() + 1);
        binding.setAddOnAmount(productWrapper.getAddOnAmount());
        onTotalAmountChanged.onChanged(getTotalPrice());
    }

    public void decrementAmountAndUpdateTotalPrice(int index, @NonNull ProductWrapper productWrapper, ItemCheckoutCardLayoutBinding binding) {
        Integer addOnAmount = productWrapper.getAddOnAmount();
        Long productId = productWrapper.getProduct().getProduct_id();
        if (addOnAmount == 1) {
            selectedProducts.remove(productId);
            productArrayList.remove(index);
            notifyItemRemoved(index);
        } else if (addOnAmount > 1) {
            productWrapper.setAddOnAmount(addOnAmount - 1);
        }
        binding.setAddOnAmount(productWrapper.getAddOnAmount());
        onTotalAmountChanged.onChanged(getTotalPrice());
    }
}
