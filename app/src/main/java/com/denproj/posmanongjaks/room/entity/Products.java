package com.denproj.posmanongjaks.room.entity;


import androidx.room.Entity;

@Entity
public class Products {

    private String productId;
    private String name;
    public String pathToImage;
    private String price;

    public Products(String productId, String name, String pathToImage, String price) {
        this.productId = productId;
        this.name = name;
        this.pathToImage = pathToImage;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }


    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return name;
    }

    public void setProductName(String productName) {
        this.name = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
