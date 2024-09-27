package com.denproj.posmanongjaks.util;

public interface OnUpdateUI<DATA> {

    public void onSuccess(DATA result);
    public void onFail(Exception e);
}
