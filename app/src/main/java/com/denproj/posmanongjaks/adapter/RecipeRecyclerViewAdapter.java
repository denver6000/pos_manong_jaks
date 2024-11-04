package com.denproj.posmanongjaks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.RecipeCardBinding;
import com.denproj.posmanongjaks.model.Item;

import java.util.List;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder>{

    List<Item> items;

    public RecipeRecyclerViewAdapter(List<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeCardBinding binding = holder.getBinding();
        Item item = items.get(holder.getAdapterPosition());
        binding.setProductName(item.getItem_name());
        binding.setProductPrice(String.valueOf(item.getItem_price()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecipeCardBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RecipeCardBinding.bind(itemView);
        }

        public RecipeCardBinding getBinding() {
            return binding;
        }
    }
}
