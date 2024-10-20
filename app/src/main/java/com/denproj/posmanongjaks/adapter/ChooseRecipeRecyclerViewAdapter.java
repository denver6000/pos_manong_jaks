package com.denproj.posmanongjaks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.RecipeCardBinding;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Recipe;
import com.denproj.posmanongjaks.util.ImagePathBinder;

import java.util.HashMap;
import java.util.List;

public class ChooseRecipeRecyclerViewAdapter extends RecyclerView.Adapter<ChooseRecipeRecyclerViewAdapter.ViewHolder> {

    private List<Item> itemsList;
    private HashMap<String, Recipe> selectedRecipes = new HashMap<>();

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

        ImagePathBinder.bindImagePathToImageView(item.getItem_image_path(), binding.recipeImagePreview, binding.getRoot().getContext(), new ImagePathBinder.OnImageBound() {
            @Override
            public void onBound() {

            }

            @Override
            public void onError(Exception e) {

            }
        });

        binding.itemAmountSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressChangedValue = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String itemId = String.valueOf(item.getItem_id());
                binding.setProductAmount(String.valueOf(progressChangedValue));
                if (progressChangedValue > 0) {
                    recipe.setAmount(progressChangedValue);
                    selectedRecipes.put(itemId, recipe);
                } else {
                    selectedRecipes.remove(itemId);
                }
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

    public HashMap<String, Recipe> getSelectedRecipes() {
        return selectedRecipes;
    }
}
