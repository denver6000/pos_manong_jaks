package com.denproj.posmanongjaks.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Item {

    private String item_image_path;

    @PrimaryKey
    private int item_id;
    private String item_name;
    private String item_category;
    private int item_quantity;
    private String item_unit;
    private double item_price;
    private boolean ads_on;


    @Ignore
    public Item(String item_image_path, int item_id, String item_name, String item_category, int item_quantity, String item_unit, double item_price, boolean ads_on) {
        this.item_image_path = item_image_path;
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_category = item_category;
        this.item_quantity = item_quantity;
        this.item_unit = item_unit;
        this.item_price = item_price;
        this.ads_on = ads_on;
    }

    public boolean isAds_on() {
        return ads_on;
    }

    public void setAds_on(boolean ads_on) {
        this.ads_on = ads_on;
    }

    public Item() {

    }

    public String getItem_image_path() {
        return item_image_path;
    }

    public void setItem_image_path(String item_image_path) {
        this.item_image_path = item_image_path;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_category() {
        return item_category;
    }

    public void setItem_category(String item_category) {
        this.item_category = item_category;
    }

    public int getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(int item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItem_unit() {
        return item_unit;
    }

    public void setItem_unit(String item_unit) {
        this.item_unit = item_unit;
    }

    public double getItem_price() {
        return item_price;
    }

    public void setItem_price(double item_price) {
        this.item_price = item_price;
    }
    @NonNull
    @Override
    public String toString() {
        return item_name;
    }
}

