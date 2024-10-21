package com.denproj.posmanongjaks.repository.imp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.denproj.posmanongjaks.model.AddOn;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.model.SaleItem;
import com.denproj.posmanongjaks.model.SaleProduct;
import com.denproj.posmanongjaks.repository.base.SaleRepository;
import com.denproj.posmanongjaks.room.dao.SalesDao;
import com.denproj.posmanongjaks.room.view.SalesWithProduct;
import com.denproj.posmanongjaks.util.AsyncRunner;
import com.denproj.posmanongjaks.util.EfficientRandomStringGenerator;
import com.denproj.posmanongjaks.util.OnDataReceived;

import java.util.HashMap;
import java.util.List;

public class SalesOfflineRepositoryImpl implements SaleRepository {

    SalesDao salesDao;

    public SalesOfflineRepositoryImpl(SalesDao productsDao) {
        this.salesDao = productsDao;
    }

    @Override
    public void insertSaleRecord(String branchId, HashMap<String, AddOn> selectedProductToSell, HashMap<Item, Integer> addOns, int year, int month, int day, Double total, Double amountToBePaid, OnDataReceived<Void> onDataReceived) {
        Sale saleRecord = new Sale();
        saleRecord.setSaleId(EfficientRandomStringGenerator.generateRandomTenDigitString());
        Double change = amountToBePaid - total;
        saleRecord.setChange(change);
        saleRecord.setTotal(total);
        saleRecord.setMonth(month);
        saleRecord.setDay(day);
        saleRecord.setYear(year);
        saleRecord.setPaidAmount(amountToBePaid);
        saleRecord.setBranchId(branchId);
        AsyncRunner.runAsync(new AsyncRunner.Runner<Void>() {
            @Override
            public Void onBackground() throws Exception {
                salesDao.insertSale(saleRecord);

                selectedProductToSell.forEach((s, addOn) -> {
                    salesDao.insertSaleProduct(new SaleProduct(addOn.getProduct().getProduct_id(), addOn.getAddOnAmount(), saleRecord.getSaleId(), addOn.getProduct().getProduct_name()));
                });

                addOns.forEach((item, integer) -> {
                    salesDao.insertSaleItem(new SaleItem(item.getItem_id(), saleRecord.getSaleId(), integer, item.getItem_name()));
                });

                return null;
            }

            @Override
            public void onFinished(Void result) {
            }

            @Override
            public void onUI(Void result) {
                onDataReceived.onSuccess(result);
            }

            @Override
            public void onError(Exception e) {
                onDataReceived.onFail(e);
            }
        });
    }

    @Override
    public void getSaleRecord(int productId, OnDataReceived<Sale> sale) {

    }

    @Override
    public List<SalesWithProduct> getAllSalesRecord() {
        return (List<SalesWithProduct>) new MutableLiveData<>();
    }

    @Override
    public LiveData<List<SaleItem>> getAllAddOnsWithSaleId(String saleId) {
        return salesDao.getAllSaleItemWithSaleIdAsync(saleId);
    }

    @Override
    public LiveData<List<SaleProduct>> getAllProductsWithSaleId(String saleId) {
        return salesDao.getAllSaleProductWithSaleIdAsync(saleId);
    }

    @Override
    public LiveData<List<Sale>> getAllRecordedSales() {
        return salesDao.getAllRecordedSalesAsync();
    }

    @Override
    public List<SaleItem> getAllSaleItemBySaleIdSync(String saleId) {
        return salesDao.getAllSaleItemWithSaleIdSync(saleId);
    }

    @Override
    public List<SaleProduct> getAllSaleProductBySaleIdSync(String saleId) {
        return salesDao.getAllSaleProductWithSaleIdSync(saleId);
    }


}
