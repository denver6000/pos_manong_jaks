package com.denproj.posmanongjaks.model;

public class StockAmountAndAmountToReduce {
    private Integer StockAmount;
    private Integer AmountToReduce;

    public StockAmountAndAmountToReduce(Integer stockAmount, Integer amountToReduce) {
        StockAmount = stockAmount;
        AmountToReduce = amountToReduce;
    }

    public Integer getStockAmount() {
        return StockAmount;
    }

    public void setStockAmount(Integer stockAmount) {
        StockAmount = stockAmount;
    }

    public Integer getAmountToReduce() {
        return AmountToReduce;
    }

    public void setAmountToReduce(Integer amountToReduce) {
        AmountToReduce = amountToReduce;
    }
}
