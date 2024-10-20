package com.denproj.posmanongjaks.adapter.simple;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.denproj.posmanongjaks.databinding.ItemCheckoutCardLayoutBinding;
import com.denproj.posmanongjaks.model.AddOn;
import com.denproj.posmanongjaks.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckOutArrayAdapter extends ArrayAdapter<Product> {

    HashMap<Product, AddOn> selectedItems;
    List<Product> productsList;
    public CheckOutArrayAdapter(HashMap<Product, AddOn> selectedItems, @NonNull Context context, int resource) {

        super(context, resource, new ArrayList<>(selectedItems.keySet()));
        this.selectedItems = selectedItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ItemCheckoutCardLayoutBinding binding = ItemCheckoutCardLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        Product product = getItem(position);
        AddOn addOn = selectedItems.get(product);





        return super.getView(position, convertView, parent);
    }

    @Nullable
    @Override
    public Product getItem(int position) {
        return super.getItem(position);
    }


    @Override
    public int getCount() {
        return selectedItems.size();
    }
}
