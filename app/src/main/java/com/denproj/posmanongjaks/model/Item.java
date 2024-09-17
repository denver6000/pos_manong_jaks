package com.denproj.posmanongjaks.model;


public class Item {

    private String itemImage;

    private String itemId;

    private String itemName;

    private int stockNumber = 0;

    public Item () {

    }

    public Item(String itemImage, String itemId, String itemName) {
        this.itemImage = itemImage;
        this.itemId = itemId;
        this.itemName = itemName;
    }

    public Item(String itemImage, String itemName, int stockNumber) {
        this.itemImage = itemImage;
        this.itemId = itemName;
        this.stockNumber = stockNumber;
    }

    public String getItemImage() {
        return itemImage;
    }


    public String getItemId() {
        return itemId;
    }


    public int getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(int stockNumber) {
        this.stockNumber = stockNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}

