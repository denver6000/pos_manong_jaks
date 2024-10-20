package com.denproj.posmanongjaks.repository.imp;

import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.repository.base.AddOnsRepository;
import com.denproj.posmanongjaks.room.dao.ProductsDao;
import com.denproj.posmanongjaks.util.AsyncRunner;
import com.denproj.posmanongjaks.util.OnDataReceived;

import java.util.List;

import javax.inject.Inject;

public class AddOnOfflineRepositoryImpl implements AddOnsRepository {

    ProductsDao productsDao;

    @Inject
    public AddOnOfflineRepositoryImpl(ProductsDao productsDao) {
        this.productsDao = productsDao;
    }

    @Override
    public void getAddOnsRepository(OnDataReceived<List<Item>> onDataReceived) {
        AsyncRunner.runAsync(new AsyncRunner.Runner<List<Item>>() {
            @Override
            public List<Item> onBackground() throws Exception {
                return productsDao.getAllAdOns();
            }

            @Override
            public void onFinished(List<Item> result) {

            }

            @Override
            public void onUI(List<Item> result) {
                onDataReceived.onSuccess(result);
            }

            @Override
            public void onError(Exception e) {
                onDataReceived.onFail(e);
            }
        });
    }
}
