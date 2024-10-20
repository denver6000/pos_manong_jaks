package com.denproj.posmanongjaks.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = Sale.class, childColumns = "saleId", parentColumns = "saleId"),
        @ForeignKey(entity = Item.class, childColumns = "item_id", parentColumns = "item_id")})
public class SaleItem {


    @PrimaryKey(autoGenerate = true)
    int saleItemId;
    int item_id;
    String saleId;
    int amount;

    String itemName;

    public SaleItem() {
    }

    public SaleItem(int item_id, String saleId, int amount, String itemName) {
        this.item_id = item_id;
        this.saleId = saleId;
        this.amount = amount;
        this.itemName = itemName;
    }

    public int getSaleItemId() {
        return saleItemId;
    }

    public void setSaleItemId(int saleItemId) {
        this.saleItemId = saleItemId;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
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
        return this.itemName;
    }
}
