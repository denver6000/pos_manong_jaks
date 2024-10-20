package com.denproj.posmanongjaks.repository.base;

import com.denproj.posmanongjaks.util.OnDataReceived;

public interface ImageRepository {
//
//    void insertImage(Uri uri, String imageName, OnDataReceived<String> onDataReceived);
    void getImage(String path, OnDataReceived<String> onDataReceived) throws Exception;
}
