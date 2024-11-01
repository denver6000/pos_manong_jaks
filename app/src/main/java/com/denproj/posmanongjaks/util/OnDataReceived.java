package com.denproj.posmanongjaks.util;

public interface OnDataReceived<DATA> {

    void onSuccess(DATA result);
    void onFail(Exception e);


}
