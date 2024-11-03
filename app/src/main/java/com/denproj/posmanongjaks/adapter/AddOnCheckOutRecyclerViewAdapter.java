package com.denproj.posmanongjaks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.ItemCheckoutCardLayoutBinding;
import com.denproj.posmanongjaks.dialog.CheckOutDialogFragment;
import com.denproj.posmanongjaks.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddOnCheckOutRecyclerViewAdapter extends RecyclerView.Adapter<AddOnCheckOutRecyclerViewAdapter.ViewHolder> {

    HashMap<Item, Integer> selectedAddOns;
    List<Item> items;

    CheckOutDialogFragment.OnTotalAmountChanged totalAmountChanged;

    public AddOnCheckOutRecyclerViewAdapter(HashMap<Item, Integer> selectedAddOns) {
        this.selectedAddOns = selectedAddOns;
        this.items = new ArrayList<>(selectedAddOns.keySet());
    }

    public AddOnCheckOutRecyclerViewAdapter(HashMap<Item, Integer> selectedAddOns, CheckOutDialogFragment.OnTotalAmountChanged totalAmountChanged) {
        this.selectedAddOns = selectedAddOns;
        this.totalAmountChanged = totalAmountChanged;
        this.items = new ArrayList<>(selectedAddOns.keySet());
    }

    @NonNull
    @Override
    public AddOnCheckOutRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkout_card_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddOnCheckOutRecyclerViewAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);
        ItemCheckoutCardLayoutBinding binding = holder.binding;
        binding.setProductName(item.getItem_name());

        Integer initialStock = selectedAddOns.get(item);
        binding.setAddOnAmount(initialStock);
        totalAmountChanged.onChanged(item.getItem_price() * binding.getAddOnAmount());

        binding.incrementAmount.setOnClickListener(view -> {
            int amount = binding.getAddOnAmount() + 1;
            selectedAddOns.put(item, amount);
            binding.setAddOnAmount(amount);
            totalAmountChanged.onChanged(0d);
        });

        binding.decrementAmount.setOnClickListener(view -> {
            int amount = binding.getAddOnAmount();
            if (amount == 1) {
//                selectedAddOns.remove(item);
//                items.remove(holder.getAdapterPosition());
//                notifyItemRemoved(holder.getAdapterPosition());
            } else if (amount > 1) {
                amount = amount - 1;
                selectedAddOns.put(item, amount);
            }
            binding.setAddOnAmount(amount);
            totalAmountChanged.onChanged(getTotal());
        });
    }

    public double getTotal() {
        ArrayList<Integer> prices = new ArrayList<>(selectedAddOns.values());
        double total = 0;
        for (int i = 0; i < prices.size(); i++) {
            Item item = items.get(i);
            Integer amount = prices.get(i);
            total += amount * item.getItem_price();
        }
        return total;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemCheckoutCardLayoutBinding binding;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCheckoutCardLayoutBinding.bind(itemView);
        }
    }

    public HashMap<Item, Integer> getSelectedAddOns() {
        return selectedAddOns;
    }

    public void setSelectedAddOns(HashMap<Item, Integer> selectedAddOns) {
        this.selectedAddOns = selectedAddOns;
    }
}
