package com.denproj.posmanongjaks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.RecipeViewOnlyCardBinding;
import com.denproj.posmanongjaks.model.Recipe;
import java.util.HashMap;

public class RecipeViewerRecyclerViewAdapter extends RecyclerView.Adapter<RecipeViewerRecyclerViewAdapter.ViewHolder> {
    HashMap<String, Recipe> selectedRecipes = new HashMap<>();

    public HashMap<String, Recipe> getSelectedRecipes() {
        return selectedRecipes;
    }

    public void setSelectedRecipes(HashMap<String, Recipe> selectedRecipes) {
        this.selectedRecipes = selectedRecipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_view_only_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Object[] keyset = selectedRecipes.keySet().stream().toArray();
        Object key = keyset[position];
        Recipe recipe = selectedRecipes.get(key.toString());

        RecipeViewOnlyCardBinding binding = holder.getBinding();
        binding.setItemAmount("x" + recipe);
        binding.setItemName(recipe.getItemName());
    }

    @Override
    public int getItemCount() {
        return selectedRecipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecipeViewOnlyCardBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RecipeViewOnlyCardBinding.bind(itemView);
        }

        public RecipeViewOnlyCardBinding getBinding() {
            return binding;
        }

        public void setBinding(RecipeViewOnlyCardBinding binding) {
            this.binding = binding;
        }
    }
}
