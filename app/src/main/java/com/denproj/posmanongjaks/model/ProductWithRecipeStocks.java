package com.denproj.posmanongjaks.model;

import java.util.List;

public class ProductWithRecipeStocks {

    Product product;

    List<Item> recipe;

    public ProductWithRecipeStocks(Product product, List<Item> recipe) {
        this.product = product;
        this.recipe = recipe;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Item> getRecipe() {
        return recipe;
    }

    public void setRecipe(List<Item> recipe) {
        this.recipe = recipe;
    }
}
