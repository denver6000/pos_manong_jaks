package com.denproj.posmanongjaks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.SelectableItemCardBinding;
import com.denproj.posmanongjaks.model.Item;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class ItemSelectionRecyclerViewAdapter extends RecyclerView.Adapter<ItemSelectionRecyclerViewAdapter.ViewHolder> {

    List<Item> globalList;
    HashMap<String, Item> selectedItemsMap = new HashMap<>();

    public ItemSelectionRecyclerViewAdapter(List<Item> globalList) {
        this.globalList = globalList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.selectable_item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = globalList.get(position);
        SelectableItemCardBinding binding = holder.binding;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storage.getReference(item.getItemImage()).getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(binding.getRoot().getContext())
                    .load(uri)
                    .into(binding.productImageView);
        });
        binding.itemCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                selectedItemsMap.put(item.getItemId(), item);
            } else {
                selectedItemsMap.remove(item.getItemId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return globalList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        SelectableItemCardBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SelectableItemCardBinding.bind(itemView);
        }

    }

    public HashMap<String, Item> getSelectedItems() {
        return this.selectedItemsMap;
    }
}
