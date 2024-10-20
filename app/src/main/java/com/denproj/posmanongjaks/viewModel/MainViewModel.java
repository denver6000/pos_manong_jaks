package com.denproj.posmanongjaks.viewModel;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.hilt.qualifier.OfflineImpl;
import com.denproj.posmanongjaks.hilt.qualifier.OnlineImpl;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.Product;
import com.denproj.posmanongjaks.repository.base.ItemRepository;
import com.denproj.posmanongjaks.repository.base.ProductRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    MutableLiveData<Boolean> booleanLiveData = new MutableLiveData<>();

    ItemRepository itemRepository;
    ItemRepository itemOfflineRepository;
    ProductRepository productRepository;
    ProductRepository productOfflineRepository;
    @Inject
    public MainViewModel(@OnlineImpl ProductRepository productRepository, @OfflineImpl ProductRepository productOfflineRepository, @OnlineImpl ItemRepository itemRepository, @OfflineImpl ItemRepository itemOfflineRepository) {

        this.productRepository = productRepository;
        this.productOfflineRepository = productOfflineRepository;
        this.itemOfflineRepository = itemOfflineRepository;
        this.itemRepository = itemRepository;
    }

//    public void loadStocksAndProductsInRoom(Context context) {
//        ConnectivityManager connMgr =
//                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        boolean isWifiConn = false;
//        boolean isMobileConn = false;
//
//        for (Network network : connMgr.getAllNetworks()) {
//            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
//            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//                isWifiConn |= networkInfo.isConnected();
//            }
//            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
//                isMobileConn |= networkInfo.isConnected();
//            }
//        }
//
//        if (isWifiConn || isMobileConn) {
//            sync();
//        }
//    }

    public void sync(){
        productRepository.fetchProductsFromBranch(new OnDataReceived<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                result.forEach(product -> productOfflineRepository.insertProductToBranch(product, null));
            }

            @Override
            public void onFail(Exception e) {

            }
        });

        itemRepository.fetchItemsFromBranch(new OnDataReceived<List<Item>>() {
            @Override
            public void onSuccess(List<Item> result){
                itemOfflineRepository.insertItemsToBranch(result, null);
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

    public MutableLiveData<Boolean> getBooleanLiveData() {
        return this.booleanLiveData;
    }

    public void setBooleanLiveData(MutableLiveData<Boolean> booleanLiveData) {
        this.booleanLiveData = booleanLiveData;
    }
}
