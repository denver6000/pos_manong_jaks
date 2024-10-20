package com.denproj.posmanongjaks.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;

public class PingUtil {


    public static void isInternetReachable(Context context, OnUpdateUI<Boolean> booleanOnUpdateUI) {

        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isWifiConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (!isWifiConnected) {
            booleanOnUpdateUI.onFail(new Exception("Wifi Not Available"));
            return;
        }
        AsyncRunner.runAsync(new AsyncRunner.MiniRunner<Boolean>() {
            @Override
            public Boolean onBackground() throws Exception {
                try {
                    Process process = Runtime.getRuntime().exec("/system/bin/ping -c 1 google.com");
                    int returnVal = process.waitFor();
                     return returnVal == 0;
                } catch (InterruptedException | IOException e) {
                    throw new Exception();
                }
            }

            @Override
            public void onUI(Boolean result) {
                booleanOnUpdateUI.onSuccess(result);
            }

            @Override
            public void onError(Exception e) {
                booleanOnUpdateUI.onFail(e);
            }
        });
    }

}
