package com.denproj.posmanongjaks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.AddOnLayoutBinding;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.util.ImagePathBinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddOnRecyclerViewAdapter extends RecyclerView.Adapter<AddOnRecyclerViewAdapter.ViewHolder> {
    List<Item> items = new ArrayList<>();
    HashMap<Item, Integer> itemsMap = new HashMap<>();



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.add_on_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        AddOnLayoutBinding addOnLayoutBinding = holder.binding;
        addOnLayoutBinding.setAddOnName(item.getItem_name());
        addOnLayoutBinding.setAddOnPrice(String.valueOf(item.getItem_price()));
        addOnLayoutBinding.setStockCount("Stocks: " + item.getItem_quantity());
        ImagePathBinder.bindImagePathToImageView(item.getItem_image_path(), addOnLayoutBinding.imageView4, addOnLayoutBinding.getRoot().getContext(), new ImagePathBinder.OnImageBound() {
            @Override
            public void onBound() {

            }

            @Override
            public void onError(Exception e) {

            }
        });
        addOnLayoutBinding.addAdOnsToCheckOut.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                itemsMap.put(item, 1);
            } else {
                itemsMap.remove(item);
            }
        });
    }

    public void refreshAdapter(List<Item> addOns) {
        items.clear();
        items.addAll(addOns);
        notifyDataSetChanged();
    }

    public HashMap<Item, Integer> getItemsMap() {
        return itemsMap;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        AddOnLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AddOnLayoutBinding.bind(itemView);
        }
    }

    public void clearSelectedItems() {
        this.itemsMap.clear();
        notifyItemRangeChanged(0, this.items.size());
    }


}
