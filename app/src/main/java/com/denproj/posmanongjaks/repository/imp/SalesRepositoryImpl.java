package com.denproj.posmanongjaks.repository.imp;

import android.util.Log;

import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.repository.base.SaleRepository;
import com.denproj.posmanongjaks.room.dao.ProductsDao;
import com.denproj.posmanongjaks.util.AsyncRunner;
import com.denproj.posmanongjaks.util.OnDataReceived;

public class SalesRepositoryImpl implements SaleRepository {

    ProductsDao productsDao;

    public SalesRepositoryImpl(ProductsDao productsDao) {
        this.productsDao = productsDao;
    }

    @Override
    public void insertSaleRecord(int amount, Product product, int year, int month, int day, OnDataReceived<Void> onDataReceived) {
        AsyncRunner.runAsync(new AsyncRunner.Runner<Void>() {
            @Override
            public Void onBackground() {
                boolean doesProductExist = productsDao.checkIfProductIsSold(product.getProduct_id());
                if (doesProductExist) {
                    productsDao.updateSaleAmountOfProduct(amount, product.getProduct_id());
                } else {
                    productsDao.insertProduct(product);
                    Sale sale = new Sale(product.getProduct_id(), amount, year, month, day);
                    productsDao.insertSale(sale);
                }
                return null;
            }

            @Override
            public void onFinished(Void result) {
                Log.d("SalesRepository", "Done");
            }

            @Override
            public void onUI(Void result) {

            }

            @Override
            public void onError(Exception e) {

                Log.e("SalesRepository", e.getMessage());
            }
        });
    }

    @Override
    public void getSaleRecord(int productId, OnDataReceived<Sale> sale) {

    }
}
