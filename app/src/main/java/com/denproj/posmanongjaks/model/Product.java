package com.denproj.posmanongjaks.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.HashMap;

@Entity
@IgnoreExtraProperties
public class Product {

    @PrimaryKey
    private Long product_id;
    private String product_name;
    private String product_image_path;
    private float product_price;
    private String product_category;

    @Ignore
    private HashMap<String, Recipe> recipes;
    public HashMap<String, Recipe> getRecipes() {
        return recipes;
    }
    public void setRecipes(HashMap<String, Recipe> recipes) {
        this.recipes = recipes;
    }
    public Product() {

    }

    public Product(@NonNull Long product_id, String product_name, String product_image_path, float product_price, String product_category, HashMap<String, Recipe> recipes) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_image_path = product_image_path;
        this.product_price = product_price;
        this.product_category = product_category;
        this.recipes = recipes;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image_path() {
        return product_image_path;
    }

    public void setProduct_image_path(String product_image_path) {
        this.product_image_path = product_image_path;
    }

    public float getProduct_price() {
        return product_price;
    }

    public void setProduct_price(float product_price) {
        this.product_price = product_price;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(product_id);
    }
}