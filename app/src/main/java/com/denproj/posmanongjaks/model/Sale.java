package com.denproj.posmanongjaks.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.List;

@Entity
public class Sale {

    @PrimaryKey
    @NonNull
    @Exclude
    @com.google.firebase.database.Exclude
    private String saleId;
    @Exclude
    @com.google.firebase.database.Exclude
    private int year;
    @Exclude
    @com.google.firebase.database.Exclude
    private int month;
    @Exclude
    @com.google.firebase.database.Exclude
    private int day;
    private String branchId;
    @Ignore
    private HashMap<String, SaleItem> itemsSold;
    @Ignore
    private HashMap<String, SaleProduct> productSold;

    @Ignore
    private List<SaleItem> soldItems;
    @Ignore
    private List<SaleProduct> soldProducts;

    @Ignore
    private Timestamp timestamp;
    private Double total = 0d;
    private Double paidAmount = 0d;
    private Double change = 0d;


    public Sale(String saleId, int year, int month, int day, String branchId, HashMap<String, SaleItem> itemsSold, Timestamp timestamp, Double total, Double paidAmount, Double change) {
        this.saleId = saleId;
        this.year = year;
        this.month = month;
        this.day = day;
        this.branchId = branchId;
        this.itemsSold = itemsSold;
        this.timestamp = timestamp;
        this.total = total;
        this.paidAmount = paidAmount;
        this.change = change;
    }

    public Sale() {
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
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

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public HashMap<String, SaleItem> getItemsSold() {
        return itemsSold;
    }

    public void setItemsSold(HashMap<String, SaleItem> itemsSold) {
        this.itemsSold = itemsSold;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Double getChange() {
        return change;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public HashMap<String, SaleProduct> getProductSold() {
        return productSold;
    }

    public void setProductSold(HashMap<String, SaleProduct> productSold) {
        this.productSold = productSold;
    }

    public List<SaleItem> getSoldItems() {
        return soldItems;
    }

    public void setSoldItems(List<SaleItem> soldItems) {
        this.soldItems = soldItems;
    }

    public List<SaleProduct> getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(List<SaleProduct> soldProducts) {
        this.soldProducts = soldProducts;
    }
}
