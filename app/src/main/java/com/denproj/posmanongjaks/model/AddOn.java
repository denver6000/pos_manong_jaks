package com.denproj.posmanongjaks.model;

import androidx.annotation.NonNull;

public class AddOn {
//    private Item addOnItem;
    private Integer addOnAmount;

    private Product product;

    public AddOn(Integer addOnAmount, Product product) {
//        this.addOnItem = item;
        this.addOnAmount = addOnAmount;
        this.product = product;
    }

//    public Item getAddOnItem() {
//        return addOnItem;
//    }
//
//    public void setAddOnItem(Item addOnItem) {
//        this.addOnItem = addOnItem;
//    }

    public Integer getAddOnAmount() {
        return addOnAmount;
    }

    public void setAddOnAmount(Integer addOnAmount) {
        this.addOnAmount = addOnAmount;
    }


    @NonNull
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
