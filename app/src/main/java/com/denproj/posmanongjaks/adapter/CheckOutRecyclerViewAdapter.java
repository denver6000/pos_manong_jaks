package com.denproj.posmanongjaks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.ItemCheckoutCardLayoutBinding;
import com.denproj.posmanongjaks.dialog.CheckOutDialogFragment;
import com.denproj.posmanongjaks.model.AddOn;
import com.denproj.posmanongjaks.model.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckOutRecyclerViewAdapter extends RecyclerView.Adapter<CheckOutRecyclerViewAdapter.ViewHolder> {
    HashMap<String, AddOn> selectedItems;
    ArrayList<String> productArrayList;
    CheckOutDialogFragment.OnTotalAmountChanged onTotalAmountChanged;


    public CheckOutRecyclerViewAdapter(HashMap<String, AddOn> selectedItems, CheckOutDialogFragment.OnTotalAmountChanged totalAmountChanged) {
        this.selectedItems = selectedItems;
        this.onTotalAmountChanged = totalAmountChanged;
        this.productArrayList = new ArrayList<>(selectedItems.keySet());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkout_card_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String key = productArrayList.get(position);
        AddOn addOn = selectedItems.get(key);
        ItemCheckoutCardLayoutBinding binding = holder.binding;
        if (addOn != null) {

            binding.incrementAmount.setOnClickListener(view -> {
                incrementAmountAndUpdateTotalPrice(addOn, binding);
            });

            binding.decrementAmount.setOnClickListener(view -> {
                decrementAmountAndUpdateTotalPrice(addOn, binding);
            });

            incrementAmountAndUpdateTotalPrice(addOn, binding);
        }
    }

    public double getTotalPrice () {

        double total = 0f;
        for (int i = 0; i < selectedItems.size(); i++) {
            String key = productArrayList.get(i);
            AddOn addOn = selectedItems.get(key);
            Integer amount = addOn.getAddOnAmount();
//            Item item = addOn.getAddOnItem();
//            if (item != null) {
//                total += item.getItem_price() * amount;
//            }
            total += addOn.getProduct().getProduct_price() * amount;
        }
        return total;
    }

    public HashMap<String, AddOn> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(HashMap<String, AddOn> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public ArrayList<String> getProductArrayList() {
        return productArrayList;
    }

    public void setProductArrayList(ArrayList<String> productArrayList) {
        this.productArrayList = productArrayList;
    }

    @Override
    public int getItemCount() {
        return selectedItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCheckoutCardLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCheckoutCardLayoutBinding.bind(itemView);
        }
    }

    public void incrementAmountAndUpdateTotalPrice(AddOn addOn, ItemCheckoutCardLayoutBinding binding) {
        addOn.setAddOnAmount(addOn.getAddOnAmount() + 1);
        binding.setAddOnAmount(addOn.getAddOnAmount());
        onTotalAmountChanged.onChanged(getTotalPrice());
    }

    public void decrementAmountAndUpdateTotalPrice(AddOn addOn, ItemCheckoutCardLayoutBinding binding) {
        Integer addOnAmount = addOn.getAddOnAmount();
        if (addOnAmount <= 0) {
            selectedItems.remove(addOn.getProduct());
        } else {
            addOn.setAddOnAmount(addOnAmount - 1);
        }
        binding.setAddOnAmount(addOn.getAddOnAmount());
        onTotalAmountChanged.onChanged(getTotalPrice());
    }
}
