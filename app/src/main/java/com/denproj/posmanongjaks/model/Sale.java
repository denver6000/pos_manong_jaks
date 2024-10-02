package com.denproj.posmanongjaks.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Product.class, parentColumns = "product_id", childColumns = "productId"))
public class Sale {

    @PrimaryKey(autoGenerate = true)
    private int saleId;
    private int productId;
    private int saleAmount;
    private int year;
    private int month;
    private int day;

    public Sale() {
    }

    public Sale(int productId, int saleAmount, int year, int month, int day) {
        this.productId = productId;
        this.saleAmount = saleAmount;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(int saleAmount) {
        this.saleAmount = saleAmount;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
