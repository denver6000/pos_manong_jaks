package com.denproj.posmanongjaks.repository.base;

import android.net.Uri;

import com.denproj.posmanongjaks.util.OnDataReceived;

public interface ImageRepository {

    void insertImage(Uri uri, OnDataReceived<String> onDataReceived);
    void getImage(String path, OnDataReceived<String> onDataReceived);
}
