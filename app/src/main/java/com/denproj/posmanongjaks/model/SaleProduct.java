package com.denproj.posmanongjaks.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = Product.class, childColumns = "product_id", parentColumns = "product_id"),
        @ForeignKey(entity = Sale.class, childColumns = "sale_id", parentColumns = "saleId")})
public class SaleProduct {


    @PrimaryKey(autoGenerate = true)
    int saleItemId;
    int product_id;
    int amount;
    String sale_id;
    String name;

    public SaleProduct(int product_id, int amount, String sale_id, String name) {
        this.product_id = product_id;
        this.amount = amount;
        this.sale_id = sale_id;
        this.name = name;
    }

    public int getSaleItemId() {
        return saleItemId;
    }

    public void setSaleItemId(int saleItemId) {
        this.saleItemId = saleItemId;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSale_id() {
        return sale_id;
    }

    public void setSale_id(String sale_id) {
        this.sale_id = sale_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}