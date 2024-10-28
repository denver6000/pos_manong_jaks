package com.denproj.posmanongjaks.repository.imp;

import androidx.lifecycle.LiveData;

import com.denproj.posmanongjaks.model.ProductWrapper;
import com.denproj.posmanongjaks.model.CompleteSaleInfo;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Sale;
import com.denproj.posmanongjaks.model.SaleItem;
import com.denproj.posmanongjaks.model.SaleProduct;
import com.denproj.posmanongjaks.repository.base.SaleRepository;
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseSaleRepository;
import com.denproj.posmanongjaks.room.dao.SalesDao;
import com.denproj.posmanongjaks.util.AsyncRunner;
import com.denproj.posmanongjaks.util.OnDataReceived;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class SalesOfflineRepositoryImpl implements SaleRepository {

    SalesDao salesDao;

    public SalesOfflineRepositoryImpl(SalesDao productsDao) {
        this.salesDao = productsDao;
    }

    @Override
    public void processSale(String branchId, HashMap<Long, ProductWrapper> selectedProductToSell, HashMap<Item, Integer> addOns, int year, int month, int day, Double total, Double amountToBePaid, FirebaseSaleRepository.OnSaleStatus saleStatus) {
//        Sale saleRecord = new Sale();
//        saleRecord.setSaleId(Integer.valueOf(generateId()));
//        //saleRecord.setSaleId(EfficientRandomStringGenerator.generateRandomTenDigitString());
//        Double change = amountToBePaid - total;
//        saleRecord.setChange(change);
//        saleRecord.setTotal(total);
//        saleRecord.setMonth(month);
//        saleRecord.setDay(day);
//        saleRecord.setYear(year);
//        saleRecord.setPaidAmount(amountToBePaid);
//        saleRecord.setBranchId(branchId);
//
//        AsyncRunner.runAsync(new AsyncRunner.Runner<CompleteSaleInfo>() {
//            @Override
//            public CompleteSaleInfo onBackground() throws Exception {
//                List<SaleProduct> saleProducts = new ArrayList<>();
//                List<SaleItem> saleItems = new ArrayList<>();
//                salesDao.insertSale(saleRecord);
//                selectedProductToSell.forEach((s, addOn) -> {
//                    SaleProduct saleProduct = new SaleProduct(addOn.getProduct().getProduct_id(), addOn.getAddOnAmount(), saleRecord.getSaleId(), addOn.getProduct().getProduct_name());
//                    saleProducts.add(saleProduct);
//                    salesDao.insertSaleProduct(saleProduct);
//                });
//                addOns.forEach((item, integer) -> {
//                    SaleItem saleItem = new SaleItem(item.getItem_id(), saleRecord.getSaleId(), integer, item.getItem_name());
//                    saleItems.add(saleItem);
//                    salesDao.insertSaleItem(saleItem);
//                });
//
//                return new CompleteSaleInfo(saleRecord, saleItems, saleProducts);
//            }
//
//            @Override
//            public void onFinished(CompleteSaleInfo result) {
//            }
//
//            @Override
//            public void onUI(CompleteSaleInfo result) {
//                onDataReceived.onSuccess(result);
//            }
//
//            @Override
//            public void onError(Exception e) {
//                onDataReceived.onFail(e);
//            }
//        });
    }

    @Override
    public LiveData<List<SaleItem>> getAllAddOnsWithSaleId(Integer saleId) {
        return salesDao.getAllSaleItemWithSaleIdAsync(saleId);
    }

    @Override
    public LiveData<List<SaleProduct>> getAllProductsWithSaleId(Integer saleId) {
        return salesDao.getAllSaleProductWithSaleIdAsync(saleId);
    }

    @Override
    public CompletableFuture<List<Sale>> getAllRecordedSalesOnBranch(String branchId) {
        return CompletableFuture.supplyAsync(() -> salesDao.getAllRecordedSalesAsync(branchId));
    }

    @Override
    public List<SaleItem> getAllSaleItemBySaleIdSync(Integer saleId) {
        return salesDao.getAllSaleItemWithSaleIdSync(saleId);
    }

    @Override
    public List<SaleProduct> getAllSaleProductBySaleIdSync(Integer saleId) {
        return salesDao.getAllSaleProductWithSaleIdSync(saleId);
    }

    @Override
    public void removeLocalSaleById(Integer saleId) {
        salesDao.removeSaleItemWithSaleId(saleId);
        salesDao.removeSaleProductSaleId(saleId);
        salesDao.removeSaleWithSaleId(saleId);
    }

    @Override
    public void clearSale() {
        salesDao.clearSales();
    }

    public String generateId() {
        Random random = new Random();
        int randomSixDigit = 100000 + random.nextInt(900000);
        return "10" + randomSixDigit;
    }




}
