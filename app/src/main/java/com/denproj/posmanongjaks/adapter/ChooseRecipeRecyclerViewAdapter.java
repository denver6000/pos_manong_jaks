package com.denproj.posmanongjaks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.RecipeCardBinding;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Recipe;

import java.util.HashMap;
import java.util.List;

public class ChooseRecipeRecyclerViewAdapter extends RecyclerView.Adapter<ChooseRecipeRecyclerViewAdapter.ViewHolder> {

    private List<Item> itemsList;
    private HashMap<Integer, Recipe> selectedRecipes = new HashMap<>();

    public ChooseRecipeRecyclerViewAdapter(List<Item> itemsList) {
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemsList.get(position);
        RecipeCardBinding binding = holder.getBinding();
        binding.setProductName(item.getItem_name());
        binding.setProductPrice(item.getItem_price() + "");
        Recipe recipe = new Recipe();
        recipe.setItemName(item.getItem_name());
        binding.itemAmountSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i > 0) {
                    recipe.setAmount(i);
                    selectedRecipes.put(item.getItem_id(), recipe);
                } else {
                    selectedRecipes.remove(item.getItem_id());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
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

    public HashMap<Integer, Recipe> getSelectedRecipes() {
        return selectedRecipes;
    }
}
