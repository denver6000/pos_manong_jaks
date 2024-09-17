package com.denproj.posmanongjaks.util;

public interface OnDataReceived<DATA> {

    public void onSuccess(DATA result);
    public void onFail(Exception e);


}
