package com.denproj.posmanongjaks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.ItemCardBinding;
import com.denproj.posmanongjaks.databinding.ItemCardWithStockBinding;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.util.ImagePathBinder;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class ItemsRecyclerViewAdapter extends RecyclerView.Adapter<ItemsRecyclerViewAdapter.ViewHolder> {
    List<Item> items = new ArrayList<>();
    NavigateWithCardInfo navigate;

    public ItemsRecyclerViewAdapter(NavigateWithCardInfo navigate) {
        this.navigate = navigate;
    }

    public void onDataSetChanged(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
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
        binding.setItemName(item.getItemName());
        binding.setItemStockCount(item.getStockNumber() + "");
        ImagePathBinder.bindImagePathToImageView(item.getItemImage(), binding.stockItemImageView, binding.getRoot().getContext(), new ImagePathBinder.OnImageBound() {
            @Override
            public void onBound() {

            }

            @Override
            public void onError(Exception e) {

            }
        });

        binding.getRoot().setOnClickListener(view -> {
            navigate.onNavigate(item.getItemId());
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

    public interface NavigateWithCardInfo {
        void onNavigate(String itemId);
    }

}
