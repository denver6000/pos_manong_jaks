package com.denproj.posmanongjaks.model;

import java.util.List;

public class CompleteSaleInfo {
    Sale sale;
    List<SaleItem> saleItems;
    List<SaleProduct> saleProducts;

    public CompleteSaleInfo(Sale sale, List<SaleItem> saleItems, List<SaleProduct> saleProducts) {
        this.sale = sale;
        this.saleItems = saleItems;
        this.saleProducts = saleProducts;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public List<SaleItem> getSaleItems() {
        return saleItems;
    }

    public void setSaleItems(List<SaleItem> saleItems) {
        this.saleItems = saleItems;
    }

    public List<SaleProduct> getSaleProducts() {
        return saleProducts;
    }

    public void setSaleProducts(List<SaleProduct> saleProducts) {
        this.saleProducts = saleProducts;
    }
}
