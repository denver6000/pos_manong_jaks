package com.denproj.posmanongjaks.model;

import androidx.annotation.NonNull;

public class ProductWrapper {
    private Integer addOnAmount;

    private Product product;

    public ProductWrapper(Integer addOnAmount, Product product) {
        this.addOnAmount = addOnAmount;
        this.product = product;
    }

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
