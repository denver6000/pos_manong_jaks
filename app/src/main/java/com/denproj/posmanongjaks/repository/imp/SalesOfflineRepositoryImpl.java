package com.denproj.posmanongjaks.repository.imp;

import androidx.lifecycle.LiveData;

import com.denproj.posmanongjaks.model.ProductWrapper;
import com.denproj.posmanongjaks.model.CompleteSaleInfo;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.model.SaleItem;
import com.denproj.posmanongjaks.model.SaleProduct;
import com.denproj.posmanongjaks.repository.base.SaleRepository;
import com.denproj.posmanongjaks.room.dao.SalesDao;
import com.denproj.posmanongjaks.util.AsyncRunner;
import com.denproj.posmanongjaks.util.EfficientRandomStringGenerator;
import com.denproj.posmanongjaks.util.OnDataReceived;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SalesOfflineRepositoryImpl implements SaleRepository {

    SalesDao salesDao;

    public SalesOfflineRepositoryImpl(SalesDao productsDao) {
        this.salesDao = productsDao;
    }

    @Override
    public void insertSaleRecord(String branchId, HashMap<Long, ProductWrapper> selectedProductToSell, HashMap<Item, Integer> addOns, int year, int month, int day, Double total, Double amountToBePaid, OnDataReceived<CompleteSaleInfo> onDataReceived) {
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
        AsyncRunner.runAsync(new AsyncRunner.Runner<CompleteSaleInfo>() {
            @Override
            public CompleteSaleInfo onBackground() throws Exception {
                List<SaleProduct> saleProducts = new ArrayList<>();
                List<SaleItem> saleItems = new ArrayList<>();
                selectedProductToSell.forEach((s, addOn) -> {
                    SaleProduct saleProduct = new SaleProduct(addOn.getProduct().getProduct_id(), addOn.getAddOnAmount(), saleRecord.getSaleId(), addOn.getProduct().getProduct_name());
                    saleProducts.add(saleProduct);
                });
                addOns.forEach((item, integer) -> {
                    SaleItem saleItem = new SaleItem(item.getItem_id(), saleRecord.getSaleId(), integer, item.getItem_name());
                    saleItems.add(saleItem);
                });
                return new CompleteSaleInfo(saleRecord, saleItems, saleProducts);
            }

            @Override
            public void onFinished(CompleteSaleInfo result) {
            }

            @Override
            public void onUI(CompleteSaleInfo result) {
                onDataReceived.onSuccess(result);
            }

            @Override
            public void onError(Exception e) {
                onDataReceived.onFail(e);
            }
        });
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
    public CompletableFuture<List<Sale>> getAllRecordedSalesOnBranch(String branchId) {
        return CompletableFuture.supplyAsync(() -> salesDao.getAllRecordedSalesAsync(branchId));
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
