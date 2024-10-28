package com.denproj.posmanongjaks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.ItemCardWithStockBinding;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.util.ImagePathBinder;

import java.util.ArrayList;
import java.util.List;

public class ItemsRecyclerViewAdapter extends RecyclerView.Adapter<ItemsRecyclerViewAdapter.ViewHolder> {
    List<Item> items = new ArrayList<>();

    public void onDataSetChanged(List<Item> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyItemRangeChanged(0, items.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_with_stock, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Item item = items.get(position);
        ItemCardWithStockBinding binding = holder.binding;
        binding.setItemName(item.getItem_name());
        binding.setItemStockCount(item.getItem_quantity() + "");
        binding.setItemUnit(item.getItem_unit());
        binding.setItemPrice(item.getItem_price() + "Pesos");
        ImagePathBinder.bindImagePathToImageView(item.getItem_image_path(), binding.stockItemImageView, binding.getRoot().getContext(), new ImagePathBinder.OnImageBound() {
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
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemCardWithStockBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.binding = ItemCardWithStockBinding.bind(itemView);
        }
    }
}
