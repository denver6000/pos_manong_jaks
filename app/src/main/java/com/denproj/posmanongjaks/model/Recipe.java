package com.denproj.posmanongjaks.model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Recipe {
    private Integer amount;


    private String itemName;


    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getItemName() + "\n x " + getAmount();
    }
}
